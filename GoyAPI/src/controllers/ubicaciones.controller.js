/*
export async function obtenerGeoCodificacion(req, res) {
  const { id } = req.params;
  const evento = await Ubicaciones.findByPk(id)
  res.status(200).json(evento);
}
export async function obtenerUbicacion(req, res) {
  const { id } = req.params;
  const evento = await Ubicaciones.findByPk(id)
  res.status(200).json(evento);
} */

import { Op, literal } from 'sequelize';
import models from '../models/index.js';

// Helper: arma include de eventos si el cliente lo pide (?include=eventos)
function buildIncludes(includeParam) {
  if (!includeParam) return [];
  const wantsEventos = String(includeParam).split(',').includes('eventos');
  return wantsEventos
    ? [{
        model: models.Eventos,
        as: 'idEvento_eventos_ubicacioneseventos', // alias generado por sequelize-auto
        through: { attributes: [] },
      }]
    : [];
}

export async function listarUbicaciones(req, res) {
  const {
    ciudad,
    provincia,
    q,
    lat,
    lon,
    radio,
    minLat,
    minLon,
    maxLat,
    maxLon,
    limit = 50,
    offset = 0,
    include,
  } = req.query ?? {};

  const where = {};

  if (ciudad) where.ciudad = ciudad;
  if (provincia) where.provincia = provincia;

  if (q) {
    where[Op.or] = [
      { nombreLugar:        { [Op.like]: `%${q}%` } },
      { direccionFormateada:{ [Op.like]: `%${q}%` } },
      { calle:              { [Op.like]: `%${q}%` } },
    ];
  }

  // Bounding Box
  if ([minLat, minLon, maxLat, maxLon].every(v => v !== undefined)) {
    where.lat = { [Op.between]: [Number(minLat), Number(maxLat)] };
    where.lon = { [Op.between]: [Number(minLon), Number(maxLon)] };
  }

  // Búsqueda por radio (metros) usando columna espacial `geometria`
  // mysql 8+; `geometria` es POINT SRID 4326
  if (lat !== undefined && lon !== undefined && radio !== undefined) {
    const latNum = Number(lat);
    const lonNum = Number(lon);
    const r = Number(radio);
    // usar ST_Distance_Sphere(geometria, POINT(lon, lat)) <= r
    where[Op.and] = [
      literal(`ST_Distance_Sphere(geometria, ST_SRID(POINT(${lonNum}, ${latNum}), 4326)) <= ${r}`)
    ];
  }

  const includes = buildIncludes(include);

  const rows = await models.Ubicaciones.findAll({
    where,
    include: includes,
    order: [['id', 'ASC']],
    limit: Number(limit),
    offset: Number(offset),
  });

  res.status(200).json(rows);
}

export async function obtenerUbicacion(req, res) {
  const { id } = req.params;
  const includes = buildIncludes(req.query?.include);

  const ubic = await models.Ubicaciones.findByPk(id, { include: includes });
  if (!ubic) return res.status(404).json({ message: 'Ubicación no encontrada' });

  res.status(200).json(ubic);
}

export async function crearUbicacion(req, res) {
  const {
    id, // ignorado si llega
    lat, lon,
    geohash,
    nombreLugar, direccionFormateada, calle, altura,
    ciudad, provincia, codigoPostal, codigoPais,
    idLugar, precisionMetros,
  } = req.body ?? {};

  if (lat === undefined || lon === undefined) {
    return res.status(400).json({ message: 'lat y lon son obligatorios' });
  }

  const nueva = await models.Ubicaciones.create({
    lat, lon, geohash,
    nombreLugar, direccionFormateada, calle, altura,
    ciudad, provincia, codigoPostal, codigoPais,
    idLugar, precisionMetros,
  });

  res
    .status(201)
    .location(`/v1/ubicaciones/${nueva.id}`)
    .json(nueva);
}

export async function actualizarUbicacion(req, res) {
  const { id } = req.params;
  const ubic = await models.Ubicaciones.findByPk(id);
  if (!ubic) return res.status(404).json({ message: 'Ubicación no encontrada' });

  const { id: _, ...changes } = req.body ?? {};
  await ubic.update(changes);

  res.status(200).json(ubic);
}

export async function eliminarUbicacion(req, res) {
  const { id } = req.params;
  const ubic = await models.Ubicaciones.findByPk(id);
  if (!ubic) return res.status(404).json({ message: 'Ubicación no encontrada' });

  await ubic.destroy();
  res.status(204).send();
}
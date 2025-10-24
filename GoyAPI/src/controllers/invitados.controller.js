// src/controllers/invitados.controller.js
import { Op } from 'sequelize';
import models from '../models/index.js';

export async function obtenerInvitado(req, res) {
  const { id } = req.params;
  const include = String(req.query?.include || '');
  const includes = include.split(',').includes('evento')
    ? [{ model: models.Eventos, as: 'idEvento_evento' }]
    : [];

  const invitado = await models.Invitados.findByPk(id, { include: includes });
  if (!invitado) return res.status(404).json({ message: 'Invitado no encontrado' });
  res.status(200).json(invitado);
}

export async function cargarInvitado(req, res){
  const { id, ...data } = req.body ?? {};

    const nuevo = await models.Invitados.create(data);

    res.setHeader('Location', `/v1/invitado/${nuevo.id}`);
    res.status(201).json(nuevo);
}

export async function modificarInvitado(req, res){
  const { id } = req.params;
    const invitado = await models.Invitados.findByPk(id);
    if (!invitado) {
      res.status(404).json({ message: 'invitado no encontrado' });
    } else {
      const { id: _, ...changes } = req.body ?? {};
      await invitado.update(changes);
      res.status(200).json(invitado);
    }
}

//borrado fisico reemplazar por: softdelete: borrado logico
export async function suprimirInvitado(req, res){
  const { id } = req.params;
    const invitado = await models.Invitados.findByPk(id);
    if (!invitado) {
      res.status(404).json({ message: 'invitado no encontrado' });
    } else {
      await invitado.destroy(); // con paranoid:true marca deletedAt
      /* res.status(204).end(); // sin body */
      res.status(200).json({ message: 'Invitado suprimido correctamente'});
    }
}

// GET /v1/invitados
// Filtros opcionales:
//   ?idEvento=123
//   ?q=romero        (busca por nombreArtistico LIKE)
//   ?limit=50&offset=0
//   ?include=evento  (trae el evento asociado usando el alias generado)
export async function listarInvitados(req, res) {
  const { idEvento, q, limit = 50, offset = 0, include } = req.query ?? {};

  const where = {};
  if (idEvento) where.idEvento = Number(idEvento);
  if (q) where.nombreArtistico = { [Op.like]: `%${q}%` };

  const includes = [];
  if (String(include || '').split(',').includes('evento')) {
    // alias que genera sequelize-auto en init-models.js:
    // Invitados.belongsTo(Eventos, { as: "idEvento_evento", foreignKey: "idEvento" });
    includes.push({
      model: models.Eventos,
      as: 'idEvento_evento',
    });
  }

  const rows = await models.Invitados.findAll({
    where,
    include: includes,
    order: [['id', 'ASC']],
    limit: Number(limit),
    offset: Number(offset),
  });

  res.status(200).json(rows);
} 

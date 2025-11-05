import models from '../models/index.js';
import { Op } from 'sequelize';

export async function obtenerOpinion(req, res){
    const { id } = req.params;
    const include = String(req.query?.include || '');
    const includes = include.split(',').includes('evento')
    ? [{ model: models.Eventos, as: 'idEvento_evento' }]
    : [];

    const opinion = await models.Opiniones.findByPk(id, { include: includes });
    if (!opinion) return res.status(404).json({ message: 'Opinion no encontrada' });
    res.status(200).json(opinion);
}
export async function cargarOpinion(req, res){
    const { id, ...data } = req.body ?? {};

    const nuevo = await models.Opiniones.create(data);

    res.setHeader('Location', `/v1/opiniones/${nuevo.id}`);
    res.status(201).json(nuevo);
}
export async function modificarOpinion(req, res){
    const { id } = req.params;
    const opinion = await models.Opiniones.findByPk(id);
    if (!opinion) {
      res.status(404).json({ message: 'opinion no encontrado' });
    } else {
      const { id: _, ...changes } = req.body ?? {};
      await opinion.update(changes);
      res.status(200).json(opinion);
    }
}
export async function suprimirOpinion(req, res){
    const { id } = req.params;
    const opinion = await models.Opiniones.findByPk(id);
    if (!opinion) {
      res.status(404).json({ message: 'opinion no encontrado' });
    } else {
      await opinion.destroy(); // con paranoid:true marca deletedAt
      /* res.status(204).end(); // sin body */
      res.status(200).json({ message: 'opinion suprimida correctamente'});
    }
}

export async function obtenerValoraciones(req, res){
    const { id } = req.params;
    const include = String(req.query?.include || '');
    const includes = include.split(',').includes('evento')
    ? [{ model: models.Eventos, as: 'idEvento_evento' }]
    : [];
    
    const where = {};
    if (id) {
        where.idEvento = parseInt(id);
    }
    
    const opiniones = await models.Opiniones.findAll({
      attributes: ['id', 'idEvento', 'valoracion'],
      where: where,
      include: includes
    });
    
    if (!opiniones || opiniones.length === 0) return res.status(404).json({ message: 'Opiniones no encontradas' });
    res.status(200).json(opiniones);
}

export async function obtenerListaOpinionesEvento(req, res){
  const { id } = req.params;
    const include = String(req.query?.include || '');
    const includes = include.split(',').includes('evento')
    ? [{ model: models.Eventos, as: 'idEvento_evento' }]
    : [];
    
    const where = {};
    if (id) {
        where.idEvento = parseInt(id);
    }
    
    const opiniones = await models.Opiniones.findAll({
      // attributes: ['id', 'idEvento', 'valoracion'],
      where: where,
      include: includes
    });
    
    if (!opiniones || opiniones.length === 0) return res.status(404).json({ message: 'Opiniones no encontradas' });
    res.status(200).json(opiniones);
}
export async function filtrarOpiniones(){}
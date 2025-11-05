import models from '../models/index.js';
import { Op } from 'sequelize';

export async function obtenerHorario(req, res){
    const { id } = req.params;
    const include = String(req.query?.include || '');
    const includes = include.split(',').includes('evento')
    ? [{ model: models.Eventos, as: 'idEvento_evento' }]
    : [];

    const horario = await models.Horarios.findByPk(id, { include: includes });
    if (!horario) return res.status(404).json({ message: 'horario no encontrado' });
    res.status(200).json(horario);
}
export async function cargarHorario(req, res){
    const { id, ...data } = req.body ?? {};

    const nuevo = await models.Horarios.create(data);

    res.setHeader('Location', `/v1/Horario/${nuevo.id}`);
    res.status(201).json(nuevo);
}
export async function modificarHorario(req, res){
    const { id } = req.params;
    const horario = await models.Horarios.findByPk(id);
    if (!horario) {
      res.status(404).json({ message: 'horario no encontrado' });
    } else {
      const { id: _, ...changes } = req.body ?? {};
      await horario.update(changes);
      res.status(200).json(horario);
    }
}
export async function suprimirHorario(req, res){
    const { id } = req.params;
    const horario = await models.Horarios.findByPk(id);
    if (!horario) {
      res.status(404).json({ message: 'horario no encontrado' });
    } else {
      await horario.destroy(); // con paranoid:true marca deletedAt
      /* res.status(204).end(); // sin body */
      res.status(200).json({ message: 'horario suprimido correctamente'});
    }
}

export async function listarHorariosEvento(){}
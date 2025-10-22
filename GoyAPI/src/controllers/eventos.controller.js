import { Evento } from '../config/db.js';

export async function obtenerEvento(req, res) {
  const evento = await Evento.findByPk(req)
  res.json(evento);
}

export async function listarEventos(req, res) {
  const eventos = await Evento.findAll({ order: [['id', 'ASC']] });
  res.json(eventos);
}

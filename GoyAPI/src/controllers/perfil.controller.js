import { Op } from 'sequelize';
import models from '../models/index.js';

export async function obtenerUsuario(req, res) {
  const { id } = req.params;
  const include = String(req.query?.include || '');
  const includes = include.split(',').includes('evento')
    ? [{ model: models.Eventos, as: 'idEvento_evento' }]
    : [];

  const usuario = await models.USuario.findByPk(id, { include: includes });
  if (!usuario) return res.status(404).json({ message: 'usuario no encontrado' });
  res.status(200).json(usuario);
}

export async function cargarUsuario(req, res){
  const { id, ...data } = req.body ?? {};

    const nuevo = await models.Usuario.create(data);

    res.setHeader('Location', `/v1/usuario/${nuevo.id}`);
    res.status(201).json(nuevo);
}

export async function modificarUsuario(req, res){
  const { id } = req.params;
    const usuario = await models.Usuario.findByPk(id);
    if (!usuario) {
      res.status(404).json({ message: 'usuario no encontrado' });
    } else {
      const { id: _, ...changes } = req.body ?? {};
      await usuario.update(changes);
      res.status(200).json(usuario);
    }
}

//borrado fisico reemplazar por: softdelete: borrado logico
export async function suprimirUsuario(req, res){
  const { id } = req.params;
    const usuario = await models.Usuario.findByPk(id);
    if (!usuario) {
      res.status(404).json({ message: 'usuario no encontrado' });
    } else {
      await usuario.destroy(); // con paranoid:true marca deletedAt
      /* res.status(204).end(); // sin body */
      res.status(200).json({ message: 'usuario suprimido correctamente'});
    }
}
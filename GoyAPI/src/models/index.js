import sequelize from '../config/db.js';
import initModels from './init-models.js';

const models = initModels(sequelize);

export { sequelize };
export default models;

export const {
  Eventos,
  Eventotagscategorias,
  Horarios,
  Invitados,
  Opiniones,
  Rol,
  Tagscategorias,
  Ubicaciones,
  Ubicacioneseventos,
  Usuario,
  UsuarioRol,
} = models;

Object.values(models)
  .filter((m) => typeof m.associate === 'function')
  .forEach((m) => m.associate(models));
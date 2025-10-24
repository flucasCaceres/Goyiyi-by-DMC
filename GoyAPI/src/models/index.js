import sequelize from '../config/db.js';
import initModels from './init-models.js'; // ← el generado por sequelize-auto

// ⚠️ Llamalo UNA sola vez en toda la app
const models = initModels(sequelize);

export { sequelize };
export default models;

// opcional: export nombrados si te gustan
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

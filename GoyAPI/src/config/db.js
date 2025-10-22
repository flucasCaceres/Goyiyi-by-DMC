import { Sequelize, DataTypes } from 'sequelize';
import 'dotenv/config';
import EventoModel from '../models/eventos.js'; // tu archivo de modelo Sequelize

export const sequelize = new Sequelize(
  process.env.DB_NAME,
  process.env.DB_USER,
  process.env.DB_PASS,
  {
    host: process.env.DB_HOST,
    port: Number(process.env.DB_PORT),
    dialect: process.env.DB_DIALECT,
    logging: false
  }
);

// Instancia el modelo y expórtalo para usarlo en los controllers
export const Evento = EventoModel.init(sequelize, DataTypes);

export async function initDb() {
  await sequelize.authenticate();
  // En dev, si querés crear/ajustar tablas automáticamente:
  // await sequelize.sync({ alter: true });
  console.log('✅ Conexión MySQL OK');
}

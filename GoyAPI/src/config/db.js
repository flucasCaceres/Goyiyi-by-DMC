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

export const Evento = EventoModel.init(sequelize, DataTypes);

export async function initDb() {
  await sequelize.authenticate();
  console.log('✅ Conexión MySQL OK');
}

export default sequelize;
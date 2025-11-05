import SequelizeAuto from 'sequelize-auto';
import 'dotenv/config';
const dialect= process.env.DB_DIALECT ;
const name = process.env.DB_NAME;
const user = process.env.DB_USER;
const pass = process.env.DB_PASS;
const host = process.env.DB_HOST;
const port = process.env.DB_PORT;
const auto = new SequelizeAuto(name, user, pass, {
  host: host,
  dialect: dialect,
  port: port,
  directory: './src/models',
  caseModel: 'p',            // PascalCase para los nombres de modelo
  caseFile: 'c',             // camelCase para los archivos
  singularize: false,
  lang: 'esm',               // usa import/export
  additional: {
    timestamps: false,       // evita createdAt / updatedAt
    freezeTableName: true,      // no pluraliza el nombre de la tabla
    underscored: false,
  }
});

auto.run()
  .then(() => console.log(':D Modelos generados correctamente.'))
  .catch(err => console.error('D: Error al generar modelos:', err));
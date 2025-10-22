// src/app.js
import express from 'express';
import rutas from './routes/index.js';
import morgan from 'morgan';
/*import cors from 'cors';
import helmet from 'helmet'; */
const app = express();
app.use(express.json());
/* // Middlewares base
app.use(helmet());               // setea cabeceras de seguridad
app.use(cors());                 // habilita CORS (por defecto: * )*/
app.use(morgan('dev'));          // logs HTTP 
// Opcional: ping rápido
app.get('/v1/ping', (req, res) => res.json({ pong: true }));
app.get('/', (req, res) => res.send('OK'));

// Monta todas las rutas de la API bajo /v1
app.use('/v1', rutas);

export default app;


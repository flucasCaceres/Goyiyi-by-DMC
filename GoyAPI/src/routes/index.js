import { Router } from 'express';
import rutasEventos from './eventos.routes.js';

const rutas = Router();

rutas.use('/eventos', rutasEventos); // → /v1/eventos

export default rutas;

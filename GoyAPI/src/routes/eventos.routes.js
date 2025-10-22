import { Router } from 'express';
import { listarEventos } from '../controllers/eventos.controller.js';

const rutasEventos = Router();

// GET /v1/eventos
rutasEventos.get('/', listarEventos);

export default rutasEventos;


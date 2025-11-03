import { Router } from 'express';
import { cargarEvento, obtenerEventoCompleto, listarEventos, modificarEvento, obtenerEvento, suprimirEvento} from '../controllers/eventos.controller.js';

const rutasEventos = Router();

// HTTP /v1/eventos
rutasEventos.post('/', cargarEvento);
rutasEventos.get('/:id', obtenerEvento);
rutasEventos.get('/', listarEventos);
rutasEventos.put('/:id', modificarEvento);
rutasEventos.delete('/:id', suprimirEvento);

rutasEventos.get('/:id/completo', obtenerEventoCompleto);

export default rutasEventos;
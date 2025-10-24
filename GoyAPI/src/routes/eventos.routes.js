import { Router } from 'express';
import { cargarEvento, listarEventos, modificarEvento, obtenerEvento, suprimirEvento} from '../controllers/eventos.controller.js';

const rutasEventos = Router();

// HTTP /v1/eventos
rutasEventos.post('/', cargarEvento);
rutasEventos.get('/:id', obtenerEvento);
rutasEventos.get('/', listarEventos);
rutasEventos.put('/:id', modificarEvento);
rutasEventos.delete('/:id', suprimirEvento);

export default rutasEventos;


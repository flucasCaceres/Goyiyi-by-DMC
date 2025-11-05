import { Router } from 'express';
import { cargarEvento, obtenerEventoCompleto, listarEventos, modificarEvento, obtenerEvento, suprimirEvento } from '../controllers/eventos.controller.js';

const rutasEventos = Router();

// HTTP /v1/eventos
rutasEventos.post('/crear', cargarEvento);
rutasEventos.get('/', listarEventos);
rutasEventos.get('/:id', obtenerEvento);
rutasEventos.get('/:id/completo', obtenerEventoCompleto);
rutasEventos.put('/modificar/:id', modificarEvento);
rutasEventos.delete('/suprimir/:id', suprimirEvento);

export default rutasEventos;
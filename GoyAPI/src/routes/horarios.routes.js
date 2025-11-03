import { Router } from 'express';
import { cargarHorario, modificarHorario, obtenerHorario, suprimirHorario } from '../controllers/horarios.controller.js';

const rutasHorarios = Router();

//HTTP /v1/invitados
rutasHorarios.post('/', cargarHorario);
rutasHorarios.get('/:id', obtenerHorario);
rutasHorarios.put('/:id', modificarHorario);
rutasHorarios.delete('/:id', suprimirHorario);
export default rutasHorarios;
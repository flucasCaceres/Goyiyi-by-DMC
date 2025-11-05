import { Router } from 'express';
import { cargarHorario, modificarHorario, obtenerHorario, suprimirHorario } from '../controllers/horarios.controller.js';

const rutasHorarios = Router();

//HTTP /v1/horarios
rutasHorarios.post('/crear', cargarHorario);
rutasHorarios.get('/:id', obtenerHorario);
rutasHorarios.put('/modificar/:id', modificarHorario);
rutasHorarios.delete('/suprimir/:id', suprimirHorario);
export default rutasHorarios;
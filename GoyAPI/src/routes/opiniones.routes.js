import { Router } from 'express';
import { cargarOpinion, obtenerOpinion, modificarOpinion, suprimirOpinion } from '../controllers/opiniones.controller.js';

const rutasOpiniones = Router();

//HTTP /v1/opiniones
rutasOpiniones.post('/', cargarOpinion);
rutasOpiniones.get('/:id', obtenerOpinion);
rutasOpiniones.put('/:id',modificarOpinion);
rutasOpiniones.delete('/:id',suprimirOpinion);

export default rutasOpiniones;
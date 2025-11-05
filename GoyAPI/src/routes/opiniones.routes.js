import { Router } from 'express';
import { cargarOpinion, obtenerOpinion, obtenerValoraciones, modificarOpinion, suprimirOpinion, obtenerListaOpinionesEvento } from '../controllers/opiniones.controller.js';

const rutasOpiniones = Router();

//HTTP /v1/opiniones
rutasOpiniones.post('/crear', cargarOpinion);
rutasOpiniones.get('/:id', obtenerOpinion);
rutasOpiniones.put('/modificar/:id',modificarOpinion);
rutasOpiniones.delete('/suprimir/:id',suprimirOpinion);
rutasOpiniones.get('/:id/valoracionesEvento', obtenerValoraciones);
rutasOpiniones.get('/evento/:id', obtenerListaOpinionesEvento);

export default rutasOpiniones;
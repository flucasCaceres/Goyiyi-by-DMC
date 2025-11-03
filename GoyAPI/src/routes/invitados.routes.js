import { Router } from 'express';
import { cargarInvitado, modificarInvitado, obtenerInvitado, suprimirInvitado } from '../controllers/invitados.controller.js';

const rutasInvitados = Router();

//HTTP /v1/invitados
rutasInvitados.post('/', cargarInvitado);
rutasInvitados.get('/:id', obtenerInvitado);
rutasInvitados.put('/:id', modificarInvitado);
rutasInvitados.delete('/:id', suprimirInvitado);
export default rutasInvitados;
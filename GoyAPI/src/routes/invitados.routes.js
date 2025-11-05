import { Router } from 'express';
import { cargarInvitado, modificarInvitado, obtenerInvitado, suprimirInvitado } from '../controllers/invitados.controller.js';

const rutasInvitados = Router();

//HTTP /v1/invitados
rutasInvitados.post('/crear', cargarInvitado);
rutasInvitados.get('/:id', obtenerInvitado);
rutasInvitados.put('/modificar/:id', modificarInvitado);
rutasInvitados.delete('/suprimir/:id', suprimirInvitado);
export default rutasInvitados;
import { Router } from 'express';
import rutasEventos from './eventos.routes.js';
import rutasInvitados from './invitados.routes.js';
import rutasOpiniones from './opiniones.routes.js';
import rutasHorarios from './horarios.routes.js';

const rutas = Router();

rutas.use('/crearEvento', rutasEventos);//v1/crearEvento
rutas.use('/eventos', rutasEventos); // → /v1/eventos
rutas.use('/evento', rutasEventos); //v1/evento
rutas.use('/modificarEvento', rutasEventos); //v1/modificarEvento
rutas.use('/suprimirEvento', rutasEventos); //v1/suprimirEvento
rutas.use('/leerEvento', rutasEventos); //v1/leerEvento

rutas.use('/cargarInvitado', rutasInvitados);//cargarinvitado
rutas.use('/invitado', rutasInvitados); //v1/invitado
rutas.use('/modificarInvitado', rutasInvitados);//v1/modificarinvitado
rutas.use('/suprimirInvitado', rutasInvitados);//v1/suprimirinvitado

rutas.use('/cargarOpinion', rutasOpiniones);//v1/cargarOpinion
rutas.use('/Opinion', rutasOpiniones);//v1/opinion
rutas.use('/modificarOpinion', rutasOpiniones);//v1/modificarOpinion
rutas.use('/suprimirOpinion', rutasOpiniones);//v1/suprimirOpinion

rutas.use('/cargarHorario', rutasHorarios);//v1/cargarHorario
rutas.use('/Horario', rutasHorarios);//v1/Horario
rutas.use('/modificarHorario', rutasHorarios);//v1/modificarHorario
rutas.use('/suprimirHorario', rutasHorarios);//v1/suprimirHorario

export default rutas;

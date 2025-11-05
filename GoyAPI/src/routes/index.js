import { Router } from 'express';
import rutasEventos from './eventos.routes.js';
import rutasInvitados from './invitados.routes.js';
import rutasOpiniones from './opiniones.routes.js';
import rutasHorarios from './horarios.routes.js';
import rutasBusqueda from './busqueda.routes.js';

const rutas = Router();

rutas.use('/eventos', rutasEventos); //v1/eventos

rutas.use('/invitados', rutasInvitados); //v1/invitados

rutas.use('/Opiniones', rutasOpiniones);//v1/Opiniones

rutas.use('/Horarios', rutasHorarios);//v1/Horarios

rutas.use('/Busqueda', rutasBusqueda); //v1/Busqueda

export default rutas;

import { Router } from 'express';
import { obtenerResultadoBusqueda} from '../controllers/eventos.controller.js';

const rutasBusqueda = Router();

// HTTP /v1/Busqueda
rutasBusqueda.get('/Eventos', obtenerResultadoBusqueda);
 //tarea -> todos consumen el mismo controlador pero condicional dependiendo de la ruta
//rutasBusqueda.get('/MisEventos', obtenerResultadoBusqueda); (opcional)
//rutasBusqueda.get('/Favoritos', obtenerResultadoBusqueda);
//rutasBusqueda.get('/TopEventos', obtenerResultadoBusqueda);
//rutasBusqueda.get('/Ubicacion', obtenerResultadoBusqueda);

//no es busqueda pero no tengo un router para el endpoint
//rutasBusqueda.get('/Calendario', obtenerResultadoBusqueda);

export default rutasBusqueda;
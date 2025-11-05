import { Sequelize } from 'sequelize';
import { Eventos, Invitados, Horarios, Tagscategorias, Ubicaciones, Opiniones } from '../models/index.js';
/* //va en repositories
async function getEventoById(id) {
  return await Evento.findByPk(id);
} */

export class EventoRepository {
    
    async findEventoCompleto(eventoId) {
        return await Eventos.findByPk(eventoId, {
            include: [
                {
                    model: Eventos,
                    as: 'idEventoPadre_evento',
                    attributes: ['id', 'nombre', 'estado']
                },
                {
                    model: Invitados,
                    as: 'invitados',
                    attributes: ['id', 'nombreArtistico', 'foto', 'descripcion']
                },
                {
                    model: Horarios,
                    as: 'horarios',
                    attributes: ['id', 'inicio', 'fin', 'esRecurrente', 'rrule', 'activo']
                },
                {
                    model: Ubicaciones,
                    as: 'idUbicacion_ubicaciones',
                    attributes: ['id', 'nombreLugar', 'direccionFormateada', 'ciudad', 'provincia', 'lat', 'lon'],
                    through: { attributes: [] }
                },
                {
                    model: Tagscategorias,
                    as: 'idTagsCategorias_tagscategoria',
                    attributes: ['id', 'nombre', 'tipo'],
                    through: { attributes: [] }
                }
            ]
        });
    }

    async findEventosBasicos() {
    return await Eventos.findAll({
        attributes: ['id', 'nombre', 'contLikes', 'organizador', 'precio', 'moneda', 'metodoPago', 'apto'],
        include: [
            {
                model: Ubicaciones,
                as: 'idUbicacion_ubicaciones',
                attributes: ['direccionFormateada'],
                through: { attributes: [] }
            },
            {
                model: Horarios,
                as: 'horarios',
                attributes: ['inicio', 'fin']
            },
            {
                model: Tagscategorias,
                as: 'idTagsCategorias_tagscategoria',
                attributes: ['nombre'],
                through: { attributes: [] }
            },
            {
                model: Opiniones,
                as: 'opiniones',
                attributes: ['valoracion']
            },
            {
                model: Eventos,
                as: 'idEventoPadre_evento',
                attributes: ['nombre'],
                required: false
            }
        ],
        order: [
            ['tipo', 'ASC'],
            ['id', 'ASC']
        ]
    });
    }
}

export const eventoRepository = new EventoRepository();
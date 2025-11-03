import { Eventos, Invitados, Horarios, Tagscategorias, Ubicaciones } from '../models/index.js';

export class EventoRepository {
    
    async findEventoCompleto(eventoId) {
        return await Eventos.findByPk(eventoId, {
            include: [
                // Evento Padre - CORREGIDO
                {
                    model: Eventos,
                    as: 'idEventoPadre_evento', // ← Alias de la relación belongsTo
                    attributes: ['id', 'nombre', 'estado']
                },
                // Invitados - CORREGIDO  
                {
                    model: Invitados,
                    as: 'invitados', // ← Alias de hasMany
                    attributes: ['id', 'nombreArtistico', 'foto', 'descripcion']
                },
                // Horarios - CORREGIDO
                {
                    model: Horarios,
                    as: 'horarios', // ← Alias de hasMany
                    attributes: ['id', 'inicio', 'fin', 'esRecurrente', 'rrule', 'activo']
                },
                // Ubicaciones - CORREGIDO (relación N:N)
                {
                    model: Ubicaciones,
                    as: 'idUbicacion_ubicaciones', // ← Alias de belongsToMany
                    attributes: ['id', 'nombreLugar', 'direccionFormateada', 'ciudad', 'provincia', 'lat', 'lon'],
                    through: { attributes: [] }
                },
                // Tags y Categorías - CORREGIDO (relación N:N)
                {
                    model: Tagscategorias,
                    as: 'idTagsCategorias_tagscategoria', // ← Alias de belongsToMany
                    attributes: ['id', 'nombre', 'tipo'],
                    through: { attributes: [] }
                }
            ]
        });
    }

    async findEventoBasico(eventoId) {
        return await Eventos.findByPk(eventoId);
    }
}

export const eventoRepository = new EventoRepository();
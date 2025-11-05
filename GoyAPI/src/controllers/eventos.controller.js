import { Evento } from '../config/db.js';
import { eventoRepository } from '../repositories/eventos.repo.js';

export async function obtenerEvento(req, res) {
  const { id } = req.params;
  const evento = await Evento.findByPk(id)
  res.status(200).json(evento);
}

export async function cargarEvento(req, res){
  const { id, ...data } = req.body ?? {};

    const nuevo = await Evento.create(data);

    res.setHeader('Location', `/v1/evento/${nuevo.id}`);
    res.status(201).json(nuevo);
}

export async function modificarEvento(req, res){
  const { id } = req.params;
    const evento = await Evento.findByPk(id);
    if (!evento) {
      res.status(404).json({ message: 'Evento no encontrado' });
    } else {
      const { id: _, ...changes } = req.body ?? {};
      await evento.update(changes);
      res.status(200).json(evento);
    }
}
//borrado fisico reemplazar por: softdelete: borrado logico
export async function suprimirEvento(req, res){
  const { id } = req.params;
    const evento = await Evento.findByPk(id);
    if (!evento) {
      res.status(404).json({ message: 'Evento no encontrado' });
    } else {
      await evento.destroy(); // con paranoid:true marca deletedAt
      res.status(204).send(); // sin body
    }
}

//patch eventos { message: 'Evento eliminado con exito' }

export async function listarEventos(req, res) {
  const eventos = await Evento.findAll({ order: [['id', 'ASC']] });
  res.status(200).json(eventos);
}

export async function obtenerEventoCompleto (req, res) {
    try {
        const { id } = req.params;
        
        if (!id || isNaN(id)) {
            return res.status(400).json({
                success: false,
                message: 'ID de evento inválido'
            });
        }

        const evento = await eventoRepository.findEventoCompleto(parseInt(id));

        if (!evento) {
            return res.status(404).json({
                success: false,
                message: 'Evento no encontrado'
            });
        }

        res.json({
            success: true,
            data: evento
        });

    } catch (error) {
        console.error('Error al obtener evento:', error);
        res.status(500).json({
            success: false,
            message: 'Error interno del servidor'
        });
    }
};

export async function obtenerResultadoBusqueda(req, res){
    try {
        const eventos = await eventoRepository.findEventosBasicos();

        if (!eventos || eventos.length === 0) {
            return res.status(404).json({
                success: false,
                message: 'No se encontraron eventos'
            });
        }

        res.json({
            success: true,
            data: eventos,
            count: eventos.length,
            message: `Se encontraron ${eventos.length} eventos`
        });

    } catch (error) {
        console.error('Error al obtener eventos:', error);
        res.status(500).json({
            success: false,
            message: 'Error interno del servidor',
            error: error.message
        });
    }
};
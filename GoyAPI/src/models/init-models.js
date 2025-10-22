import _sequelize from "sequelize";
const DataTypes = _sequelize.DataTypes;
import _Eventos from  "./eventos.js";
import _Eventotagscategorias from  "./eventotagscategorias.js";
import _Horarios from  "./horarios.js";
import _Invitados from  "./invitados.js";
import _Opiniones from  "./opiniones.js";
import _Rol from  "./rol.js";
import _Tagscategorias from  "./tagscategorias.js";
import _Ubicaciones from  "./ubicaciones.js";
import _Ubicacioneseventos from  "./ubicacioneseventos.js";
import _Usuario from  "./usuario.js";
import _UsuarioRol from  "./usuarioRol.js";

export default function initModels(sequelize) {
  const Eventos = _Eventos.init(sequelize, DataTypes);
  const Eventotagscategorias = _Eventotagscategorias.init(sequelize, DataTypes);
  const Horarios = _Horarios.init(sequelize, DataTypes);
  const Invitados = _Invitados.init(sequelize, DataTypes);
  const Opiniones = _Opiniones.init(sequelize, DataTypes);
  const Rol = _Rol.init(sequelize, DataTypes);
  const Tagscategorias = _Tagscategorias.init(sequelize, DataTypes);
  const Ubicaciones = _Ubicaciones.init(sequelize, DataTypes);
  const Ubicacioneseventos = _Ubicacioneseventos.init(sequelize, DataTypes);
  const Usuario = _Usuario.init(sequelize, DataTypes);
  const UsuarioRol = _UsuarioRol.init(sequelize, DataTypes);

  Eventos.belongsToMany(Tagscategorias, { as: 'idTagsCategorias_tagscategoria', through: Eventotagscategorias, foreignKey: "idEvento", otherKey: "idTagsCategorias" });
  Eventos.belongsToMany(Ubicaciones, { as: 'idUbicacion_ubicaciones', through: Ubicacioneseventos, foreignKey: "idEvento", otherKey: "idUbicacion" });
  Tagscategorias.belongsToMany(Eventos, { as: 'idEvento_eventos', through: Eventotagscategorias, foreignKey: "idTagsCategorias", otherKey: "idEvento" });
  Ubicaciones.belongsToMany(Eventos, { as: 'idEvento_eventos_ubicacioneseventos', through: Ubicacioneseventos, foreignKey: "idUbicacion", otherKey: "idEvento" });
  Eventos.belongsTo(Eventos, { as: "idEventoPadre_evento", foreignKey: "idEventoPadre"});
  Eventos.hasMany(Eventos, { as: "eventos", foreignKey: "idEventoPadre"});
  Eventotagscategorias.belongsTo(Eventos, { as: "idEvento_evento", foreignKey: "idEvento"});
  Eventos.hasMany(Eventotagscategorias, { as: "eventotagscategoria", foreignKey: "idEvento"});
  Horarios.belongsTo(Eventos, { as: "idEvento_evento", foreignKey: "idEvento"});
  Eventos.hasMany(Horarios, { as: "horarios", foreignKey: "idEvento"});
  Invitados.belongsTo(Eventos, { as: "idEvento_evento", foreignKey: "idEvento"});
  Eventos.hasMany(Invitados, { as: "invitados", foreignKey: "idEvento"});
  Opiniones.belongsTo(Eventos, { as: "idEvento_evento", foreignKey: "idEvento"});
  Eventos.hasMany(Opiniones, { as: "opiniones", foreignKey: "idEvento"});
  Ubicacioneseventos.belongsTo(Eventos, { as: "idEvento_evento", foreignKey: "idEvento"});
  Eventos.hasMany(Ubicacioneseventos, { as: "ubicacioneseventos", foreignKey: "idEvento"});
  UsuarioRol.belongsTo(Rol, { as: "idRol_rol", foreignKey: "idRol"});
  Rol.hasMany(UsuarioRol, { as: "usuario_rols", foreignKey: "idRol"});
  Eventotagscategorias.belongsTo(Tagscategorias, { as: "idTagsCategorias_tagscategoria", foreignKey: "idTagsCategorias"});
  Tagscategorias.hasMany(Eventotagscategorias, { as: "eventotagscategoria", foreignKey: "idTagsCategorias"});
  Ubicacioneseventos.belongsTo(Ubicaciones, { as: "idUbicacion_ubicacione", foreignKey: "idUbicacion"});
  Ubicaciones.hasMany(Ubicacioneseventos, { as: "ubicacioneseventos", foreignKey: "idUbicacion"});
  UsuarioRol.belongsTo(Usuario, { as: "idUsuario_usuario", foreignKey: "idUsuario"});
  Usuario.hasMany(UsuarioRol, { as: "usuario_rols", foreignKey: "idUsuario"});

  return {
    Eventos,
    Eventotagscategorias,
    Horarios,
    Invitados,
    Opiniones,
    Rol,
    Tagscategorias,
    Ubicaciones,
    Ubicacioneseventos,
    Usuario,
    UsuarioRol,
  };
}

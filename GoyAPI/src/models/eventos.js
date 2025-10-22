import _sequelize from 'sequelize';
const { Model, Sequelize } = _sequelize;

export default class Eventos extends Model {
  static init(sequelize, DataTypes) {
  return super.init({
    id: {
      autoIncrement: true,
      type: DataTypes.INTEGER,
      allowNull: false,
      primaryKey: true
    },
    nombre: {
      type: DataTypes.STRING(100),
      allowNull: false,
      unique: "nombre"
    },
    estado: {
      type: DataTypes.ENUM('CANCELADO','REPROGRAMADO','FINALIZADO','SUCEDIENDO','PROGRAMADO','PAUSADO'),
      allowNull: false,
      defaultValue: "PAUSADO"
    },
    tipo: {
      type: DataTypes.ENUM('PRINCIPAL','SUBEVENTO','PUNTO DE VENTA ENTRADAS'),
      allowNull: false,
      defaultValue: "PRINCIPAL"
    },
    organizador: {
      type: DataTypes.STRING(150),
      allowNull: false
    },
    contLikes: {
      type: DataTypes.INTEGER,
      allowNull: false,
      defaultValue: 0
    },
    contDislikes: {
      type: DataTypes.INTEGER,
      allowNull: false,
      defaultValue: 0
    },
    contParticipantesPosibles: {
      type: DataTypes.INTEGER,
      allowNull: false,
      defaultValue: 0
    },
    idEventoPadre: {
      type: DataTypes.INTEGER,
      allowNull: true,
      references: {
        model: 'eventos',
        key: 'id'
      }
    },
    img: {
      type: DataTypes.TEXT,
      allowNull: true
    },
    enlaceInfo: {
      type: DataTypes.TEXT,
      allowNull: true
    },
    descripcion: {
      type: DataTypes.TEXT,
      allowNull: true
    },
    cupoMax: {
      type: DataTypes.INTEGER,
      allowNull: true
    },
    cupoMin: {
      type: DataTypes.INTEGER,
      allowNull: true
    },
    apto: {
      type: DataTypes.ENUM('+18 AÑOS','TODO PUBLICO','INFANTIL','+8 AÑOS','+21 AÑOS'),
      allowNull: true,
      defaultValue: "TODO PUBLICO"
    },
    contEntradasVendidas: {
      type: DataTypes.INTEGER,
      allowNull: true,
      defaultValue: 0
    },
    metodoPago: {
      type: DataTypes.ENUM('TRANSFERENCIA','EFECTIVO','TARJETA','TODOS','GRATIS'),
      allowNull: true
    },
    moneda: {
      type: DataTypes.CHAR(3),
      allowNull: true,
      defaultValue: "ARS"
    },
    precio: {
      type: DataTypes.DECIMAL(12,2),
      allowNull: true
    }
  }, {
    sequelize,
    tableName: 'eventos',
    timestamps: false,
    freezeTableName: true,
    underscored: false,
    indexes: [
      {
        name: "PRIMARY",
        unique: true,
        using: "BTREE",
        fields: [
          { name: "id" },
        ]
      },
      {
        name: "nombre",
        unique: true,
        using: "BTREE",
        fields: [
          { name: "nombre" },
        ]
      },
      {
        name: "idxEventoPadre",
        using: "BTREE",
        fields: [
          { name: "idEventoPadre" },
        ]
      },
      {
        name: "idxEventosEstado",
        using: "BTREE",
        fields: [
          { name: "estado" },
        ]
      },
      {
        name: "idxEventosTipo",
        using: "BTREE",
        fields: [
          { name: "tipo" },
        ]
      },
      {
        name: "idxEventosPrecio",
        using: "BTREE",
        fields: [
          { name: "precio" },
        ]
      },
    ]
  });
  }
}

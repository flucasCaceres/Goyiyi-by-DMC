import _sequelize from 'sequelize';
const { Model, Sequelize } = _sequelize;

export default class Opiniones extends Model {
  static init(sequelize, DataTypes) {
  return super.init({
    id: {
      autoIncrement: true,
      type: DataTypes.INTEGER,
      allowNull: false,
      primaryKey: true
    },
    idEvento: {
      type: DataTypes.INTEGER,
      allowNull: false,
      references: {
        model: 'eventos',
        key: 'id'
      }
    },
    idUsuario: {
      type: DataTypes.INTEGER,
      allowNull: true
    },
    valoracion: {
      type: DataTypes.TINYINT,
      allowNull: false
    },
    comentarios: {
      type: DataTypes.TEXT,
      allowNull: true
    },
    estado: {
      type: DataTypes.ENUM('ELIMINADO','REPORTADO','POSTEADO'),
      allowNull: false,
      defaultValue: "POSTEADO"
    },
    img: {
      type: DataTypes.TEXT,
      allowNull: true
    }
  }, {
    sequelize,
    tableName: 'opiniones',
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
        name: "idxOpEvento",
        using: "BTREE",
        fields: [
          { name: "idEvento" },
        ]
      },
      {
        name: "idxOpEvVal",
        using: "BTREE",
        fields: [
          { name: "idEvento" },
          { name: "valoracion" },
        ]
      },
    ]
  });
  }
}

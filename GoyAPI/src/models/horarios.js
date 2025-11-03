import _sequelize from 'sequelize';
const { Model, Sequelize } = _sequelize;

export default class Horarios extends Model {
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
    inicio: {
      type: DataTypes.DATE,
      allowNull: false
    },
    fin: {
      type: DataTypes.DATE,
      allowNull: false
    },
    esRecurrente: {
      type: DataTypes.BOOLEAN,
      allowNull: true,
      defaultValue: 0
    },
    rrule: {
      type: DataTypes.STRING(255),
      allowNull: true
    },
    activo: {
      type: DataTypes.BOOLEAN,
      allowNull: true,
      defaultValue: 1
    }
  }, {
    sequelize,
    tableName: 'horarios',
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
        name: "idxEventoInicio",
        using: "BTREE",
        fields: [
          { name: "idEvento" },
          { name: "inicio" },
        ]
      },
      {
        name: "idxInicio",
        using: "BTREE",
        fields: [
          { name: "inicio" },
        ]
      },
      {
        name: "idxFin",
        using: "BTREE",
        fields: [
          { name: "fin" },
        ]
      },
    ]
  });
  }
}

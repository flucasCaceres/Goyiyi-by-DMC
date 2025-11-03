import _sequelize from 'sequelize';
const { Model, Sequelize } = _sequelize;

export default class Ubicacioneseventos extends Model {
  static init(sequelize, DataTypes) {
  return super.init({
    idEvento: {
      type: DataTypes.INTEGER,
      allowNull: false,
      primaryKey: true,
      references: {
        model: 'eventos',
        key: 'id'
      }
    },
    idUbicacion: {
      type: DataTypes.INTEGER,
      allowNull: false,
      primaryKey: true,
      references: {
        model: 'ubicaciones',
        key: 'id'
      }
    }
  }, {
    sequelize,
    tableName: 'ubicacioneseventos',
    timestamps: false,
    freezeTableName: true,
    underscored: false,
    indexes: [
      {
        name: "PRIMARY",
        unique: true,
        using: "BTREE",
        fields: [
          { name: "idEvento" },
          { name: "idUbicacion" },
        ]
      },
      {
        name: "idxEvUbicacionesEventos",
        using: "BTREE",
        fields: [
          { name: "idEvento" },
        ]
      },
      {
        name: "idxUbicUbicacionesEventos",
        using: "BTREE",
        fields: [
          { name: "idUbicacion" },
        ]
      },
    ]
  });
  }
}

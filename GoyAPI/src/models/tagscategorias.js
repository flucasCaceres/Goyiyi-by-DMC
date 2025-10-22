import _sequelize from 'sequelize';
const { Model, Sequelize } = _sequelize;

export default class Tagscategorias extends Model {
  static init(sequelize, DataTypes) {
  return super.init({
    id: {
      autoIncrement: true,
      type: DataTypes.INTEGER,
      allowNull: false,
      primaryKey: true
    },
    nombre: {
      type: DataTypes.STRING(50),
      allowNull: false
    },
    tipo: {
      type: DataTypes.ENUM('categoria','etiqueta'),
      allowNull: true,
      defaultValue: "etiqueta"
    }
  }, {
    sequelize,
    tableName: 'tagscategorias',
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
        name: "ukNombreTipo",
        unique: true,
        using: "BTREE",
        fields: [
          { name: "nombre" },
          { name: "tipo" },
        ]
      },
    ]
  });
  }
}

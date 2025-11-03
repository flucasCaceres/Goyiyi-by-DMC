import _sequelize from 'sequelize';
const { Model, Sequelize } = _sequelize;

export default class Rol extends Model {
  static init(sequelize, DataTypes) {
  return super.init({
    idRol: {
      autoIncrement: true,
      type: DataTypes.INTEGER,
      allowNull: false,
      primaryKey: true
    },
    nombreRol: {
      type: DataTypes.ENUM('DBA','Organizador','Participante','Moderador'),
      allowNull: false
    }
  }, {
    sequelize,
    tableName: 'rol',
    timestamps: false,
    freezeTableName: true,
    underscored: false,
    indexes: [
      {
        name: "PRIMARY",
        unique: true,
        using: "BTREE",
        fields: [
          { name: "idRol" },
        ]
      },
    ]
  });
  }
}

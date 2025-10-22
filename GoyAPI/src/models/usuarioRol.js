import _sequelize from 'sequelize';
const { Model, Sequelize } = _sequelize;

export default class UsuarioRol extends Model {
  static init(sequelize, DataTypes) {
  return super.init({
    idUsuarioRol: {
      autoIncrement: true,
      type: DataTypes.INTEGER,
      allowNull: false,
      primaryKey: true
    },
    idUsuario: {
      type: DataTypes.INTEGER,
      allowNull: false,
      references: {
        model: 'usuario',
        key: 'idUsuario'
      }
    },
    idRol: {
      type: DataTypes.INTEGER,
      allowNull: false,
      references: {
        model: 'rol',
        key: 'idRol'
      }
    }
  }, {
    sequelize,
    tableName: 'usuario_rol',
    timestamps: false,
    freezeTableName: true,
    underscored: false,
    indexes: [
      {
        name: "PRIMARY",
        unique: true,
        using: "BTREE",
        fields: [
          { name: "idUsuarioRol" },
        ]
      },
      {
        name: "idUsuario",
        unique: true,
        using: "BTREE",
        fields: [
          { name: "idUsuario" },
          { name: "idRol" },
        ]
      },
      {
        name: "idRol",
        using: "BTREE",
        fields: [
          { name: "idRol" },
        ]
      },
    ]
  });
  }
}

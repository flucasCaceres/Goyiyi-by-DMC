import _sequelize from 'sequelize';
const { Model, Sequelize } = _sequelize;

export default class Usuario extends Model {
  static init(sequelize, DataTypes) {
  return super.init({
    idUsuario: {
      autoIncrement: true,
      type: DataTypes.INTEGER,
      allowNull: false,
      primaryKey: true
    },
    nombreUsuario: {
      type: DataTypes.STRING(30),
      allowNull: false,
      unique: "nombreUsuario"
    },
    correo: {
      type: DataTypes.STRING(150),
      allowNull: false,
      unique: "correo"
    },
    telefono: {
      type: DataTypes.STRING(20),
      allowNull: true,
      unique: "telefono"
    },
    contrasena: {
      type: DataTypes.STRING(255),
      allowNull: false
    },
    estado: {
      type: DataTypes.ENUM('activo','bloqueado','eliminado','pendiente_verificacion'),
      allowNull: true,
      defaultValue: "pendiente_verificacion"
    },
    nombreCompleto: {
      type: DataTypes.STRING(160),
      allowNull: true
    },
    fechaNacimiento: {
      type: DataTypes.DATEONLY,
      allowNull: true
    },
    biografia: {
      type: DataTypes.TEXT,
      allowNull: true
    },
    fotoPerfil: {
      type: DataTypes.STRING(255),
      allowNull: true
    },
    confiabilidad: {
      type: DataTypes.ENUM('no confiable','poco confiable','aceptable','confiable','muy confiable'),
      allowNull: true
    },
    emailVerificado: {
      type: DataTypes.BOOLEAN,
      allowNull: true,
      defaultValue: 0
    },
    telefonoVerificado: {
      type: DataTypes.BOOLEAN,
      allowNull: true,
      defaultValue: 0
    },
    intentosFallidos: {
      type: DataTypes.INTEGER,
      allowNull: true,
      defaultValue: 0
    },
    ultimoLogin: {
      type: DataTypes.DATE,
      allowNull: true
    },
    tokenRecuperacion: {
      type: DataTypes.STRING(255),
      allowNull: true
    },
    tokenExpira: {
      type: DataTypes.DATE,
      allowNull: true
    },
    creacion: {
      type: DataTypes.DATE,
      allowNull: true,
      defaultValue: Sequelize.Sequelize.literal('CURRENT_TIMESTAMP')
    },
    actualizacion: {
      type: DataTypes.DATE,
      allowNull: true,
      defaultValue: Sequelize.Sequelize.literal('CURRENT_TIMESTAMP')
    }
  }, {
    sequelize,
    tableName: 'usuario',
    timestamps: false,
    freezeTableName: true,
    underscored: false,
    indexes: [
      {
        name: "PRIMARY",
        unique: true,
        using: "BTREE",
        fields: [
          { name: "idUsuario" },
        ]
      },
      {
        name: "nombreUsuario",
        unique: true,
        using: "BTREE",
        fields: [
          { name: "nombreUsuario" },
        ]
      },
      {
        name: "correo",
        unique: true,
        using: "BTREE",
        fields: [
          { name: "correo" },
        ]
      },
      {
        name: "telefono",
        unique: true,
        using: "BTREE",
        fields: [
          { name: "telefono" },
        ]
      },
    ]
  });
  }
}

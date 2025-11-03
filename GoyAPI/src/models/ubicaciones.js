import _sequelize from 'sequelize';
const { Model, Sequelize } = _sequelize;

export default class Ubicaciones extends Model {
  static init(sequelize, DataTypes) {
  return super.init({
    id: {
      autoIncrement: true,
      type: DataTypes.INTEGER,
      allowNull: false,
      primaryKey: true
    },
    lat: {
      type: DataTypes.DOUBLE,
      allowNull: false
    },
    lon: {
      type: DataTypes.DOUBLE,
      allowNull: false
    },
    geohash: {
      type: DataTypes.STRING(12),
      allowNull: true
    },
    geometria: {
      type: "POINT",
      allowNull: false
    },
    nombreLugar: {
      type: DataTypes.STRING(160),
      allowNull: true
    },
    direccionFormateada: {
      type: DataTypes.STRING(255),
      allowNull: true
    },
    calle: {
      type: DataTypes.STRING(120),
      allowNull: true
    },
    altura: {
      type: DataTypes.STRING(20),
      allowNull: true
    },
    ciudad: {
      type: DataTypes.STRING(120),
      allowNull: true
    },
    provincia: {
      type: DataTypes.STRING(120),
      allowNull: true
    },
    codigoPostal: {
      type: DataTypes.STRING(20),
      allowNull: true
    },
    codigoPais: {
      type: DataTypes.CHAR(2),
      allowNull: true
    },
    idLugar: {
      type: DataTypes.STRING(120),
      allowNull: true
    },
    precisionMetros: {
      type: DataTypes.DECIMAL(7,2),
      allowNull: true
    }
  }, {
    sequelize,
    tableName: 'ubicaciones',
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
        name: "idxUbicCiudad",
        using: "BTREE",
        fields: [
          { name: "ciudad" },
        ]
      },
      {
        name: "idxUbicProvCiud",
        using: "BTREE",
        fields: [
          { name: "provincia" },
          { name: "ciudad" },
        ]
      },
      {
        name: "idxUbicGeohash",
        using: "BTREE",
        fields: [
          { name: "geohash" },
        ]
      },
      {
        name: "idxUbicGeometria",
        type: "SPATIAL",
        fields: [
          { name: "geometria", length: 32 },
        ]
      },
    ]
  });
  }
}

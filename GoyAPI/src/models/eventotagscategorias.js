import _sequelize from 'sequelize';
const { Model, Sequelize } = _sequelize;

export default class Eventotagscategorias extends Model {
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
    idTagsCategorias: {
      type: DataTypes.INTEGER,
      allowNull: false,
      primaryKey: true,
      references: {
        model: 'tagscategorias',
        key: 'id'
      }
    }
  }, {
    sequelize,
    tableName: 'eventotagscategorias',
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
          { name: "idTagsCategorias" },
        ]
      },
      {
        name: "idxETCEv",
        using: "BTREE",
        fields: [
          { name: "idEvento" },
        ]
      },
      {
        name: "idxETCTagCat",
        using: "BTREE",
        fields: [
          { name: "idTagsCategorias" },
        ]
      },
    ]
  });
  }
}

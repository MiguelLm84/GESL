package com.example.geslapp.core.clases;

public class Material_invent {

    private String name;
    private String ubi;
    private String reti;
    private String tienda;
    private String foto;
    private String dbName;

    public Material_invent(String name,String dbName, String ubi, String reti, String tienda, String foto) {
        this.name = name;
        this.ubi = ubi;
        this.reti = reti;
        this.tienda = tienda;
        this.foto = foto;
        this.dbName = dbName;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUbi() {
        return ubi;
    }

    public void setUbi(String ubi) {
        this.ubi = ubi;
    }

    public String getReti() {
        return reti;
    }

    public void setReti(String reti) {
        this.reti = reti;
    }

    public String getTienda() {
        return tienda;
    }

    public void setTienda(String tienda) {
        this.tienda = tienda;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }
}

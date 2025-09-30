package application.model;

public class Szalloda_foglalas extends BaseModel {
    public int szalloda_foglalas_id;
    public int szalloda_id;
    public int felhaszn_id;
    public String foglalas_kezdete;
    public String foglalas_vege;
    public int ar;
    public int fo;

    public Szalloda_foglalas() {
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Szalloda_foglalas szallodaFoglalas = new Szalloda_foglalas();
        szallodaFoglalas.szalloda_foglalas_id = this.szalloda_foglalas_id;
        szallodaFoglalas.szalloda_id = this.szalloda_id;
        szallodaFoglalas.felhaszn_id = this.felhaszn_id;
        szallodaFoglalas.foglalas_kezdete = this.foglalas_kezdete;
        szallodaFoglalas.foglalas_vege = this.foglalas_vege;
        szallodaFoglalas.ar = this.ar;
        szallodaFoglalas.fo = this.fo;
        return szallodaFoglalas;
    }
}

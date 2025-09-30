package application.model;

public class Repjegy_foglalas extends BaseModel{
    public int repjegy_foglalas_id;
    public String foglalasi_idopont;
    public int jaratszam;
    public String fizetve;
    public int jegydarab;
    public  String jegytipus;
    public String osztaly;

    public int felhaszn_id;

    public Repjegy_foglalas(int repjegy_foglalas_id, String foglalasi_idopont, int jaratszam, String fizetve, int jegydarab, String osztaly, int felhaszn_id) {
        this.repjegy_foglalas_id = repjegy_foglalas_id;
        this.foglalasi_idopont = foglalasi_idopont;
        this.jaratszam = jaratszam;
        this.fizetve = fizetve;
        this.jegydarab = jegydarab;
        this.osztaly = osztaly;
        this.felhaszn_id = felhaszn_id;
    }

    public Repjegy_foglalas(){}

    @Override
    public Object clone() throws CloneNotSupportedException {
        Repjegy_foglalas repjegyFoglalas = new Repjegy_foglalas();
        repjegyFoglalas.repjegy_foglalas_id = this.repjegy_foglalas_id;
        repjegyFoglalas.foglalasi_idopont = this.foglalasi_idopont;
        repjegyFoglalas.jaratszam = this.jaratszam;
        repjegyFoglalas.fizetve = this.fizetve;
        repjegyFoglalas.jegydarab = this.jegydarab;
        repjegyFoglalas.jegytipus = this.jegytipus;
        repjegyFoglalas.osztaly = this.osztaly;
        repjegyFoglalas.felhaszn_id = this.felhaszn_id;
        return repjegyFoglalas;
    }
}

package application.model;

public class Arajanlat extends BaseModel {
    public int arajanlat_id;
    public int jaratszam;
    public int ar;
    public int kedvezmeny;

    public Arajanlat(int arajanlat_id, int jaratszam, int ar, int kedvezmeny) {
        this.arajanlat_id = arajanlat_id;
        this.jaratszam = jaratszam;
        this.ar = ar;
        this.kedvezmeny = kedvezmeny;
    }

    public Arajanlat(){}

    @Override
    public Object clone() throws CloneNotSupportedException {
        Arajanlat arajanlat = new Arajanlat();
        arajanlat.arajanlat_id = this.arajanlat_id;
        arajanlat.jaratszam = this.jaratszam;
        arajanlat.ar = this.ar;
        arajanlat.kedvezmeny = this.kedvezmeny;

        return arajanlat;
    }
}

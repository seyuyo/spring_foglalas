package application.model;

public class Szalloda extends BaseModel{

    public int szalloda_id;
    public String nev;
    public String varos;
    public String cim;
    public int szoba_ferohelyek_szama;
    public int ar;
    public int admin_id;

    public Szalloda(int szalloda_id, String nev, String cim, String varos, int szoba_ferohelyek_szama, int ar) {
        this.szalloda_id = szalloda_id;
        this.nev = nev;
        this.cim = cim;
        this.varos = varos;
        this.szoba_ferohelyek_szama = szoba_ferohelyek_szama;
        this.ar = ar;
    }

    public Szalloda(){}

    @Override
    public Object clone() throws CloneNotSupportedException {
        Szalloda szalloda =  new Szalloda();
        szalloda.szalloda_id =  this.szalloda_id;
        szalloda.nev =  this.nev;
        szalloda.cim =  this.cim;
        szalloda.szoba_ferohelyek_szama =  this.szoba_ferohelyek_szama;
        szalloda.ar =  this.ar;
        szalloda.admin_id =  this.admin_id;

        return szalloda;
    }
}

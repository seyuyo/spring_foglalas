package application.model;

public class SzallodaFoglalasForUser extends BaseModel{
    public int szalloda_foglalas_id;
    public String nev;
    public String varos;
    public String cim;
    public String foglalas_kezdete;
    public String foglalas_vege;
    public int ar;
    public int fo;

    public SzallodaFoglalasForUser(String nev, String varos, String cim, String foglalas_kezdete, String foglalas_vege, int ar, int fo) {
        this.nev = nev;
        this.varos = varos;
        this.cim = cim;
        this.foglalas_kezdete = foglalas_kezdete;
        this.foglalas_vege = foglalas_vege;
        this.ar = ar;
        this.fo = fo;
    }

    public SzallodaFoglalasForUser() {}

    @Override
    public Object clone() throws CloneNotSupportedException {
        SzallodaFoglalasForUser szallodaFoglalasForUser = new SzallodaFoglalasForUser();
        szallodaFoglalasForUser.szalloda_foglalas_id = this.szalloda_foglalas_id;
        szallodaFoglalasForUser.nev = this.nev;
        szallodaFoglalasForUser.varos = this.varos;
        szallodaFoglalasForUser.cim = this.cim;
        szallodaFoglalasForUser.foglalas_kezdete = this.foglalas_kezdete;
        szallodaFoglalasForUser.foglalas_vege = this.foglalas_vege;
        szallodaFoglalasForUser.ar = this.ar;
        szallodaFoglalasForUser.fo = this.fo;
        return szallodaFoglalasForUser;
    }
}

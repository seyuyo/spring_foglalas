package application.model;

public class RepjegyFoglalasForUser extends BaseModel{
    public int repjegy_foglalas_id;
    public int jaratszam;
    public String indulasi_varos;
    public String erkezesi_varos;
    public String indulasi_idopont;
    public String erkezesi_idopont;
    public String fizetve;
    public int jegydarab;
    public String jegytipus;
    public String osztaly;
    public String nev;


    public RepjegyFoglalasForUser() {}

    @Override
    public Object clone() throws CloneNotSupportedException {
        RepjegyFoglalasForUser repjegyFoglalasForUser = new RepjegyFoglalasForUser();
        repjegyFoglalasForUser.repjegy_foglalas_id = this.repjegy_foglalas_id;
        repjegyFoglalasForUser.jaratszam = this.jaratszam;
        repjegyFoglalasForUser.indulasi_varos = this.indulasi_varos;
        repjegyFoglalasForUser.erkezesi_varos = this.erkezesi_varos;
        repjegyFoglalasForUser.indulasi_idopont = this.indulasi_idopont;
        repjegyFoglalasForUser.erkezesi_idopont = this.erkezesi_idopont;
        repjegyFoglalasForUser.fizetve = this.fizetve;
        repjegyFoglalasForUser.jegydarab = this.jegydarab;
        repjegyFoglalasForUser.jegytipus = this.jegytipus;
        repjegyFoglalasForUser.osztaly = this.osztaly;
        repjegyFoglalasForUser.nev = this.nev;
        return repjegyFoglalasForUser;
    }
}

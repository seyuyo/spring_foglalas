package application.model;

import java.math.BigInteger;

public class Felhasznalo extends BaseModel{
    public int felhaszn_id;
    public String nev;
    public String jelszo;
    public String e_mail;
    public String telefonszam;
    public Integer admin_id;

    public Felhasznalo(){}

    @Override
    public Object clone() throws CloneNotSupportedException {
        Felhasznalo felhasznalo = new Felhasznalo();
        felhasznalo.felhaszn_id =  this.felhaszn_id;
        felhasznalo.nev =  this.nev;
        felhasznalo.jelszo =  this.jelszo;
        felhasznalo.e_mail =  this.e_mail;
        felhasznalo.telefonszam =  this.telefonszam;
        felhasznalo.admin_id =  this.admin_id;
        return felhasznalo;
    }

    public int getId() {
        return felhaszn_id;
    }

}

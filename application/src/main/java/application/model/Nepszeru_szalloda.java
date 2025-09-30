package application.model;

public class Nepszeru_szalloda extends BaseModel{
    public String szalloda_nev;
    public int foglalasok_szama;

    public Nepszeru_szalloda() {}

    public Object clone() throws CloneNotSupportedException {
        Nepszeru_szalloda nepszeruSzalloda = new Nepszeru_szalloda();
        nepszeruSzalloda.szalloda_nev = this.szalloda_nev;
        nepszeruSzalloda.foglalasok_szama = this.foglalasok_szama;
        return nepszeruSzalloda;
    }
}

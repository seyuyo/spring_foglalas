package application.model;

public class Biztosito extends BaseModel{
    public int biztosito_kod;
    public String nev;
    public String leiras;

    public Biztosito(int biztosito_kod, String nev, String leiras) {
        this.biztosito_kod = biztosito_kod;
        this.nev = nev;
        this.leiras = leiras;
    }

    public Biztosito(){}

    @Override
    public Object clone() throws CloneNotSupportedException {
        Biztosito biztosito = new Biztosito();
        biztosito.biztosito_kod =  this.biztosito_kod;
        biztosito.nev =  this.nev;
        biztosito.leiras =  this.leiras;
        return biztosito;
    }
}

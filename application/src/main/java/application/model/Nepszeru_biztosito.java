package application.model;

public class Nepszeru_biztosito extends BaseModel{
    public String biztosito_neve;
    public String fajtak;
    public int ar;

    public Nepszeru_biztosito() { }

    public Object clone() throws CloneNotSupportedException {
        Nepszeru_biztosito nepszeruBiztosito = new Nepszeru_biztosito();
        nepszeruBiztosito.biztosito_neve = this.biztosito_neve;
        nepszeruBiztosito.fajtak = this.fajtak;
        nepszeruBiztosito.ar = this.ar;
        return nepszeruBiztosito;
    }
}

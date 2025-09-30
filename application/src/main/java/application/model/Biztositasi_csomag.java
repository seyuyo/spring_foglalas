package application.model;

public class Biztositasi_csomag extends BaseModel{

    public int csomag_id;
    public String fajtak;
    public int ar;
    public int biztosito_kod;

    public Biztositasi_csomag(int csomag_id, String fajtak, int ar, int biztosito_kod) {
        this.csomag_id = csomag_id;
        this.fajtak = fajtak;
        this.ar = ar;
        this.biztosito_kod = biztosito_kod;
    }

    public Biztositasi_csomag(){}

    @Override
    public Object clone() throws CloneNotSupportedException {
        Biztositasi_csomag biztositasiCsomag = new Biztositasi_csomag();
        biztositasiCsomag.csomag_id = this.csomag_id;
        biztositasiCsomag.fajtak = this.fajtak;
        biztositasiCsomag.ar = this.ar;
        biztositasiCsomag.biztosito_kod = this.biztosito_kod;
        return biztositasiCsomag;
    }
}

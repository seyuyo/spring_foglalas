package application.model;

public class Repulojarat extends BaseModel{
   public int jaratszam;
   public String indulasi_varos;
   public String erkezesi_varos;
   public String indulasi_idopont;
   public String erkezesi_idopont;
   public int ulesek_szama;
   public int utasok_szama;
   public int admin_id;
   public int legitarsasag_kod;

    public Repulojarat(int jaratszam, String indulasi_varos, String erkezesi_varos, String indulasi_idopont, String erkezesi_idopont, int ulesek_szama, int utasok_szama, int admin_id, int legitarsasag_kod) {
        this.jaratszam = jaratszam;
        this.indulasi_varos = indulasi_varos;
        this.erkezesi_varos = erkezesi_varos;
        this.indulasi_idopont = indulasi_idopont;
        this.erkezesi_idopont = erkezesi_idopont;
        this.ulesek_szama = ulesek_szama;
        this.utasok_szama = utasok_szama;
        this.admin_id = admin_id;
        this.legitarsasag_kod = legitarsasag_kod;
    }
    public Repulojarat(){}

    @Override
    public Object clone() throws CloneNotSupportedException {
        Repulojarat repulojarat =  new Repulojarat();
        repulojarat.jaratszam = this.jaratszam;
        repulojarat.indulasi_varos = this.indulasi_varos;
        repulojarat.erkezesi_varos = this.erkezesi_varos;
        repulojarat.indulasi_idopont = this.indulasi_idopont;
        repulojarat.erkezesi_idopont = this.erkezesi_idopont;
        repulojarat.ulesek_szama = this.ulesek_szama;
        repulojarat.utasok_szama = this.utasok_szama;
        repulojarat.admin_id = this.admin_id;
        repulojarat.legitarsasag_kod = this.legitarsasag_kod;

        return repulojarat;
    }
}

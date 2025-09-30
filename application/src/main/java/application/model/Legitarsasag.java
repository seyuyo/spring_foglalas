package application.model;

public class Legitarsasag extends BaseModel{
    public int legitarsasag_kod;

    public String nev;
    public String orszag;
    public String flotta;
    public int admin_id;

    public Legitarsasag(int legitarsasag_kod, String nev, String orszag, String flotta, int admin_id) {
        this.legitarsasag_kod = legitarsasag_kod;
        this.nev = nev;
        this.orszag = orszag;
        this.flotta = flotta;
        this.admin_id = admin_id;
    }

    public Legitarsasag(){}

    @Override
    public Object clone() throws CloneNotSupportedException {
        Legitarsasag legitarsasag =  new Legitarsasag();
        legitarsasag.legitarsasag_kod = this.legitarsasag_kod;
        legitarsasag.nev = this.nev;
        legitarsasag.orszag = this.orszag;
        legitarsasag.flotta = this.flotta;
        legitarsasag.admin_id = this.admin_id;
        return legitarsasag;
    }
}

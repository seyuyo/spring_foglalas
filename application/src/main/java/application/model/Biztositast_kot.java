package application.model;

public class Biztositast_kot extends BaseModel{

    public int felhaszn_id;
    public int biztosito_kod;

    public Biztositast_kot(int felhaszn_id, int biztosito_kod) {
        this.felhaszn_id = felhaszn_id;
        this.biztosito_kod = biztosito_kod;
    }

    public Biztositast_kot(){};

    @Override
    public Object clone() throws CloneNotSupportedException {
        Biztositast_kot biztositastKot = new Biztositast_kot();
        biztositastKot.felhaszn_id = this.felhaszn_id;
        biztositastKot.biztosito_kod = this.biztosito_kod;

        return biztositastKot;
    }
}

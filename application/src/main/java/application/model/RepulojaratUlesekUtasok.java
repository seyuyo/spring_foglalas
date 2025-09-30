package application.model;

public class RepulojaratUlesekUtasok extends BaseModel{
    public int ulesek_szama;
    public int utasok_szama;

    public RepulojaratUlesekUtasok() {}

    @Override
    public Object clone() throws CloneNotSupportedException {
        RepulojaratUlesekUtasok szabadHelyek = new RepulojaratUlesekUtasok();
        szabadHelyek.ulesek_szama = this.ulesek_szama;
        szabadHelyek.utasok_szama = this.utasok_szama;
        return szabadHelyek;
    }
}

package application.model;

public class Admin extends BaseModel {
    public int admin_id;
    public String nev;
    public String jelszo;

    public String e_mail;

    public Admin(int admin_id, String nev, String jelszo, String e_mail) {
        this.admin_id = admin_id;
        this.nev = nev;
        this.jelszo = jelszo;
        this.e_mail = e_mail;
    }

    public Admin() {
    }

    @Override
    public String toString() {
        return "Admin{" +
                "admin_id=" + admin_id +
                ", nev='" + nev + '\'' +
                ", jelszo='" + jelszo + '\'' +
                ", e_mail='" + e_mail + '\'' +
                '}';
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Admin returnValue = new Admin();

        returnValue.admin_id = this.admin_id;
        returnValue.nev = this.nev;
        returnValue.jelszo = this.jelszo;
        returnValue.e_mail = this.e_mail;

        return returnValue;
    }
}

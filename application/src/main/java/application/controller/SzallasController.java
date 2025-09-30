package application.controller;

import application.config.Session;
import application.dao.BasicDAO;
import application.model.Felhasznalo;
import application.model.Repjegy_foglalas;
import application.model.Szalloda;
import application.model.Szalloda_foglalas;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Controller
public class SzallasController {
    private BasicDAO<Szalloda> szallodaBasicDAO = new BasicDAO<Szalloda>();
    private BasicDAO<Szalloda_foglalas> szallodaFoglalasBasicDAO = new BasicDAO<>();
    @PostMapping("/szallodaFoglalas/{szid}")
    public String szallodaFoglalas(@PathVariable("szid") String szid, Model model) throws Exception {
        Felhasznalo felhasznalo = Session.getUserBySession(RequestContextHolder.getRequestAttributes().getSessionId());
        if(felhasznalo == null){
            //hibaüzenet ha nem vagyunk bejelentkezve
            model.addAttribute("message", "A foglaláshoz előbb be kell jelentkezni!");
            return  "Szalloda";
        }
        List<Szalloda> szalloda = szallodaBasicDAO.select(new Szalloda(), "SZALLODA_ID", szid);
        model.addAttribute("szalloda", szalloda.get(0));
        model.addAttribute("szid", szid);
        return "Szalloda_foglalas";
    }
    @PostMapping(value = "/foglal_szallas")
    public String foglal_szallas(Model model, @RequestParam("szid") String szid, @RequestParam("foglalas_kezdete") String foglalas_kezdete,
                                 @RequestParam("foglalas_vege") String foglalas_vege, @RequestParam("fo_gyerek") String fo_gyerek, @RequestParam("fo_felnott") String fo_felnott) throws Exception {
        System.out.println(szid + " " + foglalas_kezdete + " " + foglalas_vege + " " + fo_gyerek + " " +  fo_felnott);
        Felhasznalo felhasznalo = Session.getUserBySession(RequestContextHolder.getRequestAttributes().getSessionId());

        if (felhasznalo == null) {
            return "Szalloda";
        }

        //megnézzük, hogy vannak-e szabad szoba user inputra
        String szabadSzobak = String.format("SELECT SUM(sf.FO) AS foglalasok FROM SZALLODA_FOGLALAS sf WHERE sf.SZALLODA_ID = '%s' " +
                "AND (sf.FOGLALAS_VEGE >= TO_DATE('%s 00:00', 'DD/MM/YYYY HH24:MI') AND sf.FOGLALAS_KEZDETE >= TO_DATE( '%s 00:00', 'DD/MM/YYYY HH24:MI')) GROUP BY sf.SZALLODA_ID" , szid, datum_megfordit(foglalas_vege), datum_megfordit(foglalas_kezdete));
        ResultSet resultSet = BasicDAO.SelectSmth(szabadSzobak);
        List<Szalloda> szallodaList = szallodaBasicDAO.select(new Szalloda(), "SZALLODA_ID", szid);

        Szalloda szalloda = szallodaList.get(0);
        boolean nincs = false;
        try {
            resultSet.wasNull();
        }catch (Exception e){
            nincs = true;
        }
        if(!nincs){
            resultSet.next();
            if (szalloda.szoba_ferohelyek_szama-resultSet.getInt("foglalasok") < Integer.parseInt(fo_felnott)+Integer.parseInt(fo_gyerek)) {
                //hibaüzenet ha nincs szabad szoba
                model.addAttribute("message", "Nincs elég szabad szoba a foglaláshoz!");
                return "Szalloda";
            }
        } else{
            String insert_sql = String.format("INSERT INTO SZALLODA_FOGLALAS (SZALLODA_ID, FELHASZN_ID, FOGLALAS_KEZDETE, FOGLALAS_VEGE, AR, FO) VALUES (%s, %s, TO_DATE( '%s 00:00', 'DD/MM/YYYY HH24:MI'), TO_DATE( '%s 00:00', 'DD/MM/YYYY HH24:MI'), %s, '%s')",
                    szid, felhasznalo.felhaszn_id, datum_megfordit(foglalas_kezdete), datum_megfordit(foglalas_vege),
                    String.valueOf(szalloda.ar*(Integer.parseInt(fo_felnott)+Integer.parseInt(fo_gyerek))), String.valueOf(Integer.parseInt(fo_felnott)+Integer.parseInt(fo_gyerek)));
            if(BasicDAO.UpdateSmth(insert_sql)){
                model = FelhasznaloController.getUserIzek(felhasznalo,model);
                model.addAttribute("Felhasznalo", felhasznalo);
                return "Profile";
                //sikeres foglalas
            }

        }
        model = FelhasznaloController.getUserIzek(felhasznalo,model);
        model.addAttribute("Felhasznalo", felhasznalo);
        return "Profile";
    }



    @PostMapping(value = "/szallodafoglalas_torles/{jaratszam}")
    public String szallodafoglalas_torles(Model model, @PathVariable("jaratszam") String szallodaFoglalasID) throws Exception {
        List<Szalloda_foglalas> szallodaFoglalas = szallodaFoglalasBasicDAO.select(new Szalloda_foglalas(), "SZALLODA_FOGLALAS_ID", szallodaFoglalasID);
        Szalloda_foglalas foglalas = null;
        if(szallodaFoglalas.size() == 1){
            foglalas = szallodaFoglalas.get(0);
            szallodaFoglalasBasicDAO.delete(foglalas);
        }
        model = FelhasznaloController.getUserIzek(Session.getUserBySession(RequestContextHolder.getRequestAttributes().getSessionId()), model);
        model.addAttribute("Felhasznalo", Session.getUserBySession(RequestContextHolder.getRequestAttributes().getSessionId()));
        return "Profile";
    }

    public String datum_megfordit(String datum){
        String ev = datum.substring(0,4);
        String ho = datum.substring(5,7);
        String nap = datum.substring(8,10);
        return nap + "/" + ho + "/" + ev;
    }
}


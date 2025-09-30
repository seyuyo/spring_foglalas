package application.controller;

import application.config.Session;
import application.dao.BasicDAO;
import application.model.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.ModelAndView;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static javax.swing.UIManager.getInt;

@Controller
public class RepjegyController {
    private BasicDAO<Repulojarat> repulojaratBasicDAO = new BasicDAO<Repulojarat>();
    private BasicDAO<Repjegy_foglalas> repjegyFoglalasBasicDAO = new BasicDAO<>();

    private BasicDAO<RepulojaratUlesekUtasok> RepulojaratUlesekUtasokBasicDAO = new BasicDAO<>();
       @PostMapping(value = "/repjegyListazFooldal")
    public ModelAndView repjegyListaz(@RequestParam("indulasi_varos") String indulasi_varos,
                                      @RequestParam("erkezesi_varos") String erkezesi_varos,
                                      @RequestParam("indulas") String indulas,
                                      @RequestParam("visszaut") String visszaut) throws Exception {
        List<Repulojarat> repulojaratList = new ArrayList<>();
        if(indulasi_varos.equals("") && erkezesi_varos.equals("")){
            repulojaratList = repulojaratBasicDAO.selectAll(new Repulojarat());
        }else{
            String sql = "SELECT * FROM REPULOJARAT WHERE ";
            if(!indulasi_varos.equals("")){
                sql+= "INDULASI_VAROS='" + indulasi_varos + "' ";
            }
            if(!indulasi_varos.equals("") && !erkezesi_varos.equals("")){
                sql+= "AND ERKEZESI_VAROS='" + erkezesi_varos + "' ";
            } else if(!erkezesi_varos.equals("")){
                sql+= "ERKEZESI_VAROS='" + erkezesi_varos + "'";
            }
            if (!indulas.isEmpty()) {
                sql += " AND TRUNC(INDULAS_DATUM) >= TO_DATE('" + indulas + "', 'YYYY-MM-DD') ";
            }
            if (!visszaut.isEmpty()) {
                sql += " AND TRUNC(VISSZAUT_DATUM) <= TO_DATE('" + visszaut + "', 'YYYY-MM-DD') ";
            }
            repulojaratList = repulojaratBasicDAO.runSql(new Repulojarat(), sql);

        }
        return new ModelAndView("Repjegy", "Jaratok", repulojaratList);
    }

    @PostMapping(value = "/legnepszerubb_repjegyek")
    public ModelAndView legnepszerubb_repjegyek() throws Exception {
        List<Repulojarat> repulojaratList = new ArrayList<>();
        String sql = "SELECT * FROM ( SELECT jaratszam, indulasi_varos, erkezesi_varos, indulasi_idopont, erkezesi_idopont, ulesek_szama, utasok_szama, admin_id, legitarsasag_kod, (SELECT COUNT(*) FROM Repjegy_foglalas WHERE jaratszam = r.jaratszam) AS foglalasok_szama FROM Repulojarat r ORDER BY foglalasok_szama DESC) WHERE ROWNUM <= 10";
        repulojaratList = repulojaratBasicDAO.runSql(new Repulojarat(), sql);

        return new ModelAndView("Repjegy", "Jaratok", repulojaratList);
    }



    @PostMapping("/repjegyfoglalas/{jaratszam}")
    public String repjegyfoglalas(@PathVariable("jaratszam") String jaratszam, Model model) throws Exception {
        Felhasznalo felhasznalo = Session.getUserBySession(RequestContextHolder.getRequestAttributes().getSessionId());
        if(felhasznalo == null){
            model.addAttribute("Jaratok", null);
            //hibaüzenet ha nem vagyunk bejelentkezve
            model.addAttribute("message", "A foglaláshoz előbb be kell jelentkezni!");
            return  "Repjegy";
        }
        List<Repulojarat> jarat = repulojaratBasicDAO.select(new Repulojarat(), "JARATSZAM", jaratszam);
        model.addAttribute("Jaratok", jarat.get(0));
        model.addAttribute("jaratszam", jaratszam);
        return "Repjegy_foglalas";
    }

    @PostMapping(value = "/foglal_repjegy")
    public String foglal_repjegy(Model model, @RequestParam("jaratszam") String jaratszam, @RequestParam("darab") String darab,
                                 @RequestParam("tipus") String tipus,  @RequestParam("osztaly") String osztaly) throws Exception {
        System.out.println(jaratszam + " " + darab + " " + tipus + " " + osztaly);
        Felhasznalo felhasznalo = Session.getUserBySession(RequestContextHolder.getRequestAttributes().getSessionId());


        if (felhasznalo == null) {
            //hibaüzenet ha nem vagyunk bejelentkezve
            model.addAttribute("message", "A foglaláshoz előbb be kell jelentkezni!");
            return "Szalloda";
        }
        //megnézzük, hogy vannak-e szabad helyek user inputra
        String szabadHelyek = String.format("SELECT COUNT(*) AS szabad_helyek FROM REPULOJARAT " +
                "WHERE jaratszam = '%s' AND ulesek_szama-utasok_szama >= %s", jaratszam, darab);

        ResultSet rs = BasicDAO.SelectSmth(szabadHelyek);
        while (rs.next()){
            if(rs.getInt("SZABAD_HELYEK") == 1){
                Date date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                String updateSQL = String.format("UPDATE REPULOJARAT set UTASOK_SZAMA = UTASOK_SZAMA+%s, ULESEK_SZAMA=ULESEK_SZAMA-%s WHERE JARATSZAM = %s", darab, darab, jaratszam);
                String insertSQL = String.format("INSERT INTO REPJEGY_FOGLALAS (FOGLALASI_IDOPONT, JARATSZAM, FIZETVE, JEGYDARAB, JEGYTIPUS, OSZTALY, FELHASZN_ID) VALUES (TO_DATE( '%s', 'DD/MM/YYYY HH24:MI'), %s, '%s', %s, '%s', '%s', %s)",
                        formatter.format(date), jaratszam, "igen", darab, tipus, osztaly, felhasznalo.felhaszn_id);
                if(BasicDAO.UpdateSmth(insertSQL) && BasicDAO.UpdateSmth(updateSQL)){
                    model = FelhasznaloController.getUserIzek(felhasznalo,model);
                    model.addAttribute("Felhasznalo", felhasznalo);
                    model.addAttribute("message", "Sikeres foglalás!");
                    return "Profile";

                }
            }else{
                model.addAttribute("message", "Nincs elég szabad hely a foglaláshoz!");
                return "Repjegy";
            }
        }
        return "Repjegy";
    }

    @PostMapping(value = "/repjegyfoglalas_torles/{jaratszam}")
    public String repjegyfoglalas_torles(Model model, @PathVariable("jaratszam") String repjegyFoglalasID) throws Exception {
        List<Repjegy_foglalas> repjegyFoglalas = repjegyFoglalasBasicDAO.select(new Repjegy_foglalas(), "REPJEGY_FOGLALAS_ID", repjegyFoglalasID);
        Repjegy_foglalas foglalas = null;
        if(repjegyFoglalas.size() == 1){
            foglalas = repjegyFoglalas.get(0);
            String updateSQL = String.format("UPDATE REPULOJARAT set UTASOK_SZAMA = UTASOK_SZAMA-%s, ULESEK_SZAMA=ULESEK_SZAMA+%s WHERE JARATSZAM = %s", foglalas.jegydarab, foglalas.jegydarab, foglalas.jaratszam);
            if(repjegyFoglalasBasicDAO.delete(foglalas) && BasicDAO.UpdateSmth(updateSQL)){
                //sikeres
            }
        }
        model = FelhasznaloController.getUserIzek(FelhasznaloController.client.getUserObject(), model);
        model.addAttribute("Felhasznalo", FelhasznaloController.client.getUserObject());
        return "Profile";
    }
}

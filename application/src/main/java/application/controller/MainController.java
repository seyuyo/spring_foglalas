package application.controller;

import application.config.Session;
import application.dao.BasicDAO;
import application.model.*;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.ModelAndView;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class MainController {
    private BasicDAO<Szalloda> szallodaBasicDAO = new BasicDAO<Szalloda>();
    private BasicDAO<Biztosito> biztositoBasicDAO = new BasicDAO<Biztosito>();
    private BasicDAO<Repulojarat> repulojaratBasicDAO = new BasicDAO<Repulojarat>();
    private BasicDAO<Nepszeru_szalloda> nepszeruSzallodaBasicDAO = new BasicDAO<>();

    @GetMapping("/login")
    public String login(Model model) throws Exception {
        if (Session.isSessionLogged(RequestContextHolder.currentRequestAttributes().getSessionId())){
            model = FelhasznaloController.getUserIzek(Session.getUserBySession(RequestContextHolder.getRequestAttributes().getSessionId()), model);
            model.addAttribute("Felhasznalo", Session.getUserBySession(RequestContextHolder.getRequestAttributes().getSessionId()));
            return "Profile";
        }
        return "Login";
    }
    @GetMapping("/repjegy")
    public ModelAndView repjegy() {
        return new ModelAndView("Repjegy", "success", true);
    }
    @GetMapping("/szalloda")
    public String szalloda(Model model) throws Exception {
        String sql = "SELECT (SELECT nev FROM szalloda WHERE szalloda_id = szalloda_foglalas.szalloda_id) AS szalloda_nev, COUNT(*) AS foglalasok_szama FROM szalloda_foglalas GROUP BY szalloda_id ORDER BY foglalasok_szama DESC FETCH FIRST 5 ROW ONLY";
        ResultSet resultSet = BasicDAO.SelectSmth(sql);

        String sql2 = "SELECT szalloda.nev, szalloda.varos FROM szalloda, szalloda_foglalas WHERE szalloda.ar > (SELECT AVG(ar) FROM szalloda) AND szalloda.szalloda_id = szalloda_foglalas.szalloda_id GROUP BY szalloda.nev, szalloda.varos";
        ResultSet resultSet2 = BasicDAO.SelectSmth(sql2);

        List<Nepszeru_szalloda> nepszeruSzallodak = new ArrayList<>();
        while(resultSet.next()) {
            Nepszeru_szalloda szalloda = new Nepszeru_szalloda();
            szalloda.szalloda_nev = resultSet.getString("SZALLODA_NEV");
            szalloda.foglalasok_szama = resultSet.getInt("FOGLALASOK_SZAMA");
            nepszeruSzallodak.add(szalloda);
        }

        List<Szalloda> dragaszallodak = new ArrayList<>();
        while(resultSet2.next()) {
            Szalloda szalloda2 = new Szalloda();
            szalloda2.nev = resultSet2.getString("NEV");
            szalloda2.varos = resultSet2.getString("VAROS");
            dragaszallodak.add(szalloda2);
        }
        List<String> kepekListaja = Arrays.asList("1.jpg", "2.jpg", "3.jpg,", "4.jpg", "5.jpg",
                "6.jpg", "7.jpg", "8.jpg", "9.jpg", "10.jpg", "11.jpg", "12.jpg",
                "13.jpg", "14.jpg", "15.jpg", "16.jpg, 17.jpg", "18.jpg", "19.jpg", "20.jpg", "21,jpg");

        List<Szalloda> szallodak = szallodaBasicDAO.selectAll(new Szalloda());
        model.addAttribute("Szallodak", szallodak);
        model.addAttribute("Nepszeru_szallodak", nepszeruSzallodak);
        model.addAttribute("kepekListaja", kepekListaja);
        model.addAttribute("Szallodak", szallodak);
        model.addAttribute("Dragaszallodak", dragaszallodak);
        return"Szalloda";
    }


    @GetMapping("/biztositas")
    public String biztositas(Model model) throws Exception {
        String sql = "SELECT b.nev AS biztosito_neve, c.fajtak, c.ar FROM biztosito b, biztositasi_csomag c WHERE b.biztosito_kod = c.biztosito_kod AND ( SELECT COUNT(*) FROM biztositasi_csomag c2 WHERE c2.fajtak = c.fajtak AND c2.ar >= c.ar ) <= 5 ORDER bY c.fajtak, c.ar DESC";
        ResultSet resultSet = BasicDAO.SelectSmth(sql);
        List<Nepszeru_biztosito> nepszeruBiztositok = new ArrayList<>();
        while(resultSet.next()) {
            Nepszeru_biztosito nepszeruBiztosito = new Nepszeru_biztosito();
            nepszeruBiztosito.biztosito_neve = resultSet.getString("BIZTOSITO_NEVE");
            nepszeruBiztosito.fajtak = resultSet.getString("FAJTAK");
            nepszeruBiztosito.ar = resultSet.getInt("AR");
            nepszeruBiztositok.add(nepszeruBiztosito);
        }
        List<Biztosito> biztositok = biztositoBasicDAO.selectAll(new Biztosito());
        model.addAttribute("Nepszeru_Biztositok", nepszeruBiztositok);
        model.addAttribute("Biztositok", biztositok);
        model.addAttribute("Csomag", null);
        return "Biztositas";
    }

    @GetMapping("/menetrend")
    public ModelAndView menetrend() throws Exception {
        List<Repulojarat> repulojaratList = repulojaratBasicDAO.selectAll(new Repulojarat());
        return new ModelAndView("Menetrend", "Jaratok", repulojaratList);
    }
    @GetMapping("/")
    public String indexPage(Model model) throws SQLException {
        String sql = "SELECT legitarsasag.nev FROM legitarsasag, repulojarat WHERE legitarsasag.legitarsasag_kod = repulojarat.legitarsasag_kod AND NOT EXISTS ( SELECT * FROM repjegy_foglalas WHERE repjegy_foglalas.jaratszam = repulojarat.jaratszam ) GROUP BY legitarsasag.nev";
        ResultSet resultSet = BasicDAO.SelectSmth(sql);
        List<String> nemnepszerulegitarsasagok = new ArrayList<>();
        while(resultSet.next()) {
            nemnepszerulegitarsasagok.add(resultSet.getString("NEV"));
        }

        model.addAttribute("nemnepszerulegitarsasagok", nemnepszerulegitarsasagok);
        return "index";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "Login";
    }

}

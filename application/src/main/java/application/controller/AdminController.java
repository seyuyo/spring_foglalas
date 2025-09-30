package application.controller;

import application.config.Session;
import application.dao.BasicDAO;
import application.model.*;
import com.fasterxml.jackson.core.Base64Variant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AdminController {
    private static BasicDAO<Felhasznalo> felhasznaloBasicDAO = new BasicDAO<>();
    private static BasicDAO<Szalloda> szallodaBasicDAO = new BasicDAO<>();
    private static BasicDAO<Repulojarat> repulojaratBasicDAO = new BasicDAO();
    private static BasicDAO<Admin> adminBasicDAO = new BasicDAO<>();
    private static BasicDAO<Legitarsasag> legitarsasagBasicDAO = new BasicDAO<>();
    private static BasicDAO<Biztosito> biztositoBasicDAO = new BasicDAO<>();
    private static BasicDAO<Biztositasi_csomag> biztositasiCsomagBasicDAO = new BasicDAO<>();
    private static BasicDAO<Arajanlat> arajanlatBasicDAO = new BasicDAO<>();
    public static Model adminPageData(Model model, String email, String password) throws Exception {
        List<Admin> adminList = adminBasicDAO.select(new Admin(), "E_MAIL", email);
        if(adminList.size() == 1){
            Admin admin = adminList.get(0);
            if(admin.jelszo.equals(password)){
                FelhasznaloController.client = new Client();
                FelhasznaloController.client.setLoggedIn(true);
                FelhasznaloController.client.setSessionId(RequestContextHolder.currentRequestAttributes().getSessionId());
                FelhasznaloController.client.setAdminid(admin.admin_id);
                model = getAdminsIzek(model);
            }else{
                model = null;
            }
        }else{
            model = null;
        }
        return model;
    }

    @PostMapping(value = "/repulojarat_szerkesztes/{jaratszam}")
    public String repulojarat_szerkesztes(Model model, @PathVariable("jaratszam") String jaratszam) throws Exception {
        model.addAttribute("Repulojarat", repulojaratBasicDAO.select(new Repulojarat(), "JARATSZAM", jaratszam).get(0));
        model = getAdminsIzek(model);
        return "Admin";
    }
    @PostMapping(value = "/repulojarat_torles/{jaratszam}")
    public String repulojarat_torles(Model model, @PathVariable("jaratszam") String jaratszam) throws Exception {
        repulojaratBasicDAO.delete(repulojaratBasicDAO.select(new Repulojarat(), "JARATSZAM", jaratszam).get(0));
        model.addAttribute("Jaratok", repulojaratBasicDAO.selectAll(new Repulojarat()));
        model.addAttribute("Szallodak", szallodaBasicDAO.selectAll(new Szalloda()));
        return "Admin";
    }
    @PostMapping(value = "/szalloda_szerkesztes/{szalloda_id}")
    public String szalloda_szerkesztes(Model model, @PathVariable("szalloda_id") String szalloda_id) throws Exception {
        model.addAttribute("Szalloda", szallodaBasicDAO.select(new Szalloda(), "SZALLODA_ID", szalloda_id).get(0));
        model = getAdminsIzek(model);
        return "Admin";
    }
    @PostMapping(value = "/szalloda_torles/{szalloda_id}")
    public String szalloda_torles(Model model, @PathVariable("szalloda_id") String szalloda_id) throws Exception {
        szallodaBasicDAO.delete(szallodaBasicDAO.select(new Szalloda(), "SZALLODA_ID", szalloda_id).get(0));
        model = getAdminsIzek(model);
        return "Admin";
    }

    @PostMapping("/repulojarat_hozzaadas")
    public String repulojarat_hozzaadas(Model model,
                                        @RequestParam("indulas") String indulas,
                                        @RequestParam("erkezes") String erkezes,
                                        @RequestParam("indulasido") String indulasiido,
                                        @RequestParam("erkezesido") String erkezesiido,
                                        @RequestParam("ulesek") String ulesek,
                                        @RequestParam("legitarsasagkod") String legitarsasagkod,
                                        @RequestParam("jaratszam") String jaratszam)throws Exception {
        if(jaratszam != ""){
            String update_sql = String.format("UPDATE REPULOJARAT SET INDULASI_VAROS='%s', ERKEZESI_VAROS='%s', INDULASI_IDOPONT = TO_DATE( '%s', 'DD/MM/YYYY HH24:MI'), " +
                            "ERKEZESI_IDOPONT = TO_DATE( '%s', 'DD/MM/YYYY HH24:MI'), ULESEK_SZAMA = %s, LEGITARSASAG_KOD = %s WHERE JARATSZAM=%s",
                    indulas, erkezes, datum_sqlbe(indulasiido), datum_sqlbe(erkezesiido), ulesek, legitarsasagkod, jaratszam);
            BasicDAO.UpdateSmth(update_sql);
            model.addAttribute("Repulojarat", null);
        }else{
            String insert_sql = String.format("INSERT INTO REPULOJARAT (INDULASI_VAROS, ERKEZESI_VAROS, INDULASI_IDOPONT, ERKEZESI_IDOPONT, ULESEK_SZAMA, UTASOK_SZAMA, ADMIN_ID, LEGITARSASAG_KOD) VALUES " +
                            "('%s','%s',TO_DATE( '%s', 'DD/MM/YYYY HH24:MI'),TO_DATE( '%s', 'DD/MM/YYYY HH24:MI'),%s,%s,%s,%s)",
                    indulas, erkezes, datum_sqlbe(indulasiido), datum_sqlbe(erkezesiido), ulesek, 0, FelhasznaloController.client.getAdminid(), legitarsasagkod);
            BasicDAO.UpdateSmth(insert_sql);
        }
        model = getAdminsIzek(model);
        return "Admin";
    }
    @PostMapping("/szalloda_hozzaadas")
    public String szalloda_hozzaadas(Model model,
                                     @RequestParam("szallodanev") String szallodanev,
                                     @RequestParam("varos") String varos,
                                     @RequestParam("cim") String cim,
                                     @RequestParam("szobafero") String szobafero,
                                     @RequestParam("ar") String ar,
                                     @RequestParam("szalloda_id") String szalloda_id) throws Exception {
        if(szalloda_id != ""){
            Szalloda szalloda = new Szalloda(Integer.parseInt(szalloda_id), szallodanev, cim, varos, Integer.parseInt(szobafero), Integer.parseInt(ar));
            ArrayList<String> skipList = new ArrayList<>();
            skipList.add("ADMIN_ID");
            szallodaBasicDAO.update(szalloda,skipList);
            model.addAttribute("Szalloda", null);
        }else{
            Szalloda szalloda = new Szalloda();
            szalloda.szoba_ferohelyek_szama = Integer.parseInt(szobafero);
            szalloda.ar = Integer.parseInt(ar);
            szalloda.admin_id = FelhasznaloController.client.getAdminid();
            szalloda.cim = cim;
            szalloda.nev = szallodanev;
            szalloda.varos = varos;
            ArrayList<String> skipList = new ArrayList<>();
            skipList.add("SZALLODA_ID");
            szallodaBasicDAO.insert(szalloda, skipList);
        }
        model = getAdminsIzek(model);
        return "Admin";
    }
    @PostMapping("/legitarsasag_hozzaadas")
    public String legitarsasag_hozzaadas(Model model,
                                     @RequestParam("nev") String nev,
                                     @RequestParam("orszag") String orszag,
                                     @RequestParam("flotta") String flotta,
                                         @RequestParam("legitarsasag_kod") String legitarsasag_kod
                                     ) throws Exception {
        if(legitarsasag_kod != ""){
            Legitarsasag legitarsasag = new Legitarsasag();
            legitarsasag.legitarsasag_kod = Integer.parseInt(legitarsasag_kod);
            legitarsasag.nev = nev;
            legitarsasag.flotta = flotta;
            legitarsasag.orszag = orszag;
            ArrayList<String> skipList = new ArrayList<>();
            skipList.add("ADMIN_ID");
            legitarsasagBasicDAO.update(legitarsasag, skipList);
            model.addAttribute("Legitarsasag", null);
        }else {
            Legitarsasag legitarsasag = new Legitarsasag();
            legitarsasag.nev = nev;
            legitarsasag.flotta = flotta;
            legitarsasag.orszag = orszag;
            legitarsasag.admin_id = FelhasznaloController.client.getAdminid();
            ArrayList<String> skipList = new ArrayList<>();
            skipList.add("LEGITARSASAG_KOD");
            legitarsasagBasicDAO.insert(legitarsasag, skipList);
        }

        model = getAdminsIzek(model);
        return "Admin";
    }
    @PostMapping(value = "/legitarsasag_torles/{legitarsasag_kod}")
    public String legitarsasag_torles(Model model, @PathVariable("legitarsasag_kod") String legitarsasag_kod) throws Exception {
        legitarsasagBasicDAO.delete(repulojaratBasicDAO.select(new Legitarsasag(), "LEGITARSASAG_KOD", legitarsasag_kod).get(0));
        model = getAdminsIzek(model);
        return "Admin";
    }
    @PostMapping(value = "/legitarsasag_szerkesztes/{legitarsasag_kod}")
    public String legitarsasag_szerkesztes(Model model, @PathVariable("legitarsasag_kod") String legitarsasag_kod) throws Exception {
        model.addAttribute("Legitarsasag", legitarsasagBasicDAO.select(new Legitarsasag(), "LEGITARSASAG_KOD", legitarsasag_kod).get(0));
        model = getAdminsIzek(model);
        return "Admin";
    }

    @PostMapping("/biztosito_hozzaadas")
    public String biztosito_hozzaadas(Model model,
                                     @RequestParam("nev1") String nev1,
                                     @RequestParam("leiras") String leiras,
                                      @RequestParam("biztosito_kod") String biztosito_kod
                                    ) throws Exception {
        if(biztosito_kod != ""){
            Biztosito biztosito = new Biztosito();
            biztosito.biztosito_kod = Integer.parseInt(biztosito_kod);
            biztosito.nev = nev1;
            biztosito.leiras = leiras;
            ArrayList<String> skipList = new ArrayList<>();
            biztositoBasicDAO.update(biztosito, skipList);
            model.addAttribute("Biztosito", null);
        }else{
            Biztosito biztosito = new Biztosito();
            biztosito.nev = nev1;
            biztosito.leiras = leiras;
            ArrayList<String> skipList = new ArrayList<>();
            skipList.add("BIZTOSITO_KOD");
            biztositoBasicDAO.insert(biztosito, skipList);
        }
        model = getAdminsIzek(model);
        return "Admin";
    }

    @PostMapping(value = "/biztosito_torles/{biztosito_kod}")
    public String biztosito_torles(Model model, @PathVariable("biztosito_kod") String biztosito_kod) throws Exception {
        biztositoBasicDAO.delete(biztositoBasicDAO.select(new Biztosito(), "BIZTOSITO_KOD", biztosito_kod).get(0));
        model = getAdminsIzek(model);
        return "Admin";
    }
    @PostMapping(value = "/biztosito_szerkesztes/{biztosito_kod}")
    public String biztosito_szerkesztes(Model model, @PathVariable("biztosito_kod") String biztosito_kod) throws Exception {
        model.addAttribute("Biztosito", biztositoBasicDAO.select(new Biztosito(), "BIZTOSITO_KOD", biztosito_kod).get(0));
        model = getAdminsIzek(model);
        return "Admin";
    }

    @PostMapping("/biztosito_csomag_hozzaadas")
    public String biztosito_csomag_hozzaadas(Model model,
                                      @RequestParam("fajtak") String fajtak,
                                      @RequestParam("ar2") String ar2,
                                             @RequestParam("biztkod2") String biztkod2,
                                                @RequestParam("csomag_id") String csomag_id
    ) throws Exception {
        if(csomag_id != ""){
            Biztositasi_csomag biztositasiCsomag = new Biztositasi_csomag();
            biztositasiCsomag.csomag_id = Integer.parseInt(csomag_id);
            biztositasiCsomag.biztosito_kod = Integer.parseInt(biztkod2);
            biztositasiCsomag.fajtak = fajtak;
            biztositasiCsomag.ar = Integer.parseInt(ar2);
            ArrayList<String> skipList = new ArrayList<>();
            biztositasiCsomagBasicDAO.update(biztositasiCsomag, skipList);
            model.addAttribute("Csomag", null);
        }else{
            Biztositasi_csomag biztositasiCsomag = new Biztositasi_csomag();
            biztositasiCsomag.biztosito_kod = Integer.parseInt(biztkod2);
            biztositasiCsomag.fajtak = fajtak;
            biztositasiCsomag.ar = Integer.parseInt(ar2);
            ArrayList<String> skipList = new ArrayList<>();
            skipList.add("CSOMAG_ID");
            biztositasiCsomagBasicDAO.insert(biztositasiCsomag, skipList);
        }

        model = getAdminsIzek(model);
        return "Admin";
    }

    @PostMapping(value = "/csomag_torles/{csomag_id}")
    public String csomag_torles(Model model, @PathVariable("csomag_id") String csomag_id) throws Exception {
        biztositasiCsomagBasicDAO.delete(biztositasiCsomagBasicDAO.select(new Biztositasi_csomag(), "CSOMAG_ID", csomag_id).get(0));
        model = getAdminsIzek(model);
        return "Admin";
    }
    @PostMapping(value = "/felhasznalo_torles/{felhaszn_id}")
    public String felhasznalo_torles(Model model, @PathVariable("felhaszn_id") String felhaszn_id) throws Exception {
        felhasznaloBasicDAO.delete(felhasznaloBasicDAO.select(new Felhasznalo(), "FELHASZN_ID", felhaszn_id).get(0));
        model = getAdminsIzek(model);
        return "Admin";
    }


    @PostMapping(value = "/csomag_szerkesztes/{csomag_id}")
    public String csomag_szerkesztes(Model model, @PathVariable("csomag_id") String csomag_id) throws Exception {
        model.addAttribute("Csomag", biztositasiCsomagBasicDAO.select(new Biztositasi_csomag(), "CSOMAG_ID", csomag_id).get(0));
        model = getAdminsIzek(model);
        return "Admin";
    }
    @PostMapping("/arajanlat_hozzaadas")
    public String biztosito_hozzaadas(Model model,
                                      @RequestParam("jaratszam2") String jaratszam2,
                                      @RequestParam("arajanlat_id") String arajanlat_id,
                                      @RequestParam("ar3") String ar3,
                                      @RequestParam("kedvezm") String kedvezm
    ) throws Exception {
        if(arajanlat_id != ""){
            Arajanlat arajanlat = new Arajanlat();
            arajanlat.ar = Integer.parseInt(ar3);
            arajanlat.kedvezmeny = Integer.parseInt(kedvezm);
            arajanlat.jaratszam = Integer.parseInt(jaratszam2);
            arajanlat.arajanlat_id = Integer.parseInt(arajanlat_id);
            ArrayList<String> skipList = new ArrayList<>();
            arajanlatBasicDAO.update(arajanlat, skipList);
            model.addAttribute("Arajanlat", null);
        }else{
            Arajanlat arajanlat = new Arajanlat();
            arajanlat.ar = Integer.parseInt(ar3);
            arajanlat.kedvezmeny = Integer.parseInt(kedvezm);
            arajanlat.jaratszam = Integer.parseInt(jaratszam2);
            ArrayList<String> skipList = new ArrayList<>();
            skipList.add("ARAJANLAT_ID");
            arajanlatBasicDAO.insert(arajanlat, skipList);
        }
        model = getAdminsIzek(model);
        return "Admin";
    }

    @PostMapping(value = "/arajanlat_torles/{arajanlat_id}")
    public String arajanlat_torles(Model model, @PathVariable("arajanlat_id") String arajanlat_id) throws Exception {
        arajanlatBasicDAO.delete(arajanlatBasicDAO.select(new Arajanlat(), "ARAJANLAT_ID", arajanlat_id).get(0));
        model = getAdminsIzek(model);
        return "Admin";
    }
    @PostMapping(value = "/arajanlat_szerkesztes/{arajanlat_id}")
    public String arajanlat_szerkesztes(Model model, @PathVariable("arajanlat_id") String arajanlat_id) throws Exception {
        model.addAttribute("Arajanlat", arajanlatBasicDAO.select(new Arajanlat(), "ARAJANLAT_ID", arajanlat_id).get(0));
        model = getAdminsIzek(model);
        return "Admin";
    }


    public String datum_sqlbe(String datum){
        String ev = datum.substring(0,4);
        String ho = datum.substring(5,7);
        String nap = datum.substring(8,10);
        String ora = datum.substring(11, 13);
        String perc = datum.substring(14,16);
        return nap + "/" + ho + "/" + ev + " " + ora + ":" + perc;
    }

    /**
    * Egy listát állít össze azokról a felhasználókról, akiknek van legalább egy szálloda foglalása
    * és legalább egy biztosítási csomagjuk.
    * */
    public static Model getAdminsIzek(Model model) throws Exception {
        model.addAttribute("Jaratok", repulojaratBasicDAO.selectAll(new Repulojarat()));
        model.addAttribute("Szallodak", szallodaBasicDAO.selectAll(new Szalloda()));
        model.addAttribute("Legitarsasagok", legitarsasagBasicDAO.selectAll(new Legitarsasag()));
        model.addAttribute("Felhasznalok", felhasznaloBasicDAO.selectAll(new Felhasznalo()));
        model.addAttribute("Biztositok", biztositoBasicDAO.selectAll(new Biztosito()));
        model.addAttribute("Csomagok", biztositasiCsomagBasicDAO.selectAll(new Biztositasi_csomag()));
        model.addAttribute("Arajanlatok", arajanlatBasicDAO.selectAll(new Arajanlat()));
        String sql = "SELECT felhasznalo.nev, felhasznalo.e_mail FROM felhasznalo, repjegy_foglalas, biztositast_kot," +
                " szalloda_foglalas WHERE felhasznalo.felhaszn_id = repjegy_foglalas.felhaszn_id " +
                "AND felhasznalo.felhaszn_id = biztositast_kot.felhaszn_id  AND " +
                "felhasznalo.felhaszn_id = szalloda_foglalas.felhaszn_id  GROUP BY felhasznalo.nev, felhasznalo.e_mail " +
                "HAVING COUNT(DISTINCT szalloda_foglalas.szalloda_foglalas_id) > 0 AND " +
                "COUNT(DISTINCT biztositast_kot.biztositasi_csomag_kod) > 0 ";
        ResultSet resultSet = BasicDAO.SelectSmth(sql);
        List<Aktiv_felhasznalo> aktivFelhasznalok = new ArrayList<>();
        while(resultSet.next()) {
           Aktiv_felhasznalo aktivFelhasznalo = new Aktiv_felhasznalo();
           aktivFelhasznalo.e_mail = resultSet.getString("E_MAIL");
           aktivFelhasznalo.nev = resultSet.getString("NEV");
           aktivFelhasznalok.add(aktivFelhasznalo);
        }
        model.addAttribute("aktivFelhasznalok", aktivFelhasznalok);
        return model;
    }
}

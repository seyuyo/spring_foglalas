package application.controller;

import application.config.Session;
import application.dao.BasicDAO;
import application.model.Biztositasi_csomag;
import application.model.Biztositast_kot;
import application.model.Biztosito;
import application.model.Felhasznalo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.ArrayList;
import java.util.List;

@Controller
public class BiztositasController {
    private BasicDAO<Biztositasi_csomag> biztositasiCsomagBasicDAO = new BasicDAO<Biztositasi_csomag>();
    private BasicDAO<Biztosito> biztositoBasicDAO = new BasicDAO<Biztosito>();
    private BasicDAO<Biztositast_kot> biztositastKotBasicDAO = new BasicDAO<Biztositast_kot>();
    @PostMapping(value = "/bovebben/{bid}")
    public String filmDelete(@PathVariable("bid") String bid, Model model) throws Exception {
        List<Biztosito> biztositok = biztositoBasicDAO.select(new Biztosito(), "BIZTOSITO_KOD", bid);
        model.addAttribute("Biztositok", biztositok);
        List<Biztositasi_csomag> biztositasiCsomagList = biztositasiCsomagBasicDAO.select(new Biztositasi_csomag(), "BIZTOSITO_KOD", bid);
        model.addAttribute("Csomagok", biztositasiCsomagList);
        return "Biztositas_kotes";
    }

    @PostMapping(value = "/biztositast_kot")
    public String biztositast_kot(Model model, @RequestParam("csomag_id") String csomag_id) throws Exception {
        System.out.println(csomag_id);
        Felhasznalo felhasznalo = FelhasznaloController.client.getUserObject();
        if(felhasznalo != null){
            Biztositast_kot biztositastKot = new Biztositast_kot(felhasznalo.felhaszn_id, Integer.parseInt(csomag_id));
            List<String> skipList = new ArrayList<>();
            if(biztositastKotBasicDAO.insert(biztositastKot, skipList));{
                model = FelhasznaloController.getUserIzek(felhasznalo,model);
                model.addAttribute("Felhasznalo", felhasznalo);
                return "Profile";
                //sikeres foglalas
            }

        }

        return "Login";
    }
}

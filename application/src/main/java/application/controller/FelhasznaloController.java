package application.controller;

import application.config.Session;
import application.dao.BasicDAO;
import application.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import java.security.MessageDigest;
import java.sql.CallableStatement;
import java.util.ArrayList;
import java.util.List;

@Controller
public class FelhasznaloController {
    public static Client client;
    private BasicDAO<Felhasznalo> felhasznaloDao = new BasicDAO<Felhasznalo>();
    private static BasicDAO<RepjegyFoglalasForUser> repjegyFoglalasForUserBasicDAO = new BasicDAO<RepjegyFoglalasForUser>();
    private static BasicDAO<SzallodaFoglalasForUser> szallodaFoglalasForUserBasicDAO = new BasicDAO<SzallodaFoglalasForUser>();

    @PostConstruct
    public void init() {
        client = new Client();
    }

    @PostMapping(value = "/loginUser")
    public String loginUser(@RequestParam("email") String email,
                                  @RequestParam("password") String password, Model model) throws Exception {
        List<Felhasznalo> users = felhasznaloDao.select(new Felhasznalo(),"E_MAIL", email);
        if(users.size() ==1){
            Felhasznalo user = users.get(0);
            if(user.jelszo.equals(password)){
                client = new Client();

                client.setUserObject((Felhasznalo) user.clone());
                client.setLoggedIn(true);
                client.setSessionId(RequestContextHolder.currentRequestAttributes().getSessionId());
                client.setAdminid(-1);

                Session.users.add(client);
                model = getUserIzek(user,model);
                model.addAttribute("Felhasznalo", user);
                return "Profile";
            }
        }
        if(users.size() == 0){
            model = AdminController.adminPageData(model,email,password);
            if(model != null){
                return "Admin";
            }
        }
        return "Login";
    }
    @PostMapping(value = "/registerUser")
    public String registerUser(@RequestParam("name") String name, @RequestParam("email") String email, @RequestParam("mobile-phone") String mobil_phone,
                            @RequestParam("password") String password, @RequestParam("password2") String password2, Model model) throws Exception {
        List<Felhasznalo> users = felhasznaloDao.select(new Felhasznalo(),"E_MAIL", email);
        if(users.size() == 0){
            Felhasznalo user = new Felhasznalo();
            if(password.equals(password2)){
                user.jelszo = password;
                user.e_mail = email;
                user.telefonszam = mobil_phone;
                user.admin_id = 1;
                user.nev = name;
                List<String> skip = new ArrayList<>();
                skip.add("ADMIN_ID");
                if(felhasznaloDao.insert(user,skip)){
                    client = new Client();

                    client.setUserObject((Felhasznalo) user.clone());
                    client.setLoggedIn(true);
                    client.setSessionId(RequestContextHolder.currentRequestAttributes().getSessionId());
                    client.setAdminid(-1);

                    Session.users.add(client);
                    model = getUserIzek(user,model);
                    model.addAttribute("Felhasznalo", user);
                    return "Profile";
                }else {
                    //sikertelen regisztráció
                }
            }else{
                //jelszavak nem egyeznek
            }

        }
        //felhasznalo mar létezik
        return "Login";
    }
    @GetMapping("/logout")
    public String logout() {
        Session.logOutSession(RequestContextHolder.currentRequestAttributes().getSessionId());
        client = null;
        return "Index";
    }


    public static Model getUserIzek(Felhasznalo user, Model model) throws Exception {
        String sql = "begin ? := RepFogFelhSzer(?); end;";
        String sql2 = "begin ? := SzallFogFelhSzer(?); end;";
        String sql3 = "{ ? = call foglalasok_es_bevetel }";
        List<RepjegyFoglalasForUser> repjegyFoglalasForUserList = repjegyFoglalasForUserBasicDAO.SelectSpecificObject(new RepjegyFoglalasForUser(),sql, user.felhaszn_id);
        List<SzallodaFoglalasForUser> szallodaFoglalasForUserList = szallodaFoglalasForUserBasicDAO.SelectSpecificObject(new SzallodaFoglalasForUser(), sql2, user.felhaszn_id);
        model.addAttribute("Repjegyek", repjegyFoglalasForUserList);
        model.addAttribute("Szallodak", szallodaFoglalasForUserList);
        return model;
    }

    @GetMapping("/profil-szerkesztes")
    public String profilSzerkesztes() {
        return "Profil_szerkesztes";
    }
}

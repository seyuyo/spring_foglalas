package application.config;

import application.model.Client;
import application.model.Felhasznalo;

import java.util.ArrayList;
import java.util.List;

public class Session {
    public static List<Client> users = new ArrayList<Client>();

    public static boolean isSessionLogged(String session) {
        for(Client client : users) {
            if(client.getSessionId().equals(session) && client.isLoggedIn())
                return true;
        }
        return false;
    }

    public static boolean isSessionAdmin(String session) {
        for(Client client : users) {
            if(client.getSessionId().equals(session) && client.getAdminid() != -1)
                return true;
        }
        return false;
    }

    public static Felhasznalo getUserBySession(String session) {
        for(Client client : users) {
            if(client.getSessionId().equals(session))
                return client.getUserObject();
        }
        return null;
    }

    public static Client getClientBySession(String session) {
        for(Client client : users) {
            if(client.getSessionId().equals(session))
                return client;
        }
        return null;
    }

    public static void logOutSession(String session) {
        for(int i = 0; i < users.size(); i++) {
            if(users.get(i).getSessionId().equals(session)) {
                users.remove(users.get(i));
                return;
            }
        }
    }
}

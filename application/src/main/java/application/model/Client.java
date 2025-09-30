package application.model;

public class Client {
    private String sessionId;
    private boolean loggedIn;
    private Felhasznalo userObject;
    private Integer adminid;

    public Integer getAdminid() {
        return adminid;
    }

    public void setAdminid(Integer adminid) {
        this.adminid = adminid;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public Felhasznalo getUserObject() {
        if(userObject == null){
            return null;
        }
        return userObject;
    }

    public void setUserObject(Felhasznalo userObject) {
        this.userObject = userObject;
    }
}



package org.pahappa.systems.registrationapp.views;

import org.mindrot.jbcrypt.BCrypt;
import org.pahappa.systems.registrationapp.models.User;
import org.pahappa.systems.registrationapp.services.UserService;

import javax.annotation.ManagedBean;
import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.Objects;

@ManagedBean
@SessionScoped
public class LoginBean implements Serializable {

    private String username;
    private String password;
    private static User currentUser;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        LoginBean.currentUser = currentUser;
    }


    public static void redirect(String location) {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/user_registration_main_war" + location);
        } catch (Exception ignored) {
        }
    }

    public void authenticatedOnly() {
        if (currentUser == null) redirect("/pages/login/login.xhtml?faces-redirect=true");
    }

    public String login() {
        UserService userService = new UserService();

        if (userService.checkUsername(username)) {
            User user = userService.retrieveUser(username);
            String storedPassword = user.getPassword();
            String role = user.getRole();
            boolean passwordMatch = BCrypt.checkpw(password, storedPassword);

            if ("ADMIN".equals(role) && passwordMatch) {
                setCurrentUser(user);
                return "/pages/dashboard/dashboard.xhtml?faces-redirect=true"; // Successful login

            } else if ("USER".equals(role) && passwordMatch){
                setCurrentUser(user);
                return "/pages/user/userHomePage?faces-redirect=true"; //
            } else {
                // Add error message to the faces context
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid Credentials!", "Please try again."));
                return null; // Stay on the login page
            }

        } else {
            //User not found
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "User not Found!", "Please try again."));
            return null; // Stay on the login page
        }
    }

    public String logout() {
        setCurrentUser(null);
        return "/pages/login/login.xhtml?faces-redirect=true";
    }
}

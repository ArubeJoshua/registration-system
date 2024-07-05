package org.pahappa.systems.registrationapp.views;

import org.pahappa.systems.registrationapp.dao.UserDAO;
import org.pahappa.systems.registrationapp.models.User;
import org.pahappa.systems.registrationapp.services.UserService;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.util.Date;


@ManagedBean
@RequestScoped
public class AdminBean {
    private User admin;
    private final UserDAO userDAO = new UserDAO();
    private final UserService userService = new UserService();

    @PostConstruct
    public void init() {
        admin = userDAO.getAdminUser();
    }

    public User getAdmin() {
        return admin;
    }

    public void setAdmin(User admin) {
        this.admin = admin;
    }

    public UserDAO getUserDAO() {
        return userDAO;
    }

    public String updateAdmin() {
        String newPassword = admin.getPassword();
        String hashedPassword = userService.hashPassword(newPassword);
        admin.setPassword(hashedPassword);
        userDAO.update(admin, admin.getUsername());
        return "/pages/admin/updateAdmin.xhtml?faces-redirect=true";
    }
}

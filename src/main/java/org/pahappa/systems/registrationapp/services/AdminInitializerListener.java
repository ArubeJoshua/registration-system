package org.pahappa.systems.registrationapp.services;

import org.pahappa.systems.registrationapp.dao.UserDAO;
import org.pahappa.systems.registrationapp.models.User;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class AdminInitializerListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        UserDAO userDAO = new UserDAO();
        UserService userService = new UserService();
        String adminUserName = userDAO.getAdminUsername();

        if (adminUserName == null) {
            // Create a new admin user
            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setPassword(userService.hashPassword("admin123")); // Hash the password before storing
            adminUser.setRole("ADMIN");
            adminUser.setFirstname("unknown");
            adminUser.setLastname("unknown");
            adminUser.setDateOfBirth(userService.parseDate("01-01-1970"));
            adminUser.setEmail("admin@example.com");
            userDAO.save(adminUser);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Cleanup logic if needed
    }
}

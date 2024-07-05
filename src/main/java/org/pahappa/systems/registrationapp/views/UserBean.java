package org.pahappa.systems.registrationapp.views;

import org.pahappa.systems.registrationapp.dao.UserDAO;
import org.pahappa.systems.registrationapp.models.Dependant;
import org.pahappa.systems.registrationapp.models.User;
import org.pahappa.systems.registrationapp.services.UserService;
import org.primefaces.component.piechart.PieChart;
import org.primefaces.model.charts.pie.PieChartModel;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@ManagedBean
@ViewScoped
public class UserBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private UserService userService = new UserService();
    private String search;
    private Set<User> users;
    private User selectedUser; // This is the user selected for updating
    private Set<User> filteredUsers;
    UserDAO userDAO =new UserDAO();
    private PieChartModel pieModel;

    @PostConstruct
    public void init() {
        users = filteredUsers = userService.retrieveUsersList();
        selectedUser = new User(); // Initialize with a new User object

    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public User getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(User selectedUser) {
        this.selectedUser = selectedUser;
    }

    public void selectUserForUpdate(User user) {
        this.selectedUser = user;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
        searchUsers();
    }

    public Set<User> getUsers() {
        return userService.retrieveUsersList();
    }

    public void update(User user) {
        userService.updateUser(user);
        addMessage("Success", "Record Updated");

    }

    public void delete(User user) {
        userService.deleteUser(user.getUsername());
        addMessage("Confirmed", "Record deleted");

    }

    public void addMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public String manageDependants(User user) {

        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext external = context.getExternalContext();
        external.getSessionMap().put("user", user);

        return "/pages/dependants/userDependants.xhtml?faces-redirect=true";

    }

    public Set<User> getFilteredUsers() {
        return filteredUsers;
    }

    public void setFilteredUsers(Set<User> filteredUsers) {
        this.filteredUsers = filteredUsers;
    }

    public void deleteAll() {
        userService.deleteAllUsers();
        addMessage("Confirmed", "Records deleted");
    }

    public void searchUsers() {
        if (search == null || search.isEmpty()) {
            filteredUsers = users;
        } else {
            filteredUsers = users.stream()
                    .filter(user -> user.getUsername().toLowerCase().contains(search.toLowerCase()) ||
                            user.getFirstname().toLowerCase().contains(search.toLowerCase()) ||
                            user.getLastname().toLowerCase().contains(search.toLowerCase()))
                    .collect(Collectors.toSet());
        }

    }

    public int getFilteredUsersCount() {
        return filteredUsers != null ? filteredUsers.size() : 0;
    }

    public Long usersWithDependants() {
        return userDAO.countUsersWithDependants();
    }

    public Long usersWithoutDependants() {
        return getFilteredUsersCount() - usersWithDependants();
    }

    public Long deletedUsers(){
        return userDAO.countDeletedUsersWithRoleUSER();
    }

    public int getDependants(User user){
        List<Dependant> dependants = userDAO.getDependants(user);
        if(dependants == null) {
            return 0;
        }
        return dependants.size();
    }



}
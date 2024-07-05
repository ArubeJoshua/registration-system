package org.pahappa.systems.registrationapp.views;

import org.pahappa.systems.registrationapp.dao.DependantDAO;
import org.pahappa.systems.registrationapp.models.Dependant;
import org.pahappa.systems.registrationapp.models.Gender;
import org.pahappa.systems.registrationapp.models.User;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@ManagedBean
@ViewScoped
public class UserHomeBean implements Serializable {
    private static final long serialVersionUID = 3L;
    private final DependantDAO dependantDAO = new DependantDAO();
    private User user;
    private List<Dependant> filteredDependants;
    private List<Dependant> dependants;
    private String search;
    private Gender gender;
    private Dependant dependant;
    private String firstname;
    private String lastname;
    private Date dateOfBirth;


    LoginBean loginBean = new LoginBean();

    @PostConstruct
    public void init() {
        user = loginBean.getCurrentUser();
        filteredDependants = dependants = getDependantsForUser();

    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public List<Dependant> getDependants() {
        return dependants;
    }

    public void setDependants(List<Dependant> dependants) {
        this.dependants = dependants;
    }

    public List<Dependant> getFilteredDependants() {
        return filteredDependants;
    }

    public void setFilteredDependants(List<Dependant> filteredDependants) {
        this.filteredDependants = filteredDependants;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public List<Dependant> getDependantsForUser() {
        if (user == null) {
            // handle the null case
            return new ArrayList<>();
        }
        return dependantDAO.getDependants(user, false);
    }


    public void searchDependants() {
        if (search == null || search.isEmpty()) {
            filteredDependants = dependants;
        } else {
            filteredDependants = dependants.stream()
                    .filter(dependant -> dependant.getFirstname().toLowerCase().contains(search.toLowerCase()) ||
                            dependant.getLastname().toLowerCase().contains(search.toLowerCase()))
                    .collect(Collectors.toList());
        }

    }

    public int getFilteredDependantsCount() {
        return filteredDependants != null ? filteredDependants.size() : 0;
    }

    public void filterDependants() {
        if (gender == null) {
            filteredDependants = dependants; // Reset to all dependants if no gender is selected
        } else {
            filteredDependants = dependants.stream()
                    .filter(dependant -> gender.equals(dependant.getGender()))
                    .collect(Collectors.toList());
        }
    }

    public String saveDependant() {
        User cu = user;

        dependant.setFirstname(firstname);
        dependant.setLastname(lastname);
        dependant.setDateOfBirth(dateOfBirth);
        dependant.setGender(gender);
        dependant.setUser(cu);

        System.out.println(cu);
        dependantDAO.save(dependant);

        return "/pages/dependants/userDependants.xhtml?faces-redirect=true";
    }
}

package org.pahappa.systems.registrationapp.views;

import org.pahappa.systems.registrationapp.dao.DependantDAO;
import org.pahappa.systems.registrationapp.models.Dependant;
import org.pahappa.systems.registrationapp.models.Gender;
import org.pahappa.systems.registrationapp.models.User;
import org.pahappa.systems.registrationapp.services.DependantService;

import javax.annotation.PostConstruct;
import javax.el.MethodExpression;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@ManagedBean
@ViewScoped
public class DependantBean implements Serializable {
    private static final long serialVersionUID = 2L;
    private String firstname;
    private String lastname;
    private Date dateOfBirth;
    private Gender gender;
    private String search;
    private Dependant selectedDependant;
    private List<Dependant> dependantList;
    private List<Dependant> filteredDependants;
    private Dependant dependant = new Dependant();
    private final DependantService dependantService = new DependantService();
    DependantDAO dependantDAO = new DependantDAO();


    @PostConstruct
    public void init() {

        filteredDependants = dependantList = dependantService.retrieveDependantsList();
        selectedDependant = new Dependant();
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public List<Dependant> getFilteredDependants() {
        return filteredDependants;
    }

    public void setFilteredDependants(List<Dependant> filteredDependants) {
        this.filteredDependants = filteredDependants;
    }

    public Dependant getDependant() {
        return dependant;
    }

    public void setDependant(Dependant dependant) {
        this.dependant = dependant;
    }

    public String getSearch() {
        return search;
    }


    public void setSearch(String search) {
        this.search = search;
        searchDependants();
    }

    public Dependant getSelectedDependant() {
        return selectedDependant;
    }

    public void setSelectedDependant(Dependant selectedDependant) {
        this.selectedDependant = selectedDependant;
    }

    public void selectDependantForUpdate(Dependant dependant) {
        this.selectedDependant = dependant;
    }


    public List<Dependant> getDependants() {
        return dependantService.retrieveDependantsList();
    }

    public Gender[] getGenders() {
        return Gender.values();
    }

    public String saveDependant() {
        User cu = getSessionUser();

        dependant.setFirstname(firstname);
        dependant.setLastname(lastname);
        dependant.setDateOfBirth(dateOfBirth);
        dependant.setGender(gender);
        dependant.setUser(cu);

        System.out.println(cu);
        dependantDAO.save(dependant);

        return "/pages/dependants/userDependants.xhtml?faces-redirect=true";
    }

    public List<Dependant> getDependantsForUser(User user) {
        if (user == null) {
            // handle the null case
            return new ArrayList<>();
        }
        return dependantDAO.getDependants(user, false);
    }


    public void searchDependants() {
        if (search == null || search.isEmpty()) {
            filteredDependants = dependantList;
        } else {
            filteredDependants = dependantList.stream()
                    .filter(dependant -> dependant.getFirstname().toLowerCase().contains(search.toLowerCase()) ||
                            dependant.getLastname().toLowerCase().contains(search.toLowerCase()))
                    .collect(Collectors.toList());
        }

    }

    public int getFilteredDependantsCount() {
        return filteredDependants != null ? filteredDependants.size() : 0;
    }

    public User getSessionUser() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        return (User) externalContext.getSessionMap().get("user");
    }

    public String deleteAllDependants() {
        dependantDAO.deleteAllEntities();
        addMessage("Confirmed", "Records deleted");
        return "/pages/dependants/viewDependants.xhtml?faces-redirect=true";
    }

    public void delete(Dependant dependant) {
        System.out.println(dependant + "To be deleted");
        dependantDAO.deleteDependant(dependant);
        addMessage("Confirmed", "Record deleted");
    }

    public void update(Dependant dependant) {
        dependantDAO.update(dependant);
        addMessage("Success", "Record Updated");
    }

    public void addMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);

    }

    public Long maleDependants() {
        return dependantDAO.countMaleDependantsNotDeleted();
    }

    public Long femaleDependants() {
        return getFilteredDependantsCount() - maleDependants();
    }

    public void filterDependants() {
        if (gender == null) {
            filteredDependants = dependantList; // Reset to all dependants if no gender is selected
        } else {
            filteredDependants = dependantList.stream()
                    .filter(dependant -> gender.equals(dependant.getGender()))
                    .collect(Collectors.toList());
        }
    }

    public int getFemaleDependants() {
        return dependantList.stream()
                .filter(dependant -> dependant.getGender() == Gender.FEMALE)
                .collect(Collectors.toList()).size();
    }

    public int getMaleDependants() {
        return dependantList.stream()
                .filter(dependant -> dependant.getGender() == Gender.MALE)
                .collect(Collectors.toList()).size();
    }

}
package org.pahappa.systems.registrationapp.models;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User extends TopUser {
    @Column(name = "username")
    protected String username;

    @Column(name= "role")
    private String role;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Dependant> dependants;

    public User() {
    }

    private User(String username, String firstname, String lastname, Date dateOfBirth) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.dateOfBirth = dateOfBirth;
    }

    public String getUsername() { return username;  }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() { return role; }

    public void setRole(String role) { this.role = role; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public List<Dependant> getDependants() {
        return dependants;
    }

    public void setDependants(List<Dependant> dependants) {
        this.dependants = dependants;
    }

    @Override
    public String toString() {
        return "username: " + Objects.toString(username, "Unkwown")+
                "\n Firstname: " + Objects.toString(firstname, "Unkwown")+
                "\n Lastname: " + Objects.toString(lastname, "Unkwown")
                + "\n Date: " + Objects.toString(dateOfBirth, "Unkwown")
                + "\n Email: " + Objects.toString(email, "Unkwown")
                + "\n ID: " + id
                + "\n role" + Objects.toString(role, "Unkwown");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) &&
                Objects.equals(firstname, user.firstname) &&
                Objects.equals(lastname, user.lastname) &&
                Objects.equals(dateOfBirth, user.dateOfBirth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, firstname, lastname, dateOfBirth);
    }
}

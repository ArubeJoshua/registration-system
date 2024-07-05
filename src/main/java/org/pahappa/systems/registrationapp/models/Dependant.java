package org.pahappa.systems.registrationapp.models;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "dependants")
public class Dependant extends TopUser {

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Dependant() {
    }

    private Dependant(String firstname, String lastname, Date dateOfBirth, Gender gender) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dependant dependant = (Dependant) o;
        return gender == dependant.gender &&
                Objects.equals(user, dependant.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gender, user);
    }

    @Override
    public String toString() {
        return "Firstname: " + Objects.toString(firstname, "Unkwown")+
                "\n Lastname: " + Objects.toString(lastname, "Unkwown")
                + "\n Date: " + Objects.toString(dateOfBirth, "Unkwown")
                + "\n Gender: " + Objects.toString(gender, "Unkwown")
                + "\n ID: " + id
                ;
    }
}

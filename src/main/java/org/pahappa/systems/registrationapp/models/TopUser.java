package org.pahappa.systems.registrationapp.models;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
public class TopUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected long id;

    @Column(name = "first_name")
    protected String firstname;

    @Column(name = "last_name")
    protected String lastname;

    @Column(name = "date_of_birth")
    protected Date dateOfBirth;

    @Column(name = "is_deleted",columnDefinition = "BOOLEAN DEFAULT false")
    protected boolean deleted;

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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isDeleted() { return deleted; }

    public void setDeleted(boolean deleted) { this.deleted = deleted; }

}
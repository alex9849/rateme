package de.hskl.rateme.model;

import de.hskl.rateme.util.Validator;

import java.sql.Timestamp;

public class User {
    private int id;

    @Validator.Required()
    private String username;

    @Validator.Required()
    @Validator.Regex(regex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", errorMessage = "Email invalid!")
    private String email;

    @Validator.Required
    private String firstname;

    @Validator.Required
    private String lastname;

    @Validator.Required
    private String street;

    @Validator.Required
    private String streetNr;

    @Validator.Required
    private String zip;

    @Validator.Required
    private String city;

    @Validator.Required
    private String password;

    private Timestamp createDt;
    private Timestamp modifyDt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreetNr() {
        return streetNr;
    }

    public void setStreetNr(String streetNr) {
        this.streetNr = streetNr;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Timestamp getCreateDt() {
        return createDt;
    }

    public void setCreateDt(Timestamp createDt) {
        this.createDt = createDt;
    }

    public Timestamp getModifyDt() {
        return modifyDt;
    }

    public void setModifyDt(Timestamp modifyDt) {
        this.modifyDt = modifyDt;
    }
}

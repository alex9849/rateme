package de.hskl.rateme.model;

import de.hskl.rateme.util.Validator;

import java.sql.Date;

public class User implements Cloneable {
    private int id;

    @Validator.Required()
    @Validator.Regex(regex = "^.+$", errorMessage = "Nutzername benötigt!")
    private String username;

    @Validator.Required()
    @Validator.Regex(regex = "^[a-z]{4,4}[0-9]{4,4}@(stud\\.(hs|fh)-kl\\.de)$", errorMessage = "Hochschul-Email benötigt! (klein geschrieben)")
    private String email;

    @Validator.Required
    @Validator.Regex(regex = "^[A-Z]([^0-9\\§\\%\\&\\!\\?])+?[a-z]$", errorMessage = "Vorname muss mit einem Großbuchstaben anfangen und mit einem Kleinbuchstaben enden! Die Zeichen '§', '%', '&', '!' und '?' sind nicht erlaubt!")
    private String firstname;

    @Validator.Required
    @Validator.Regex(regex = "^[A-Z]([^0-9\\§\\%\\&\\!\\?])+?[a-z]$", errorMessage = "Nachname muss mit einem Großbuchstaben anfangen und mit einem Kleinbuchstaben enden! Die Zeichen '§', '%', '&', '!' und '?' sind nicht erlaubt!")
    private String lastname;

    @Validator.Required
    @Validator.Regex(regex = "^.+$", errorMessage = "Straße benötigt!")
    private String street;

    @Validator.Required(errorMessage = "Hausnummer benötigt!")
    @Validator.Regex(regex = "^[0-9]+$", errorMessage = "HausNUMMER benötigt!")
    private String streetNr;

    @Validator.Required
    @Validator.Regex(regex = "^[0-9]{5,5}$", errorMessage = "Postleitzahl muss aus 5 Zahlen bestehen!")
    private String zip;

    @Validator.Required
    @Validator.Regex(regex = "^.+$", errorMessage = "Stadt benötigt!")
    private String city;

    @Validator.Required
    @Validator.Regex(regex = "^.+$", errorMessage = "Passwort benötigt!")
    private String password;

    private Date createDt;
    private Date modifyDt;

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

    public Date getCreateDt() {
        return createDt;
    }

    public void setCreateDt(Date createDt) {
        this.createDt = createDt;
    }

    public Date getModifyDt() {
        return modifyDt;
    }

    public void setModifyDt(Date modifyDt) {
        this.modifyDt = modifyDt;
    }

    public User clone() throws CloneNotSupportedException {
        return (User) super.clone();
    }

    public User cloneForFrontend() {
        try {
            User clone = this.clone();
            clone.setPassword("***********");
            return clone;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            throw new RatemeDbException("Could not clone user for frontend!", e);
        }
    }
}

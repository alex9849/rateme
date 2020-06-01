package de.hskl.rateme.model;

import de.hskl.rateme.util.Validator;

import java.sql.Date;

public class Rating {
    private int id;
    private String username;
    private int userId;
    private long osmId;
    @Validator.Required
    @Validator.Regex(regex = "^[1-5]$", errorMessage = "Bewertung benötigt!")
    private int grade;
    @Validator.Required
    @Validator.Regex(regex = "^.+$", errorMessage = "Kommentar benötigt!")
    private String text;
    @Validator.Regex(regex = "^data:image\\/(png|jpeg);base64,(.)+$", errorMessage = "%fieldname% needs to be a png or jpg file!")
    private String image;
    private Date createDt;
    private Date modifyDt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public long getOsmId() {
        return osmId;
    }

    public void setOsmId(long osmId) {
        this.osmId = osmId;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}

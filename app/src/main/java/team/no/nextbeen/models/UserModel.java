package team.no.nextbeen.models;

import java.time.LocalDate;

public class UserModel {
    private String id, fullName, shortId, email, bio, avatar;
    private LocalDate dayOfBirth;

    public UserModel(String id, String fullName, String shortId, String email, String bio, String avatar, LocalDate dayOfBirth) {
        this.id = id;
        this.fullName = fullName;
        this.shortId = shortId;
        this.email = email;
        this.bio = bio;
        this.avatar = avatar;
        this.dayOfBirth = dayOfBirth;
    }

    public UserModel() {}

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getShortId() {
        return shortId;
    }

    public void setShortId(String shortId) {
        this.shortId = shortId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDayOfBirth() {
        return dayOfBirth;
    }

    public void setDayOfBirth(LocalDate dayOfBirth) {
        this.dayOfBirth = dayOfBirth;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}

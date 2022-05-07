package team.no.nextbeen.viewmodels;

import java.util.List;

public class UserViewModel {
    private String id, fullName, shortId, email, bio, avatar;
    private List<ReviewViewModel> posts;

    public UserViewModel(String id, String fullName, String shortId, String email, String bio, String avatar, List<ReviewViewModel> posts) {
        this.id = id;
        this.fullName = fullName;
        this.shortId = shortId;
        this.email = email;
        this.bio = bio;
        this.avatar = avatar;
        this.posts = posts;
    }

    public UserViewModel() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public List<ReviewViewModel> getPosts() {
        return posts;
    }

    public void setPosts(List<ReviewViewModel> posts) {
        this.posts = posts;
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

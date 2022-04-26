package team.no.nextbeen.viewmodels;

import java.util.List;

public class ReviewViewModel {
    private String shortId, fullName, author, content, address;
    private List<String> images;

    public ReviewViewModel(String shortId, String fullName, String author, String content, String address, List<String> images) {
        this.shortId = shortId;
        this.fullName = fullName;
        this.author = author;
        this.content = content;
        this.address = address;
        this.images = images;
    }

    public ReviewViewModel() {

    }


    public String getShortId() {
        return shortId;
    }

    public void setShortId(String shortId) {
        this.shortId = shortId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getShortDesc(int length) {
        return content.substring(0, Math.min(length, content.length())) + "...";
    }
}

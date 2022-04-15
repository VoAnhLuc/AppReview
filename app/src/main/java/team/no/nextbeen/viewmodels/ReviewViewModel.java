package team.no.nextbeen.viewmodels;

import java.util.List;

public class ReviewViewModel {
    private String shortId, author, content, address;
    private List<String> images;

    public ReviewViewModel(String shortId, String author, String content, String address, List<String> images) {
        this.shortId = shortId;
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
}

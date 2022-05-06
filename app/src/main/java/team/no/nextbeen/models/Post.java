package team.no.nextbeen.models;

public class Post {
    private String url;
    private String title;
    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }
    public Post()
    {

    }
    public Post(String url, String title) {
        this.url = url;
        this.title = title;
    }

}

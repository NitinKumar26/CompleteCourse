package in.completecourse.model;

public class Update {

    private final String name;

    private final String url;

    public Update(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
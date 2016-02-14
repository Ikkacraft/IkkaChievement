package ws;

public class Badge {
    private int badge_id;
    private String title;
    private int category_id;
    private String description;
    private String parameters;
    private String urlImage;

    public Badge() {

    }

    public Badge(int badge_id, String title, int category_id, String description, String parameters, String urlImage) {
        this.badge_id = badge_id;
        this.title = title;
        this.category_id = category_id;
        this.description = description;
        this.parameters = parameters;
        this.urlImage = urlImage;
    }

    public int getBadge_id() {
        return badge_id;
    }

    public void setBadge_id(int badge_id) {
        this.badge_id = badge_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }
}

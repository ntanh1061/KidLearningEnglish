package anhnt.pickidlearning.models;

/**
 * Created by AnhNT on 4/26/2017.
 */

public class Type {
    private String image;
    private String title;

    public Type(String image, String title) {
        this.image = image;
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

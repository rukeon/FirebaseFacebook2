package rukeon.com.firebasefacebook;

/**
 * Created by rukeon01 on 2016-10-03.
 */

public class Blog {
    // shortcut to getter, setter -> alt + N
    private  String title, desc, image;

    // if you don't have this constructor, app will crush
    public Blog() {

    }

    public Blog(String title, String desc, String image) {
        this.title = title;
        this.desc = desc;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

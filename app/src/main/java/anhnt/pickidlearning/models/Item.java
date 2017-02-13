package anhnt.pickidlearning.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2/13/2017.
 */

public class Item implements Parcelable {
    private int id;
    private String image;
    private String name;
    private String sound;
    private int categoryId;

    public Item(int id, String image, String name, String sound, int categoryId) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.sound = sound;
        this.categoryId = categoryId;
    }

    protected Item(Parcel in) {
        id = in.readInt();
        image = in.readString();
        name = in.readString();
        sound = in.readString();
        categoryId = in.readInt();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(image);
        dest.writeString(name);
        dest.writeString(sound);
        dest.writeInt(categoryId);
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", image='" + image + '\'' +
                ", name='" + name + '\'' +
                ", sound='" + sound + '\'' +
                ", categoryId=" + categoryId +
                '}';
    }
}

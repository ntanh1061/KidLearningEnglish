package anhnt.pickidlearning.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by AnhNT on 1/25/2017.
 */

public class Category implements Parcelable {
    private int id;
    private String pathImage;
    private String  name;

    public Category(int id, String pathImage, String name) {
        this.id = id;
        this.pathImage = pathImage;
        this.name = name;
    }

    protected Category(Parcel in) {
        id = in.readInt();
        pathImage = in.readString();
        name = in.readString();
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPathImage() {
        return pathImage;
    }

    public void setPathImage(String pathImage) {
        this.pathImage = pathImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(pathImage);
        dest.writeString(name);
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", pathImage='" + pathImage + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}

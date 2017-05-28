package anhnt.pickidlearning.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by HCD-Fresher046 on 1/10/2017.
 */

public class TuDien implements Parcelable {
    private int id;
    private String word;
    private String content;
    private int fav;
    private String note;

    public TuDien(int id, String word, String content, int fav) {
        this.id = id;
        this.word = word;
        this.content = content;
        this.fav = fav;
    }

    public TuDien(String word, String content, int fav) {
        this.word = word;
        this.content = content;
        this.fav = fav;
    }

    public TuDien(String note, String word, String content, int fav) {
        this.note = note;
        this.word = word;
        this.content = content;
        this.fav = fav;
    }

    public TuDien(String note, int id, String word, String content, int fav) {
        this.note = note;
        this.id = id;
        this.word = word;
        this.content = content;
        this.fav = fav;
    }

    protected TuDien(Parcel in) {
        id = in.readInt();
        word = in.readString();
        content = in.readString();
        fav = in.readInt();
        note = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(word);
        dest.writeString(content);
        dest.writeInt(fav);
        dest.writeString(note);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TuDien> CREATOR = new Creator<TuDien>() {
        @Override
        public TuDien createFromParcel(Parcel in) {
            return new TuDien(in);
        }

        @Override
        public TuDien[] newArray(int size) {
            return new TuDien[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getFav() {
        return fav;
    }

    public void setFav(int fav) {
        this.fav = fav;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}

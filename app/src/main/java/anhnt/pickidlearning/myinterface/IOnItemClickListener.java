package anhnt.pickidlearning.myinterface;

/**
 * Created by AnhNT on 4/11/2017.
 */

public interface IOnItemClickListener {
    void setOnItemClick(int position);
    void itemClick(int position);
    void yourWordClick(int posirion);
    void speakerItemClick(int position);
    void itemLongClick(int position);
}

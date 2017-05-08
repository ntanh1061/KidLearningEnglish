package anhnt.pickidlearning.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import org.json.JSONException;

import java.io.IOException;

import anhnt.pickidlearning.ConstValue;
import anhnt.pickidlearning.R;
import anhnt.pickidlearning.ReadJson;
import anhnt.pickidlearning.models.Item;
import anhnt.pickidlearning.myinterface.ISendItemID;

/**
 * Created by AnhNT on 5/3/2017.
 */

public class ChooseWordFragment extends Fragment implements View.OnClickListener {
    public ISendItemID iSendItemID;
    private Bundle mBundle;
    private ImageView mImgDetail;
    private int[] arrItemId;
    private int mItemId;
    private int resID;
    private String mWord;
    private Button btnWordOne;
    private Button btnWordTwo;
    private String mWord1;
    private int tam = -1;
    private Handler mHandler;

    public void onAttach(Context context) {
        super.onAttach(context);
        this.iSendItemID = (ISendItemID) context;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_word, container, false);
        mHandler = new Handler();
        mBundle = getArguments();
        mImgDetail = (ImageView) view.findViewById(R.id.img_detail);
        btnWordOne = (Button) view.findViewById(R.id.btn_word_2);
        btnWordOne.setOnClickListener(this);
        btnWordTwo = (Button) view.findViewById(R.id.btn_word_3);
        btnWordTwo.setOnClickListener(this);

        arrItemId = mBundle.getIntArray(ConstValue.ITEM_ID_ARRAY);
        mItemId = arrItemId[0];
        mWord = getItems(mItemId - 1).getName();
        mWord1 = getItems(arrItemId[1]).getName();
        tam = getRandom(0, 1);
        if (tam == 0) {
            setTextButton(btnWordOne, mWord);
            setTextButton(btnWordTwo, mWord1);
        } else {
            setTextButton(btnWordTwo, mWord);
            setTextButton(btnWordOne, mWord1);
        }
        setImage(mImgDetail);
        return view;
    }

    public void setTextButton(Button button, String text) {
        button.setText(text);
    }

    public Item getItems(int itemID) {
        Item item = null;
        try {
            item = ReadJson.getItemByItemID(getContext(), itemID);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return item;
    }

    public void setImage(ImageView view) {
        resID = getContext().getResources().getIdentifier(getItems(mItemId - 1).getImage().toString(), "drawable", getContext().getPackageName());
        view.setImageDrawable(getContext().getResources().getDrawable(resID));
    }

    private int getRandom(int min, int max) {
        int i = (int) (Math.random() * (max - min + 1) + min);
        return i;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_word_2:
                setBackgroundButton(btnWordOne);
                break;
            case R.id.btn_word_3:
                setBackgroundButton(btnWordTwo);
                break;
        }
    }

    public void setBackgroundButton(Button button) {

        if (button.getText().toString() == mWord) {
            button.setBackgroundResource(R.color.color_wrong);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    iSendItemID.sendItemID(arrItemId[0], 5);
                }
            }, 1000);
        } else {
            button.setBackgroundResource(R.color.button_color_right);
        }
    }
}



package anhnt.pickidlearning.fragment;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.util.Locale;

import anhnt.pickidlearning.ConstValue;
import anhnt.pickidlearning.R;
import anhnt.pickidlearning.ReadJson;
import anhnt.pickidlearning.models.Item;
import anhnt.pickidlearning.myinterface.ISendItemID;

/**
 * Created by AnhNT on 4/27/2017.
 */

public class ListenWriteFragment extends Fragment implements View.OnClickListener {
    private int mItemId;
    private Bundle mBundle;
    private String mWord;
    private Handler mHandler;
    private int resID;
    private ImageView mImgDetail;
    private String[] charArr = new String[26];
    private Button btn_one;
    private Button btn_two;
    private Button btn_three;
    private Button btn_four;
    private Button btn_five;
    private Button btn_six;
    private Button btn_seven;
    private Button btn_eight;
    private Button btn_nine;
    private Button btn_ten;
    private TextView tv_char_0;
    private TextView tv_char_1;
    private TextView tv_char_2;
    private TextView tv_char_3;
    private TextView tv_char_4;
    private TextView tv_char_5;
    private TextView tv_char_6;
    private TextView tv_char_7;
    private TextView tv_char_8;
    private TextView tv_char_9;
    private TextView tv_char_10;
    private TextView tv_char_11;
    private TextView tv_char_12;
    private int wordCount = 0;
    private int mPosition = 0;
    private String wordArr[] = new String[10];
    private String charWordArr[];
    private int i1 = -1;
    private int i2 = -1;
    private int i3 = -1;
    private int i4 = -1;
    private int i5 = -1;
    private int i6 = -1;
    private int i7 = -1;
    private int i8 = -1;
    private int i9 = -1;
    private int i10 = -1;
    public ISendItemID iSendItemID;
    private int[] arrItemId;
    private ImageView mImgHelp;
    private ImageView mImgSpeak;
    private String wordCompare = "";
    private TextToSpeech mTextToSpeech;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.iSendItemID = (ISendItemID) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listen_write, container, false);
        mImgDetail = (ImageView) view.findViewById(R.id.img_detail);
        init(view);
        mBundle = getArguments();
        arrItemId = mBundle.getIntArray(ConstValue.ITEM_ID_ARRAY);
        mItemId = arrItemId[0];
        mWord = getItems(mItemId - 1).getName();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                readText(mWord);
            }
        }, 500);
        setImage(mImgDetail);
        charWordArr = mWord.split("");
        for (char word = 'a'; word < 'z'; word++) {
            charArr[wordCount] = String.valueOf(word);
            wordCount++;
        }
        for (int i = 0; i < charWordArr.length - 1; i++) {
            charArr[i] = charWordArr[i + 1];
        }
        Xoatrung(charArr);
        setButton(btn_one, charArr[i1 = getRandom(0, 9, i1, i2, i3, i4, i5, i6, i7, i8, i9)]);
        setButton(btn_two, charArr[i2 = getRandom(0, 9, i1, i2, i3, i4, i5, i6, i7, i8, i9)]);
        setButton(btn_three, charArr[i3 = getRandom(0, 9, i1, i2, i3, i4, i5, i6, i7, i8, i9)]);
        setButton(btn_four, charArr[i4 = getRandom(0, 9, i1, i2, i3, i4, i5, i6, i7, i8, i9)]);
        setButton(btn_five, charArr[i5 = getRandom(0, 9, i1, i2, i3, i4, i5, i6, i7, i8, i9)]);
        setButton(btn_six, charArr[i6 = getRandom(0, 9, i1, i2, i3, i4, i5, i6, i7, i8, i9)]);
        setButton(btn_seven, charArr[i7 = getRandom(0, 9, i1, i2, i3, i4, i5, i6, i7, i8, i9)]);
        setButton(btn_eight, charArr[i8 = getRandom(0, 9, i1, i2, i3, i4, i5, i6, i7, i8, i9)]);
        setButton(btn_nine, charArr[i9 = getRandom(0, 9, i1, i2, i3, i4, i5, i6, i7, i8, i9)]);
        setButton(btn_ten, charArr[i10 = getRandom(0, 9, i1, i2, i3, i4, i5, i6, i7, i8, i9)]);
        Log.d("Chuoi", "Cac ky tu " + i1 + ":" + i2 + ":" + i3 + ":" + i4 + ":" + i5 + ":" + i6 + ":" + i7 + ":" + i8 + ":" + i9 + ":");

//        for (int i = 0; i < charArr.length; i++) {
//            Log.d("Chuoi", "Cut: " + charArr[i]);
//        }

        for (int i = 0; i < charWordArr.length; i++) {
            Log.d("Chuoi", "onCreateView: " + charWordArr[i]);
        }
        for (int i = 0; i < charWordArr.length - 1; i++) {
            setmWord(i, view, "");
        }
        for (int i = 0; i < 10; i++) {
            wordArr[i] = getTextButton(view, i);
        }

        setBackgroundRight(mPosition);
        return view;
    }

    public void Xoatrung(String a[]) {
        int n = a.length;
        int i, j, k;
        for (i = 0; i < (n) - 1; i++) {
            j = i + 1;
            while (j < n)
                if (String.valueOf(a[i]).equalsIgnoreCase(String.valueOf(a[j]))) {
                    for (k = j; k < n - 1; k++) a[k] = a[k + 1];
                    n = n - 1;
                } else j = j + 1;
        }
    }


    public void setButton(Button button, String text) {
        button.setText(text);
    }

    public void init(View view) {
        mHandler = new Handler();
        btn_one = (Button) view.findViewById(R.id.btn_0);
        btn_one.setOnClickListener(this);
        btn_two = (Button) view.findViewById(R.id.btn_1);
        btn_two.setOnClickListener(this);
        btn_three = (Button) view.findViewById(R.id.btn_2);
        btn_three.setOnClickListener(this);
        btn_four = (Button) view.findViewById(R.id.btn_3);
        btn_four.setOnClickListener(this);
        btn_six = (Button) view.findViewById(R.id.btn_5);
        btn_six.setOnClickListener(this);
        btn_seven = (Button) view.findViewById(R.id.btn_6);
        btn_seven.setOnClickListener(this);
        btn_eight = (Button) view.findViewById(R.id.btn_7);
        btn_eight.setOnClickListener(this);
        btn_nine = (Button) view.findViewById(R.id.btn_8);
        btn_nine.setOnClickListener(this);
        btn_ten = (Button) view.findViewById(R.id.btn_9);
        btn_ten.setOnClickListener(this);
        btn_five = (Button) view.findViewById(R.id.btn_4);
        btn_five.setOnClickListener(this);
        mImgHelp = (ImageView) view.findViewById(R.id.img_help);
        mImgHelp.setOnClickListener(this);
        mImgSpeak = (ImageView) view.findViewById(R.id.img_play_sound);
        mImgSpeak.setOnClickListener(this);
        tv_char_0 = (TextView) view.findViewById(R.id.tv_char_0);
        tv_char_1 = (TextView) view.findViewById(R.id.tv_char_1);
        tv_char_2 = (TextView) view.findViewById(R.id.tv_char_2);
        tv_char_3 = (TextView) view.findViewById(R.id.tv_char_3);
        tv_char_4 = (TextView) view.findViewById(R.id.tv_char_4);
        tv_char_5 = (TextView) view.findViewById(R.id.tv_char_5);
        tv_char_6 = (TextView) view.findViewById(R.id.tv_char_6);
        tv_char_7 = (TextView) view.findViewById(R.id.tv_char_7);
        tv_char_8 = (TextView) view.findViewById(R.id.tv_char_8);
        tv_char_9 = (TextView) view.findViewById(R.id.tv_char_9);
        tv_char_10 = (TextView) view.findViewById(R.id.tv_char_10);
        tv_char_11 = (TextView) view.findViewById(R.id.tv_char_11);
        tv_char_12 = (TextView) view.findViewById(R.id.tv_char_12);
        setupTextToSpeech();
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

    public void setBackgroundRight(int position) {
        for (int j = 0; j < 10; j++) {
            if (charArr[position] == charArr[j]) {
                if (j == i1) {
                    setBackgroundButton(btn_one, true);
                }
                if (j == i2) {
                    setBackgroundButton(btn_two, true);
                }
                if (j == i3) {
                    setBackgroundButton(btn_three, true);
                }
                if (j == i4) {
                    setBackgroundButton(btn_four, true);
                }
                if (j == i5) {
                    setBackgroundButton(btn_five, true);
                }
                if (j == i6) {
                    setBackgroundButton(btn_six, true);
                }
                if (j == i7) {
                    setBackgroundButton(btn_seven, true);
                }
                if (j == i8) {
                    setBackgroundButton(btn_eight, true);
                }
                if (j == i9) {
                    setBackgroundButton(btn_nine, true);
                }
                if (j == i10) {
                    setBackgroundButton(btn_ten, true);
                }
                break;
            }
        }
    }
//
//    public void setButtonWrong(int position){
//            setBackgroundButton(btn_one, true);
//            setBackgroundButton(btn_two, true);
//            setBackgroundButton(btn_three, true);
//            setBackgroundButton(btn_four, true);
//            setBackgroundButton(btn_five, true);
//            setBackgroundButton(btn_six, true);
//            setBackgroundButton(btn_seven, true);
//            setBackgroundButton(btn_eight, true);
//            setBackgroundButton(btn_nine, true);
//            setBackgroundButton(btn_ten, true);
//
//    }

    @Override
    public void onClick(View v) {
//        String wordCompare = "";
        switch (v.getId()) {
            case R.id.btn_0:
                wordCompare = getTextButton(v, 0);
                setBackgroundButton(btn_one, false);
                break;
            case R.id.btn_1:
                wordCompare = getTextButton(v, 1);
                setBackgroundButton(btn_two, false);
                break;
            case R.id.btn_2:
                wordCompare = getTextButton(v, 2);
                setBackgroundButton(btn_three, false);
                break;
            case R.id.btn_3:
                wordCompare = getTextButton(v, 3);
                setBackgroundButton(btn_four, false);
                break;
            case R.id.btn_4:
                wordCompare = getTextButton(v, 4);
                setBackgroundButton(btn_five, false);
                break;
            case R.id.btn_5:
                wordCompare = getTextButton(v, 5);
                setBackgroundButton(btn_six, false);
                break;
            case R.id.btn_6:
                wordCompare = getTextButton(v, 6);
                setBackgroundButton(btn_seven, false);
                break;
            case R.id.btn_7:
                wordCompare = getTextButton(v, 7);
                setBackgroundButton(btn_eight, false);
                break;
            case R.id.btn_8:
                wordCompare = getTextButton(v, 8);
                setBackgroundButton(btn_nine, false);
                break;
            case R.id.btn_9:
                wordCompare = getTextButton(v, 9);
                setBackgroundButton(btn_ten, false);
                break;
        }

        if (charArr[mPosition] == wordCompare) {
            switch (mPosition) {
                case 0:
                    tv_char_0.setText(wordCompare);
                    mPosition++;
                    checkWordFinish(mPosition);
                    break;
                case 1:
                    tv_char_1.setText(wordCompare);
                    mPosition++;
                    checkWordFinish(mPosition);
                    break;
                case 2:
                    tv_char_2.setText(wordCompare);
                    mPosition++;
                    checkWordFinish(mPosition);
                    break;
                case 3:
                    tv_char_3.setText(wordCompare);
                    mPosition++;
                    checkWordFinish(mPosition);
                    break;
                case 4:
                    tv_char_4.setText(wordCompare);
                    mPosition++;
                    checkWordFinish(mPosition);
                    break;
                case 5:
                    tv_char_5.setText(wordCompare);
                    mPosition++;
                    checkWordFinish(mPosition);
                    break;
                case 6:
                    tv_char_6.setText(wordCompare);
                    mPosition++;
                    checkWordFinish(mPosition);
                    break;
                case 7:
                    tv_char_7.setText(wordCompare);
                    mPosition++;
                    checkWordFinish(mPosition);
                    break;
                case 8:
                    tv_char_8.setText(wordCompare);
                    mPosition++;
                    checkWordFinish(mPosition);
                    break;
                case 9:
                    tv_char_9.setText(wordCompare);
                    mPosition++;
                    checkWordFinish(mPosition);
                    break;
                case 10:
                    tv_char_10.setText(wordCompare);
                    mPosition++;
                    checkWordFinish(mPosition);
                    break;
                case 11:
                    tv_char_11.setText(wordCompare);
                    mPosition++;
                    checkWordFinish(mPosition);
                    break;
                case 12:
                    tv_char_12.setText(wordCompare);
                    mPosition++;
                    checkWordFinish(mPosition);
                    break;
            }
            setBackgroundRight(mPosition);
        }
        if (v.getId() == R.id.img_help) {
            tv_char_0.setText(mWord);
            setTextVisibility();
            setClick();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    tv_char_0.setVisibility(View.GONE);
                    setTextVisibility();
                }
            }, 1000);
        }
        if (v.getId() == R.id.img_play_sound) {
            readText(mWord);
        }
    }

    public void checkWordFinish(int position) {
        if (position == charWordArr.length - 1) {
            setClick();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    tv_char_0.setVisibility(View.GONE);
                    setTextVisibility();
                }
            }, 1000);

        }
    }

    public void setBackgroundButton(Button btn, boolean check) {
        if (check) {
            btn.setBackgroundResource(R.drawable.background_button_custom_right);
        } else {
            btn.setBackgroundResource(R.drawable.background_button_custom_wrong);
        }
    }

    public void setTextVisibility() {
//        tv_char_0.setVisibility(View.GONE);
        tv_char_1.setVisibility(View.GONE);
        tv_char_2.setVisibility(View.GONE);
        tv_char_3.setVisibility(View.GONE);
        tv_char_4.setVisibility(View.GONE);
        tv_char_5.setVisibility(View.GONE);
        tv_char_6.setVisibility(View.GONE);
        tv_char_7.setVisibility(View.GONE);
        tv_char_8.setVisibility(View.GONE);
        tv_char_9.setVisibility(View.GONE);
        tv_char_10.setVisibility(View.GONE);
        tv_char_11.setVisibility(View.GONE);
        tv_char_12.setVisibility(View.GONE);
    }

    public void setmWord(int position, View view, String word) {
        int tvID = getContext().getResources().getIdentifier("tv_char_" + position, "id", getContext().getPackageName());
        TextView tv = (TextView) view.findViewById(tvID);
        tv.setVisibility(View.VISIBLE);
        tv.setText(" _ " + word);
    }

    public String getTextButton(View view, int position) {
        int btnID = getContext().getResources().getIdentifier("btn_" + position, "id", getContext().getPackageName());
        Button button = (Button) view.findViewById(btnID);
        return button.getText().toString();
    }

    private int getRandom(int min, int max, int i1, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9) {
        int i = (int) (Math.random() * (max - min + 1) + min);
        while (i == i1 || i == i2 || i == i3 || i == i4 || i == i5 || i == i6 || i == i7 || i == i8 || i == i9) {
            i = (int) (Math.random() * (max - min + 1) + min);
        }
        return i;
    }

    public void setClick() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                iSendItemID.sendItemID(arrItemId[0], 2);
            }
        }, 1000);
    }

    private void readText(final String text) {
        setupTextToSpeech();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mTextToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
            }
        }, 50);
    }

    private void setupTextToSpeech() {
        mTextToSpeech = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                mTextToSpeech.setLanguage(Locale.US);
            }
        });
    }
}

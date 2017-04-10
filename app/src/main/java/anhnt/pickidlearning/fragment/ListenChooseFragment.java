package anhnt.pickidlearning.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import anhnt.pickidlearning.ConstValue;
import anhnt.pickidlearning.R;
import anhnt.pickidlearning.ReadJson;
import anhnt.pickidlearning.models.Item;
import anhnt.pickidlearning.myinterface.ISendItemID;

/**
 * Created by AnhNT on 4/9/2017.
 */

public class ListenChooseFragment extends Fragment implements View.OnClickListener {
    private ImageView mImgImage1;
    private ImageView mImgImage2;
    private ImageView mImgImage3;
    private ImageView mImgImage4;
    private ImageView mImgPlaySound;
    private ImageView mImgHelpPhon;
    private TextView mTvWord;
    private View mViewRight1;
    private View mViewWrong1;
    private View mViewRight2;
    private View mViewWrong2;
    private View mViewRight3;
    private View mViewWrong3;
    private View mViewRight4;
    private View mViewWrong4;
    private Bundle mBundle;
    private int[] arrItemId;
    private List<Item> items;
    private int resID;
    private int mItemID1 = -1;
    private int mItemID2 = -1;
    private int mItemID3 = -1;
    private int mItemID4 = -1;
    private Handler mHandler;
    private ISendItemID iSendItemID;
    private TextToSpeech mTextToSpeech;
    private String mWord;
    private int practicId;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.iSendItemID = (ISendItemID) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listen_choose, container, false);
        init(view);
        mBundle = getArguments();
        arrItemId = mBundle.getIntArray(ConstValue.ITEM_ID_ARRAY);
        practicId = mBundle.getInt(ConstValue.PRACTIC_ID);
        mItemID1 = getRandom(0, 3, mItemID2, mItemID3, mItemID4);
        mWord = getItems(arrItemId[0] - 1).get(0).getName().toLowerCase().toString();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                readText(mWord);
            }
        }, 500);
        mItemID2 = getRandom(0, 3, mItemID1, mItemID3, mItemID4);
        mItemID3 = getRandom(0, 3, mItemID1, mItemID2, mItemID4);
        mItemID4 = getRandom(0, 3, mItemID1, mItemID3, mItemID2);

        setImage(mItemID1, mImgImage1);
        setImage(mItemID2, mImgImage2);
        setImage(mItemID3, mImgImage3);
        setImage(mItemID4, mImgImage4);

        Log.d("test", "onCreateView: " + mItemID1 + mItemID2 + mItemID3 + mItemID4);

        return view;
    }

    public void setImage(int position, ImageView view) {
        resID = getContext().getResources().getIdentifier(getItems(arrItemId[position] - 1).get(0).getImage().toString(), "drawable", getContext().getPackageName());
        view.setImageDrawable(getContext().getResources().getDrawable(resID));
    }

    public List<Item> getItems(int itemID) {
        items = new ArrayList<>();
        try {
            items.addAll(ReadJson.getItemByItemID(getContext(), itemID));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return items;
    }

    public void init(View view) {
        mImgImage1 = (ImageView) view.findViewById(R.id.img_image_view_1);
        mImgImage1.setOnClickListener(this);
        mImgImage2 = (ImageView) view.findViewById(R.id.img_image_view_2);
        mImgImage2.setOnClickListener(this);
        mImgImage3 = (ImageView) view.findViewById(R.id.img_image_view_3);
        mImgImage3.setOnClickListener(this);
        mImgImage4 = (ImageView) view.findViewById(R.id.img_image_view_4);
        mImgImage4.setOnClickListener(this);
        mImgPlaySound = (ImageView) view.findViewById(R.id.img_play_sound);
        mImgPlaySound.setOnClickListener(this);
        mImgHelpPhon = (ImageView) view.findViewById(R.id.img_help_phon);
        mImgHelpPhon.setOnClickListener(this);
        mTvWord = (TextView) view.findViewById(R.id.tv_word);
        mViewRight1 = view.findViewById(R.id.view_right_1);
        mViewWrong1 = view.findViewById(R.id.view_wrong_1);
        mViewRight2 = view.findViewById(R.id.view_right_2);
        mViewWrong2 = view.findViewById(R.id.view_wrong_2);
        mViewRight3 = view.findViewById(R.id.view_right_3);
        mViewWrong3 = view.findViewById(R.id.view_wrong_3);
        mViewRight4 = view.findViewById(R.id.view_right_4);
        mViewWrong4 = view.findViewById(R.id.view_wrong_4);
        mHandler = new Handler();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_image_view_1:
                if (mItemID1 == 0) {
                    mViewRight1.setVisibility(View.VISIBLE);
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            iSendItemID.sendItemID(arrItemId[0], practicId);
                        }
                    }, 1000);
                } else {
                    mViewWrong1.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.img_image_view_2:
                if (mItemID2 == 0) {
                    mViewRight2.setVisibility(View.VISIBLE);
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            iSendItemID.sendItemID(arrItemId[0], practicId);
                        }
                    }, 1000);
                } else {
                    mViewWrong2.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.img_image_view_3:
                if (mItemID3 == 0) {
                    mViewRight3.setVisibility(View.VISIBLE);
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            iSendItemID.sendItemID(arrItemId[0], practicId);
                        }
                    }, 1000);
                } else {
                    mViewWrong3.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.img_image_view_4:
                if (mItemID4 == 0) {
                    mViewRight4.setVisibility(View.VISIBLE);
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            iSendItemID.sendItemID(arrItemId[0], practicId);
                        }
                    }, 1000);
                } else {
                    mViewWrong4.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.img_play_sound:
                readText(mWord);
                break;
            case R.id.img_help_phon:
                mTvWord.setVisibility(View.VISIBLE);
                mTvWord.setText(mWord);
                break;
        }
    }

    private int getRandom(int min, int max, int i1, int i2, int i3) {
        int i = (int) (Math.random() * (max - min + 1) + min);
        while (i == i1 || i == i2 || i == i3) {
            i = (int) (Math.random() * (max - min + 1) + min);
        }
        return i;
    }

    private void readText(final String text) {
        setupTextToSpeech();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mTextToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
            }
        }, 500);
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

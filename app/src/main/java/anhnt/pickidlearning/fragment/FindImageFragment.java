package anhnt.pickidlearning.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
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
import java.util.List;

import anhnt.pickidlearning.ConstValue;
import anhnt.pickidlearning.R;
import anhnt.pickidlearning.ReadJson;
import anhnt.pickidlearning.models.Item;
import anhnt.pickidlearning.myinterface.ISendItemID;

/**
 * Created by AnhNT on 4/9/2017.
 */

public class FindImageFragment extends Fragment implements View.OnClickListener {
    private ImageView mImgImage1;
    private ImageView mImgImage2;
    private ImageView mImgImage3;
    private ImageView mImgImage4;
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
    private TextView mTvWord;
    private List<Item> items;
    private int resID;
    private int mItemIdPosition1 = -1;
    private int mItemIdPosition2 = -1;
    private int mItemIdPosition3 = -1;
    private int mItemIdPosition4 = -1;
    private Handler mHandler;
    public ISendItemID iSendItemID;
    private int mCategoryId;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.iSendItemID = (ISendItemID) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find_image, container, false);
        init(view);
        mBundle = getArguments();
        arrItemId = mBundle.getIntArray(ConstValue.ITEM_ID_ARRAY);
        mCategoryId = mBundle.getInt(ConstValue.CATEGORY_ID);
        mTvWord.setText(getItems(arrItemId[0] - 1).getName().toLowerCase().toString());
        mItemIdPosition1 = getRandom(0, 3, mItemIdPosition2, mItemIdPosition3, mItemIdPosition4);
        mItemIdPosition2 = getRandom(0, 3, mItemIdPosition1, mItemIdPosition3, mItemIdPosition4);
        mItemIdPosition3 = getRandom(0, 3, mItemIdPosition1, mItemIdPosition2, mItemIdPosition4);
        mItemIdPosition4 = getRandom(0, 3, mItemIdPosition1, mItemIdPosition3, mItemIdPosition2);

        setImage(mItemIdPosition1, mImgImage1);
        setImage(mItemIdPosition2, mImgImage2);
        setImage(mItemIdPosition3, mImgImage3);
        setImage(mItemIdPosition4, mImgImage4);

        return view;
    }

    public void setImage(int position, ImageView view) {
        resID = getContext().getResources().getIdentifier(getItems(arrItemId[position] - 1).getImage().toString(), "drawable", getContext().getPackageName());
        view.setImageDrawable(getContext().getResources().getDrawable(resID));
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

    public void init(View view) {
        mImgImage1 = (ImageView) view.findViewById(R.id.img_image_view_1);
        mImgImage1.setOnClickListener(this);
        mImgImage2 = (ImageView) view.findViewById(R.id.img_image_view_2);
        mImgImage2.setOnClickListener(this);
        mImgImage3 = (ImageView) view.findViewById(R.id.img_image_view_3);
        mImgImage3.setOnClickListener(this);
        mImgImage4 = (ImageView) view.findViewById(R.id.img_image_view_4);
        mImgImage4.setOnClickListener(this);
        mViewRight1 = view.findViewById(R.id.view_right_1);
        mViewWrong1 = view.findViewById(R.id.view_wrong_1);
        mViewRight2 = view.findViewById(R.id.view_right_2);
        mViewWrong2 = view.findViewById(R.id.view_wrong_2);
        mViewRight3 = view.findViewById(R.id.view_right_3);
        mViewWrong3 = view.findViewById(R.id.view_wrong_3);
        mViewRight4 = view.findViewById(R.id.view_right_4);
        mViewWrong4 = view.findViewById(R.id.view_wrong_4);
        mTvWord = (TextView) view.findViewById(R.id.tv_word);
        mHandler = new Handler();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_image_view_1:
                setClick(mItemIdPosition1, mViewRight1, mViewWrong1);
                break;
            case R.id.img_image_view_2:
                setClick(mItemIdPosition2, mViewRight2, mViewWrong2);
                break;
            case R.id.img_image_view_3:
                setClick(mItemIdPosition3, mViewRight3, mViewWrong3);
                break;
            case R.id.img_image_view_4:
                setClick(mItemIdPosition4, mViewRight4, mViewWrong4);
                break;
        }
    }

    public void setClick(int itemID, View imageViewRight, View imageViewWrong) {
        if (itemID == 0) {
            imageViewRight.setVisibility(View.VISIBLE);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    iSendItemID.sendItemID(arrItemId[0], 4);
                }
            }, 1000);
        } else {
            imageViewWrong.setVisibility(View.VISIBLE);
        }
    }

    private int getRandom(int min, int max, int i1, int i2, int i3) {
        int i = (int) (Math.random() * (max - min + 1) + min);
        while (i == i1 || i == i2 || i == i3) {
            i = (int) (Math.random() * (max - min + 1) + min);
        }
        return i;
    }

}

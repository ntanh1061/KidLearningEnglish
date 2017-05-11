package anhnt.pickidlearning.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.SynthesisCallback;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import anhnt.pickidlearning.ConstValue;
import anhnt.pickidlearning.R;
import anhnt.pickidlearning.ReadJson;
import anhnt.pickidlearning.fragment.ChooseWordFragment;
import anhnt.pickidlearning.fragment.FindImageFragment;
import anhnt.pickidlearning.fragment.ListenChooseFragment;
import anhnt.pickidlearning.fragment.ListenWriteFragment;
import anhnt.pickidlearning.fragment.WriteWordFragment;
import anhnt.pickidlearning.models.Item;
import anhnt.pickidlearning.myinterface.ISendItemID;

/**
 * Created by AnhNT on 4/26/2017.
 */

public class PracticActivity extends AppCompatActivity implements ISendItemID, View.OnClickListener {
    private Fragment mFindImageFragment;
    private Fragment mListenChooseFragment;
    private ListenWriteFragment mListenWriteFragment;
    private ChooseWordFragment mChooseWordFragment;
    private WriteWordFragment mWriteWordFragment;
    private Bundle mBundle;
    private int[] mArrItemId = new int[4];
    private int mCategoryId;
    private int mPracticId;
    private int mItemID1 = 1;
    private int mItemID2 = 2;
    private int mItemID3 = 3;
    private int mItemID4 = 4;
    private int pos1 = 0;
    private int pos2 = 0;
    private int pos3 = 0;
    private int pos4 = 0;
    private List<Item> items;
    private Toolbar mToolbar;
    private RelativeLayout mRelativeLayout;
    private ImageView mImgHome;
    private ImageView mImgNext;
    private ImageView mImgReplay;
    private String mPracticName;
    private String mCategoryName;
    private Handler mHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practic);
        init();
        getItems(mCategoryId);
        setFragment(mPracticId);
        setupToolbar();
        setTitleToolbar();
    }

    public void setFragment(int practicId) {
        setmArrItemId();
        switch (practicId) {
            case 1:
                mPracticName = "Write Word";
                mWriteWordFragment = new WriteWordFragment();
                switchFragment(mWriteWordFragment);
                break;
            case 2:
                mPracticName = "Listen & Write";
                mListenWriteFragment = new ListenWriteFragment();
                switchFragment(mListenWriteFragment);
                break;
            case 3:
                mPracticName = "Listen & Choose";
                mListenChooseFragment = new ListenChooseFragment();
                switchFragment(mListenChooseFragment);
                break;
            case 4:
                mPracticName = "Find Image";
                mFindImageFragment = new FindImageFragment();
                switchFragment(mFindImageFragment);
                break;
            case 5:
                mPracticName = "Choose Wrod";
                mChooseWordFragment = new ChooseWordFragment();
                switchFragment(mChooseWordFragment);
                break;
        }
    }

    public void init() {
        mRelativeLayout = (RelativeLayout) findViewById(R.id.relative_finish);
        Bundle bundle = getIntent().getExtras();
        mItemID1 = bundle.getInt(ConstValue.ITEM_ID, 1);
        mCategoryId = bundle.getInt(ConstValue.CATEGORY_ID, 1);
        mPracticId = bundle.getInt(ConstValue.PRACTIC_ID, 1);
        mCategoryName = bundle.getString(ConstValue.SEND_CATEGORY_NAME);
        mImgHome = (ImageView) findViewById(R.id.img_home);
        mImgNext = (ImageView) findViewById(R.id.img_next);
        mImgReplay = (ImageView) findViewById(R.id.img_replay);
        mImgHome.setOnClickListener(this);
        mImgNext.setOnClickListener(this);
        mImgReplay.setOnClickListener(this);
        mHandler = new Handler();
    }

    public void setmArrItemId() {
        pos2 = getRandom(0, items.size() - 1, pos1, pos3, pos4);
        pos3 = getRandom(0, items.size() - 1, pos1, pos2, pos4);
        pos4 = getRandom(0, items.size() - 1, pos2, pos3, pos1);

        mItemID2 = items.get(pos2).getId();
        mItemID3 = items.get(pos3).getId();
        mItemID4 = items.get(pos4).getId();

        mArrItemId[0] = mItemID1;
        mArrItemId[1] = mItemID2;
        mArrItemId[2] = mItemID3;
        mArrItemId[3] = mItemID4;

        Log.d("Main", "setmArrItemId: " + mItemID1 + ":" + mItemID2 + ":" + mItemID3 + ":" + mItemID4 + ":");
    }

    @Override
    public void sendItemID(int itemID, int practicId) {
        mItemID1 = itemID;
        mItemID1++;
        pos1++;
        setmArrItemId();
        if (mItemID1 == items.get(items.size() - 1).getId()) {
            mRelativeLayout.setVisibility(View.VISIBLE);
        } else {
            switch (practicId) {
                case 1:
                    mWriteWordFragment = new WriteWordFragment();
                    switchFragment(mWriteWordFragment);
                    break;
                case 2:
                    mListenWriteFragment = new ListenWriteFragment();
                    switchFragment(mListenWriteFragment);
                    break;
                case 3:
                    mListenChooseFragment = new ListenChooseFragment();
                    switchFragment(mListenChooseFragment);
                    break;
                case 4:
                    mFindImageFragment = new FindImageFragment();
                    switchFragment(mFindImageFragment);
                    break;
                case 5:
                    mChooseWordFragment = new ChooseWordFragment();
                    switchFragment(mChooseWordFragment);
            }
        }

    }

    private int getRandom(int min, int max, int i1, int i2, int i3) {
        int i = (int) (Math.random() * (max - min + 1) + min);
        while (i == i1 || i == i2 || i == i3) {
            i = (int) (Math.random() * (max - min + 1) + min);
        }
        return i;
    }

    private void switchFragment(Fragment fragment) {
        mBundle = new Bundle();
        mBundle.putIntArray(ConstValue.ITEM_ID_ARRAY, mArrItemId);
        mBundle.putInt(ConstValue.CATEGORY_ID, 1);
        fragment.setArguments(mBundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fr_container, fragment).commit();
    }

    public void setupToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_action_back);
    }

    private void getItems(int categoryId) {
        items = new ArrayList<>();
        try {
            items.addAll(ReadJson.readItemById(this, categoryId));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setTitleToolbar() {
        getSupportActionBar().setTitle(mPracticName);
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setSubtitle(mCategoryName);
        mToolbar.setSubtitleTextColor(Color.WHITE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_home:
                finish();
                break;
            case R.id.img_next:
                actionPracticActivity(mPracticId, mCategoryId + 1);
                mRelativeLayout.setVisibility(View.GONE);
                break;
            case R.id.img_replay:
                actionPracticActivity(mPracticId, mCategoryId);
                mRelativeLayout.setVisibility(View.GONE);
                break;
        }
    }

    public void actionPracticActivity(int practicId, int categoryId) {
        getItems(categoryId);
        Bundle bundle = new Bundle();
        Intent intent = new Intent(PracticActivity.this, PracticActivity.class);
        bundle.putInt(ConstValue.ITEM_ID, items.get(0).getId());
        bundle.putInt(ConstValue.CATEGORY_ID, categoryId);
        bundle.putInt(ConstValue.PRACTIC_ID, practicId);
        bundle.putString(ConstValue.SEND_CATEGORY_NAME, mCategoryName);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public Item getItem(int itemID) {
        Item item = null;
        try {
            item = ReadJson.getItemByItemID(this, itemID);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return item;
    }
}

package anhnt.pickidlearning.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import anhnt.pickidlearning.ConstValue;
import anhnt.pickidlearning.R;
import anhnt.pickidlearning.ReadJson;
import anhnt.pickidlearning.fragment.FindImageFragment;
import anhnt.pickidlearning.fragment.ListenChooseFragment;
import anhnt.pickidlearning.models.Category;
import anhnt.pickidlearning.models.Item;
import anhnt.pickidlearning.myinterface.ISendItemID;

/**
 * Created by AnhNT on 4/9/2017.
 */

public class PracticActivity extends AppCompatActivity implements ISendItemID {
    private Toolbar mToolbar;
    private String mCategory;
    private int mCategoryID;
    private List<Category> categories;
    private List<Item> items;
    private Fragment mFindImageFragment;
    private Fragment mListenChooseFragment;
    private Bundle mBundle;
    private int[] mArrItemId = new int[4];
    private int mItemID1;
    private int mItemID2;
    private int mItemID3;
    private int mItemID4;
    private int pos1 = 0;
    private int pos2 = 0;
    private int pos3 = 0;
    private int pos4 = 0;
    private String mPracticName;
    private int mPracticId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practic);
        mCategoryID = getIntent().getIntExtra(ConstValue.CATEGORY_ID, 0);
        mPracticId = getIntent().getIntExtra(ConstValue.PRACTIC_ID, 0);
        mPracticName = getIntent().getStringExtra(ConstValue.PRACTIC_NAME);
        getCategories();
        mCategory = categories.get(mCategoryID - 1).getName();
        getItems();
        mItemID1 = items.get(0).getId();
        setupToolbar();

        switch (mPracticId) {
            case 5:
                mListenChooseFragment = new ListenChooseFragment();
                setmArrItemId();
                setupFragment(mListenChooseFragment);
                break;
            case 6:
                mFindImageFragment = new FindImageFragment();
                setmArrItemId();
                setupFragment(mFindImageFragment);
                break;
        }

    }

    private void setupFragment(Fragment fragment) {
        mBundle = new Bundle();
        mBundle.putIntArray(ConstValue.ITEM_ID_ARRAY, mArrItemId);
        mBundle.putInt(ConstValue.PRACTIC_ID, mPracticId);
        fragment.setArguments(mBundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fr_container, fragment)
                .commit();
    }

    private void getItems() {
        items = new ArrayList<>();
        try {
            items.addAll(ReadJson.readItemById(this, mCategoryID));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getCategories() {
        categories = new ArrayList<>();
        try {
            categories.addAll(ReadJson.readCategory(this));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setupToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_action_back);
        getSupportActionBar().setTitle(mPracticName);
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setSubtitle(mCategory);
        mToolbar.setSubtitleTextColor(Color.WHITE);
    }

    private int getRandom(int min, int max, int i1, int i2, int i3) {
        int i = (int) (Math.random() * (max - min + 1) + min);
        while (i == i1 || i == i2 || i == i3) {
            i = (int) (Math.random() * (max - min + 1) + min);
        }
        return i;
    }

    @Override
    public void sendItemID(int itemID, int practicId) {
        mItemID1 = itemID;
        if (mItemID1 == items.get(items.size() - 1).getId()) {
            finish();
        }
        mItemID1++;
        pos1++;
        setmArrItemId();
        switch (practicId) {
            case 5:
                mListenChooseFragment = new ListenChooseFragment();
                setupFragment(mListenChooseFragment);
                break;
            case 6:
                mFindImageFragment = new FindImageFragment();
                setupFragment(mFindImageFragment);
                break;
        }

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
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}

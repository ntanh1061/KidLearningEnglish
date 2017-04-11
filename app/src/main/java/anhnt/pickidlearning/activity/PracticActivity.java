package anhnt.pickidlearning.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import anhnt.pickidlearning.ConstValue;
import anhnt.pickidlearning.R;
import anhnt.pickidlearning.ReadJson;
import anhnt.pickidlearning.adapter.DetailPagerAdapter;
import anhnt.pickidlearning.adapter.RecyclerViewAdapter;
import anhnt.pickidlearning.fragment.FindImageFragment;
import anhnt.pickidlearning.fragment.ListenChooseFragment;
import anhnt.pickidlearning.models.Category;
import anhnt.pickidlearning.models.Item;
import anhnt.pickidlearning.myinterface.ISendItemID;

/**
 * Created by AnhNT on 4/9/2017.
 */

public class PracticActivity extends AppCompatActivity implements ISendItemID, View.OnClickListener {
    private Toolbar mToolbar;
    private int mCategoryId;
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ;
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
    private int mPracticId;
    private String mCategoryName;
    private String mPracticName;
    private ImageView mImgListenWrite;
    private ImageView mImgWriteWord;
    private ImageView mImgVocabulary;
    private ImageView mImgChooseWord;
    private ImageView mImgListenChoose;
    private ImageView mImgFindImage;
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mToggle;
    private RelativeLayout mRelativeLayout;
    private ImageView mImgHome;
    private ImageView mImgNext;
    private ImageView mImgReplay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practic);
        init();
        getCategories();
        setupToolbar();
        setupRecyclerView();
    }

    public void init() {
        mImgHome = (ImageView) findViewById(R.id.img_home);
        mImgHome.setOnClickListener(this);
        mImgNext = (ImageView) findViewById(R.id.img_next);
        mImgNext.setOnClickListener(this);
        mImgReplay = (ImageView) findViewById(R.id.img_replay);
        mImgReplay.setOnClickListener(this);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mImgChooseWord = (ImageView) findViewById(R.id.img_choose_word);
        mImgChooseWord.setOnClickListener(this);
        mImgFindImage = (ImageView) findViewById(R.id.img_find_image);
        mImgFindImage.setOnClickListener(this);
        mImgListenChoose = (ImageView) findViewById(R.id.img_listen_choose);
        mImgListenChoose.setOnClickListener(this);
        mImgVocabulary = (ImageView) findViewById(R.id.img_vocabulary);
        mImgVocabulary.setOnClickListener(this);
        mImgListenWrite = (ImageView) findViewById(R.id.img_listen_write);
        mImgListenWrite.setOnClickListener(this);
        mImgWriteWord = (ImageView) findViewById(R.id.img_write_word);
        mImgWriteWord.setOnClickListener(this);
    }

    public void setupRecyclerView() {
        mAdapter = new RecyclerViewAdapter(this, categories);
        mLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new RecyclerViewAdapter.IOnItemClickListener() {
            @Override
            public void setOnItemClick(int position) {
                mCategoryId = categories.get(position).getId();
                mCategoryName = categories.get(position).getName().toLowerCase().toString();
                getItems(mCategoryId);
                mItemID1 = items.get(0).getId();
                switch (mPracticId) {
                    case 0:
                        Toast.makeText(PracticActivity.this, "Please choose option", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        actionActivity(position);
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        mListenChooseFragment = new ListenChooseFragment();
                        setmArrItemId();
                        mPracticName = "Find Image";
                        setupFragment(mListenChooseFragment);
                        setTitleToolbar();
                        break;
                    case 6:
                        mFindImageFragment = new FindImageFragment();
                        setmArrItemId();
                        mPracticName = "Listen & Choose";
                        setupFragment(mFindImageFragment);
                        setTitleToolbar();
                        break;
                }
            }
        });
    }

    public void setTitleToolbar(){
        getSupportActionBar().setTitle(mPracticName);
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setSubtitle(mCategoryName);
        mToolbar.setSubtitleTextColor(Color.WHITE);
    }

    public void actionActivity(int position) {
        Bundle bundle = new Bundle();
        Intent intent = new Intent(PracticActivity.this, VocabularyActivity.class);
        bundle.putInt(ConstValue.SENDDATA, position);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void setupFragment(Fragment fragment) {
        mBundle = new Bundle();
        mBundle.putIntArray(ConstValue.ITEM_ID_ARRAY, mArrItemId);
        mBundle.putInt(ConstValue.PRACTIC_ID, mPracticId);
        fragment.setArguments(mBundle);
        getSupportFragmentManager().beginTransaction()
                .addToBackStack(null)
                .replace(R.id.fr_container, fragment)
                .commit();
    }

    private void getItems(int categoryID) {
        items = new ArrayList<>();
        try {
            items.addAll(ReadJson.readItemById(this, categoryID));
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
        mToolbar.setTitleTextColor(Color.WHITE);
        mToggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.setDrawerListener(mToggle);
        mToggle.syncState();
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
            mRelativeLayout.setVisibility(View.VISIBLE);
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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
        setBackgroundDefault(mImgChooseWord);
        setBackgroundDefault(mImgFindImage);
        setBackgroundDefault(mImgListenWrite);
        setBackgroundDefault(mImgListenChoose);
        setBackgroundDefault(mImgVocabulary);
        setBackgroundDefault(mImgWriteWord);

        switch (v.getId()) {
            case R.id.img_choose_word:
                setBackgroundImageView(mImgChooseWord);
                mPracticId = 2;
                break;
            case R.id.img_find_image:
                setBackgroundImageView(mImgFindImage);
                mPracticId = 6;
                break;
            case R.id.img_listen_choose:
                setBackgroundImageView(mImgListenChoose);
                mPracticId = 5;
                break;
            case R.id.img_vocabulary:
                setBackgroundImageView(mImgVocabulary);
                mPracticId = 1;
                break;
            case R.id.img_listen_write:
                setBackgroundImageView(mImgListenWrite);
                mPracticId = 3;
                break;
            case R.id.img_write_word:
                setBackgroundImageView(mImgWriteWord);
                mPracticId = 4;
                break;
            case R.id.img_home:
                startActivity(new Intent(PracticActivity.this, PracticActivity.class));
                break;
            case R.id.img_replay:
                mRelativeLayout.setVisibility(View.GONE);
//                mViewPager.setCurrentItem(0);
                break;
            case R.id.img_next:
//                mProgressBar.setProgress(0);
//                mItems.clear();
//                if (mCategoryId < 9) {
//                    mCategoryId++;
//                    position++;
//                    try {
//                        getItems(mCategoryId);
//                        mPagerAdapter = new DetailPagerAdapter(this, mItems);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    mCategoryId = 1;
//                    position = 0;
//                    try {
//                        getItems(mCategoryId);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    mPagerAdapter = new DetailPagerAdapter(this, mItems);
//                }
//                mCategoryName = categories.get(position).getName();
//                setTitleToolbar(mCategoryName);
//                mViewPager.setAdapter(mPagerAdapter);
//                mRelativeLayout.setVisibility(View.GONE);
                break;
        }
    }

    public void setBackgroundImageView(ImageView imageView) {
        imageView.setBackgroundColor(Color.parseColor("#FFC107"));
    }

    public void setBackgroundDefault(ImageView imageView) {
        imageView.setBackgroundColor(Color.parseColor("#FFFFFF"));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}

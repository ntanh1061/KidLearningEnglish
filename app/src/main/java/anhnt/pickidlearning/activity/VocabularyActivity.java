package anhnt.pickidlearning.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import anhnt.pickidlearning.FinalValue;
import anhnt.pickidlearning.R;
import anhnt.pickidlearning.ReadJson;
import anhnt.pickidlearning.adapter.DetailPagerAdapter;
import anhnt.pickidlearning.adapter.RecyclerViewVocabularyAdapter;
import anhnt.pickidlearning.models.Category;
import anhnt.pickidlearning.models.Item;

/**
 * Created by Administrator on 2/13/2017.
 */

public class VocabularyActivity extends AppCompatActivity implements View.OnClickListener {
    private ViewPager mViewPager;
    private ProgressBar mProgressBar;
    private DetailPagerAdapter mPagerAdapter;
    private List<Item> mItems;
    private List<Item> mAllItems;
    private Bundle mBundle;
    private int mCategoryId;
    private int mPositionItem;
    private TextToSpeech mTextToSpeech;
    private Handler mHandler;
    private Boolean mCheckPlay = false;
    private Dialog mDialog;
    private Toolbar mToolbar;
    private String mCategoryName;
    private RecyclerView recyclerView;
    private RecyclerViewVocabularyAdapter mAdapter;
    private LinearLayoutManager layoutManager;
    private List<Category> categories;
    private int position;
    private int row = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulary);
        init();
        getCategoryId();
        setupToolbar();
        setViewPager();
        setChoiceText();
        readText(mItems.get(mPositionItem).getName().toString().toLowerCase());
        mProgressBar.setMax(mItems.size());

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            //// TODO: 3/2/2017
            @Override
            public void onPageSelected(int position) {
                layoutManager.scrollToPositionWithOffset(position, 0);
                mAdapter.notifyDataSetChanged();
                setChoiceText();
                mProgressBar.setProgress(position);
                mTextToSpeech.speak(mItems.get(mPositionItem).getName().toString().toLowerCase(), TextToSpeech.QUEUE_FLUSH, null);
                Log.d("Test", "onPageSelected: " + position + " " + (mItems.size() - 1));
                if (position == (mItems.size() - 1)) {
                    mProgressBar.setProgress(mPositionItem + 1);
                    showDialog();
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mAdapter = new RecyclerViewVocabularyAdapter(this, mItems);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClick(new RecyclerViewVocabularyAdapter.IOnClickListener() {
            @Override
            public void setOnItemClick(int position) {
                mViewPager.setCurrentItem(position);
                layoutManager.scrollToPositionWithOffset(position, 0);
                mAdapter.notifyDataSetChanged();
            }
        });

    }

    private void setChoiceText() {
        mPositionItem = mViewPager.getCurrentItem();
    }

    private int getRandom(int min, int max) {
        return (int) (Math.random() * (max - min + 1) + min);
    }

    private void init() {
        mItems = new ArrayList<>();
        categories = new ArrayList<>();
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mHandler = new Handler();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Vocabulary");
        mToolbar.setTitleTextColor(Color.WHITE);
        setTitleToolbar(mCategoryName);
    }

    public void setTitleToolbar(String category) {
        mToolbar.setSubtitle(category);
        mToolbar.setSubtitleTextColor(Color.WHITE);
    }

    private void setupTextToSpeech() {
        mTextToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                mTextToSpeech.setLanguage(Locale.US);
            }
        });
    }

    private void getItems(int categoryId) throws IOException, JSONException {
        mAllItems = ReadJson.readItem(VocabularyActivity.this);
        for (int i = 0; i < mAllItems.size(); i++) {
            if (mAllItems.get(i).getCategoryId() == categoryId) {
                mItems.add(mAllItems.get(i));
            }
        }
    }

    private void setViewPager() {
        try {
            getItems(mCategoryId);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mPagerAdapter = new DetailPagerAdapter(this, mItems);
        mViewPager.setAdapter(mPagerAdapter);
    }

    private void getCategoryId() {
        mBundle = getIntent().getExtras();
        position = mBundle.getInt(FinalValue.SENDDATA);
        try {
            categories.addAll(ReadJson.readCategory(this));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mCategoryId = categories.get(position).getId();
        mCategoryName = categories.get(position).getName();
//        mCategoryName = mBundle.getString(FinalValue.SEND_CATEGORY_NAME).toLowerCase();
    }

    @Override
    public void onClick(View v) {
        mPositionItem = mViewPager.getCurrentItem();
        switch (v.getId()) {
            case R.id.img_home:
                finish();
                break;
            case R.id.img_replay:
                mDialog.dismiss();
                mViewPager.setCurrentItem(0);
                break;

            //// TODO: 3/2/2017
            case R.id.img_next:
                mItems.clear();
                if (mCategoryId < 9) {
                    mCategoryId++;
                    position++;
                    try {
                        getItems(mCategoryId);
                        mPagerAdapter = new DetailPagerAdapter(this, mItems);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    mCategoryId = 1;
                    position = 0;
                    try {
                        getItems(mCategoryId);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mPagerAdapter = new DetailPagerAdapter(this, mItems);
                }
                mCategoryName = categories.get(position).getName();
                setTitleToolbar(mCategoryName);
                recyclerView.getChildAt(2).setBackgroundColor(Color.BLUE);
                mAdapter.notifyDataSetChanged();
                mDialog.dismiss();
                mViewPager.setAdapter(mPagerAdapter);
                layoutManager.scrollToPositionWithOffset(0, 0);
                mAdapter.notifyDataSetChanged();
                break;
        }
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

    private void showDialog() {
        mDialog = new Dialog(this);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setCancelable(false);
        mDialog.setContentView(R.layout.dialog_finish);
        mDialog.show();

        ImageView imgHome = (ImageView) mDialog.findViewById(R.id.img_home);
        ImageView imgReplay = (ImageView) mDialog.findViewById(R.id.img_replay);
        ImageView imgNext = (ImageView) mDialog.findViewById(R.id.img_next);
        imgNext.setOnClickListener(this);
        imgReplay.setOnClickListener(this);
        imgHome.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_play:
                if (mCheckPlay) {
                    item.setIcon(R.mipmap.ic_play);
                    mCheckPlay = false;
                } else {
                    item.setIcon(R.mipmap.ic_pause);
                    mCheckPlay = true;
                }
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        int position = mViewPager.getCurrentItem();
                        if (position < mItems.size() && mCheckPlay) {
                            mPositionItem++;
                            mViewPager.setCurrentItem(mPositionItem);
                            mHandler.postDelayed(this, 2000);
                        } else {
                            mHandler.removeCallbacks(null);
                        }
                    }
                }, 1500);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}

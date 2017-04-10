package anhnt.pickidlearning.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import anhnt.pickidlearning.ConstValue;
import anhnt.pickidlearning.R;
import anhnt.pickidlearning.ReadJson;
import anhnt.pickidlearning.adapter.DetailPagerAdapter;
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
    private Toolbar mToolbar;
    private String mCategoryName;
    private List<Category> categories;
    private int position;
    private ImageView mImgSpeaker;
    private ImageView mImgReturn;
    private TextView mTvWord;
    private String word;
    private RelativeLayout mRelativeLayout;
    private ImageView mImgHome;
    private ImageView mImgNext;
    private ImageView mImgReplay;
    private Animation mAnimation;
    private ImageView mImgNextPage;
    private ImageView mImgPrevPage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulary);
        init();
        getCategoryId();
        setupToolbar();
        setViewPager();
        setChoiceText();

        word = mItems.get(mPositionItem).getName().toString();
        readText(word);
        mProgressBar.setMax(mItems.size());
        mTvWord.setText(word);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showButtonNextPrev(mPositionItem);
            }
        }, 1500);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mPositionItem = position;
                setChoiceText();
                hideButtonNextPrev();
                mProgressBar.setProgress(position);
                word = mItems.get(mPositionItem).getName().toString();
                mTvWord.setText(word);
                mTextToSpeech.speak(word, TextToSpeech.QUEUE_FLUSH, null);
                Log.d("Test", "onPageSelected: " + position + " " + (mItems.size() - 1));
                if (position == (mItems.size() - 1)) {
//                    mProgressBar.setProgress(mPositionItem + 1);
                    mRelativeLayout.setVisibility(View.VISIBLE);
                } else {
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showButtonNextPrev(mPositionItem);
                        }
                    }, 1500);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

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
        mImgSpeaker = (ImageView) findViewById(R.id.img_speaker);
        mImgReturn = (ImageView) findViewById(R.id.img_return);
        mImgHome = (ImageView) findViewById(R.id.img_home);
        mImgNext = (ImageView) findViewById(R.id.img_next);
        mImgReplay = (ImageView) findViewById(R.id.img_replay);
        mImgNextPage = (ImageView) findViewById(R.id.img_next_page);
        mImgPrevPage = (ImageView) findViewById(R.id.img_prev_page);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.relative_finish);
        mTvWord = (TextView) findViewById(R.id.tv_Word);
        mItems = new ArrayList<>();
        categories = new ArrayList<>();
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mHandler = new Handler();
        mImgSpeaker.setOnClickListener(this);
        mImgReturn.setOnClickListener(this);
        mImgHome.setOnClickListener(this);
        mImgNext.setOnClickListener(this);
        mImgReplay.setOnClickListener(this);
        mImgNextPage.setOnClickListener(this);
        mImgPrevPage.setOnClickListener(this);
        mAnimation = AnimationUtils.loadAnimation(this, R.anim.action_finish);
        mAnimation.setDuration(500);
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Vocabulary");
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_action_back);
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
        position = mBundle.getInt(ConstValue.SENDDATA);
        try {
            categories.addAll(ReadJson.readCategory(this));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mCategoryId = categories.get(position).getId();
        mCategoryName = categories.get(position).getName();
//        mCategoryName = mBundle.getString(ConstValue.SEND_CATEGORY_NAME).toLowerCase();
    }

    @Override
    public void onClick(View v) {
        mPositionItem = mViewPager.getCurrentItem();
        switch (v.getId()) {
            case R.id.img_home:
                finish();
                break;
            case R.id.img_replay:
                mRelativeLayout.setVisibility(View.GONE);
                mViewPager.setCurrentItem(0);
                break;
            case R.id.img_speaker:
                readText(word);
                break;
            case R.id.img_return:
                changeViewPager(0);
                break;
            case R.id.img_next_page:
                mPositionItem++;
                changeViewPager(mPositionItem);
                break;
            case R.id.img_prev_page:
                mPositionItem--;
                changeViewPager(mPositionItem);
                break;
            case R.id.img_next:
                mProgressBar.setProgress(0);
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
                mViewPager.setAdapter(mPagerAdapter);
                mRelativeLayout.setVisibility(View.GONE);
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
                            changeViewPager(mPositionItem);
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

    private void showButtonNextPrev(int position) {
        mImgNextPage.setVisibility(View.VISIBLE);
        if (position != 0) {
            mImgPrevPage.setVisibility(View.VISIBLE);
        }
    }

    private void hideButtonNextPrev() {
        mImgNextPage.setVisibility(View.GONE);
        mImgPrevPage.setVisibility(View.GONE);
    }

    private void changeViewPager(int position) {
        mViewPager.setCurrentItem(position);
        mPagerAdapter.notifyDataSetChanged();
    }
}

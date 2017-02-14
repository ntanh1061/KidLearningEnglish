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
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import anhnt.pickidlearning.FinalValue;
import anhnt.pickidlearning.R;
import anhnt.pickidlearning.ReadJson;
import anhnt.pickidlearning.adapter.DetailPagerAdapter;
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
    private TextView mTvName;
    private ImageView mImgPlayPause;
    private ImageView mImgSpeaker;
    private Bundle mBundle;
    private int mCategoryId;
    private int mPositionItem;
    private TextToSpeech mTextToSpeech;
    private Handler mHandler;
    private Boolean mCheckPlay = false;
    private Dialog mDialog;
    private Toolbar mToolbar;
    private String mCategoryName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
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

            @Override
            public void onPageSelected(int position) {
                setChoiceText();
                mProgressBar.setProgress(position);
                mTextToSpeech.speak(mItems.get(mPositionItem).getName().toString().toLowerCase(), TextToSpeech.QUEUE_FLUSH, null);
                if (position == (mItems.size() - 1)) {
                    mProgressBar.setProgress(mPositionItem + 1);
                    showDialog();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mImgPlayPause.setOnClickListener(VocabularyActivity.this);
        mImgSpeaker.setOnClickListener(VocabularyActivity.this);
    }

    private void setChoiceText() {
        mPositionItem = mViewPager.getCurrentItem();
        String currentWord = mItems.get(mPositionItem).getName().toString().toLowerCase();
        mTvName.setText(currentWord);
    }

    private int getRandom(int min, int max) {
        return (int) (Math.random() * (max - min + 1) + min);
    }

    private void init() {
        mItems = new ArrayList<>();
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mTvName = (TextView) findViewById(R.id.tvName);
        mImgPlayPause = (ImageView) findViewById(R.id.img_play_pause);
        mImgSpeaker = (ImageView) findViewById(R.id.img_speaker);
        mHandler = new Handler();
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Vocabulary");
//        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setSubtitle(mCategoryName);
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

    private void getItems() throws IOException, JSONException {
        mAllItems = ReadJson.readItem(VocabularyActivity.this);
        for (int i = 0; i < mAllItems.size(); i++) {
            if (mAllItems.get(i).getCategoryId() == mCategoryId) {
                mItems.add(mAllItems.get(i));
            }
        }
    }

    private void setViewPager() {
        try {
            getItems();
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
        mCategoryId = mBundle.getInt(FinalValue.SENDDATA);
        mCategoryName = mBundle.getString(FinalValue.SEND_CATEGORY_NAME).toLowerCase();
    }

    @Override
    public void onClick(View v) {
        mPositionItem = mViewPager.getCurrentItem();
        switch (v.getId()) {
            case R.id.img_play_pause:
                if (mCheckPlay) {
                    mImgPlayPause.setImageResource(R.mipmap.ic_play);
                    mCheckPlay = false;
                } else {
                    mImgPlayPause.setImageResource(R.mipmap.ic_pause);
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
            case R.id.img_speaker:
                readText(mItems.get(mPositionItem).getName().toString().toLowerCase());
                break;
            case R.id.img_home:
                finish();
                break;
            case R.id.img_replay:
                mDialog.dismiss();
                mViewPager.setCurrentItem(0);
                break;
            case R.id.img_next:
                if (mCategoryId < 9) {
                    Intent intent = new Intent(VocabularyActivity.this, VocabularyActivity.class);
                    Bundle bundle1 = new Bundle();
                    bundle1.putInt(FinalValue.SENDDATA, mCategoryId + 1);
                    intent.putExtras(bundle1);
                    startActivity(intent);
                    finish();
                } else {
                    finish();
                }
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
}

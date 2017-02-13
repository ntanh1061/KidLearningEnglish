package anhnt.pickidlearning.activity;

import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

public class DetailsActivity extends AppCompatActivity implements View.OnClickListener {
    private ViewPager mViewPager;
    private ProgressBar mProgressBar;
    private DetailPagerAdapter mPagerAdapter;
    private List<Item> items;
    private List<Item> allItems;
    private TextView mTvName;
    private ImageView mImgMicro;
    private ImageView mImgSpeaker;
    private Bundle bundle;
    private int categoryId;
    private int positionItem;
    private TextToSpeech mTextToSpeech;
    private Handler mHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        init();
        getCategoryId();
        setViewPager();
        setChoiceText();
        setupTextToSpeech();
        mTextToSpeech.speak(items.get(positionItem).getName().toString().toLowerCase(), TextToSpeech.QUEUE_FLUSH, null);
        mProgressBar.setMax(items.size());

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setChoiceText();
                mProgressBar.setProgress(position);
                mTextToSpeech.speak(items.get(positionItem).getName().toString().toLowerCase(), TextToSpeech.QUEUE_FLUSH, null);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mImgMicro.setOnClickListener(DetailsActivity.this);
        mImgSpeaker.setOnClickListener(DetailsActivity.this);
    }

    private void setChoiceText() {
        positionItem = mViewPager.getCurrentItem();
        String currentWord = items.get(positionItem).getName().toString().toLowerCase();
        mTvName.setText(currentWord);
    }

    private int getRandom(int min, int max) {
        return (int) (Math.random() * (max - min + 1) + min);
    }

    private void init() {
        items = new ArrayList<>();
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mTvName = (TextView) findViewById(R.id.tvName);
        mImgMicro = (ImageView) findViewById(R.id.img_micro);
        mImgSpeaker = (ImageView) findViewById(R.id.img_speaker);
        mHandler = new Handler();
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
        allItems = ReadJson.readItem(DetailsActivity.this);
        for (int i = 0; i < allItems.size(); i++) {
            if (allItems.get(i).getCategoryId() == categoryId) {
                items.add(allItems.get(i));
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
        mPagerAdapter = new DetailPagerAdapter(this, items);
        mViewPager.setAdapter(mPagerAdapter);
    }

    private void getCategoryId() {
        bundle = getIntent().getExtras();
        categoryId = bundle.getInt(FinalValue.SENDDATA);
    }

    @Override
    public void onClick(View v) {
        positionItem = mViewPager.getCurrentItem();
        switch (v.getId()) {
            case R.id.img_micro:
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        int position = mViewPager.getCurrentItem();
                        if (position<items.size()){
                            positionItem++;
                            mViewPager.setCurrentItem(positionItem);
                            mHandler.postDelayed(this,2000);
                        } else {
                            mHandler.removeCallbacks(null);
                        }
                    }
                },1000);
                break;
            case R.id.img_speaker:
                mTextToSpeech.speak(items.get(positionItem).getName().toString().toLowerCase(), TextToSpeech.QUEUE_FLUSH, null);
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mTextToSpeech.speak(items.get(positionItem).getName().toString().toLowerCase(), TextToSpeech.QUEUE_FLUSH, null);
    }
}

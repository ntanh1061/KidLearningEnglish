package anhnt.pickidlearning.activity;

import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

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
    private Button mBtnChose1;
    private Button mBtnChose2;
    private Bundle bundle;
    private int categoryId;
    private int positionItem;
    private int positionRandom;
    private int random;
    private TextToSpeech mTextToSpeech;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        init();
        getCategoryId();
        setViewPager();
        setChoiceText();
        mProgressBar.setMax(items.size());

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setChoiceText();
                mProgressBar.setProgress(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setChoiceText() {
        positionItem = mViewPager.getCurrentItem();
        positionRandom = getRandom(0, items.size());
        random = getRandom(1, 2);
        mBtnChose1.setBackgroundColor(getResources().getColor(R.color.button_color_normal));
        mBtnChose2.setBackgroundColor(getResources().getColor(R.color.button_color_normal));
        if (random == 1) {
            mBtnChose1.setText(items.get(positionItem).getName().toString().toLowerCase());
            mBtnChose2.setText(items.get(positionRandom).getName().toString().toLowerCase());
        } else {
            mBtnChose2.setText(items.get(positionItem).getName().toString().toLowerCase());
            mBtnChose1.setText(items.get(positionRandom).getName().toString().toLowerCase());
        }
    }

    private int getRandom(int min, int max) {
        return (int) (Math.random() * (max - min + 1) + min);
    }

    private void init() {
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mBtnChose1 = (Button) findViewById(R.id.btn_chose_1);
        mBtnChose2 = (Button) findViewById(R.id.btn_chose_2);
        mBtnChose1.setOnClickListener(this);
        mBtnChose2.setOnClickListener(this);
        mTextToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    mTextToSpeech.setLanguage(Locale.UK);
                }
            }
        });
        items = new ArrayList<>();


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
        String currentWord1 = mBtnChose1.getText().toString().toUpperCase();
        String currentWord2 = mBtnChose2.getText().toString().toUpperCase();
        switch (v.getId()) {
            case R.id.btn_chose_1:
                if (currentWord1.equals(items.get(positionItem).getName().toString())) {
                    mBtnChose1.setBackgroundColor(getResources().getColor(R.color.button_color_right));
                    mTextToSpeech.speak(currentWord1, TextToSpeech.QUEUE_FLUSH, null);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mViewPager.setCurrentItem(positionItem + 1);
                        }
                    }, 1000);
                } else {
                    mBtnChose1.setBackgroundColor(getResources().getColor(R.color.button_color_wrong));
                }
                break;
            case R.id.btn_chose_2:
                if (currentWord2.equals(items.get(positionItem).getName().toString())) {
                    mBtnChose2.setBackgroundColor(getResources().getColor(R.color.button_color_right));
                    mTextToSpeech.speak(currentWord2, TextToSpeech.QUEUE_FLUSH, null);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mViewPager.setCurrentItem(positionItem + 1);
                        }
                    }, 1000);
                } else {
                    mBtnChose2.setBackgroundColor(getResources().getColor(R.color.button_color_wrong));
                }
                break;
        }
    }
}

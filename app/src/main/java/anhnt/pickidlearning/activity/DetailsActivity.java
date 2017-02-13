package anhnt.pickidlearning.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import anhnt.pickidlearning.FinalValue;
import anhnt.pickidlearning.R;
import anhnt.pickidlearning.ReadJson;
import anhnt.pickidlearning.adapter.DetailPagerAdapter;
import anhnt.pickidlearning.models.Item;

/**
 * Created by Administrator on 2/13/2017.
 */

public class DetailsActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private DetailPagerAdapter mPagerAdapter;
    private List<Item> items;
    private List<Item> allItems;
    private Button mBtnChose1;
    private Button mBtnChose2;
    private Bundle bundle;
    private int categoryId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        init();
        bundle = getIntent().getExtras();
        categoryId = bundle.getInt(FinalValue.SENDDATA);
        setViewPager();

        int positionItem = mViewPager.getCurrentItem();
        int positionRandom = getRandom(0, items.size());
        int random = getRandom(1, 2);
        if (random == 1) {
            mBtnChose1.setText(items.get(positionItem).getName().toString());
            mBtnChose2.setText(items.get(positionRandom).getName());
        } else {
            mBtnChose2.setText(items.get(positionItem).getName().toString());
            mBtnChose1.setText(items.get(positionRandom).getName());
        }
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int positionItem = mViewPager.getCurrentItem();
                int positionRandom = getRandom(0, allItems.size());
                int random = getRandom(1, 2);
                if (random == 1) {
                    mBtnChose1.setText(items.get(positionItem).getName().toString());
                    mBtnChose2.setText(allItems.get(positionRandom).getName());
                } else {
                    mBtnChose2.setText(items.get(positionItem).getName().toString());
                    mBtnChose1.setText(allItems.get(positionRandom).getName());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private int getRandom(int min, int max) {
        return (int) (Math.random() * (max - min + 1) + min);
    }

    private void init() {
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mBtnChose1 = (Button) findViewById(R.id.btn_chose_1);
        mBtnChose2 = (Button) findViewById(R.id.btn_chose_2);
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
}

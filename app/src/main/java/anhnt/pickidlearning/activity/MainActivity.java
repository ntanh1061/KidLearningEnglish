package anhnt.pickidlearning.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import anhnt.pickidlearning.ConstValue;
import anhnt.pickidlearning.R;
import anhnt.pickidlearning.ReadJson;
import anhnt.pickidlearning.adapter.RecyclerViewAdapter;
import anhnt.pickidlearning.adapter.RecyclerViewTypeAdapter;
import anhnt.pickidlearning.fragment.FindImageFragment;
import anhnt.pickidlearning.fragment.ListenChooseFragment;
import anhnt.pickidlearning.models.Category;
import anhnt.pickidlearning.models.Item;
import anhnt.pickidlearning.models.Type;
import anhnt.pickidlearning.myinterface.ISendItemID;

/**
 * Created by AnhNT on 4/9/2017.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar mToolbar;
    private int mCategoryId = 1;
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView mRecyclerViewType;
    private RecyclerView.LayoutManager mLayoutManagerType;
    private RecyclerViewTypeAdapter mAdapterType;
    private List<Type> types;
    private List<Category> categories;
    private List<Item> items;
    private int mPracticId;
    private String mCategoryName;
    private int mPosition;
    private ImageView mImgPlay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        getCategories();
        setupToolbar();
        setmRecyclerViewType();
        setupRecyclerView();

    }

    public void getType() {
        String[] image = {"gi_v", "gi_ww", "gi_lw", "gi_lc", "gi_fi", "gi_cw"};
        String[] title = {"Vocabulary", "Write Word", "Listen & Write", "Listen & Choose", "Find Image", "Choose Word"};
        for (int i = 0; i < image.length; i++) {
            Type type = new Type(image[i], title[i]);
            types.add(type);
        }
    }

    public void init() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerViewType = (RecyclerView) findViewById(R.id.recyclerView);
        mImgPlay = (ImageView) findViewById(R.id.img_play);
        mImgPlay.setOnClickListener(this);
        types = new ArrayList<>();
    }

    public void setupRecyclerView() {
        mAdapter = new RecyclerViewAdapter(this, categories);
        mLayoutManager = new GridLayoutManager(this, 2, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new RecyclerViewAdapter.IOnItemClickListener() {
            @Override
            public void setOnItemClick(int position) {
                mCategoryId = categories.get(position).getId();
                mCategoryName = categories.get(position).getName().toLowerCase().toString();
                mPosition = position;
                mLayoutManager.scrollToPosition(position);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    public void actionPracticActivity(int practicId) {
        getItems(mCategoryId);
        Bundle bundle = new Bundle();
        Intent intent = new Intent(MainActivity.this, PracticActivity.class);
        bundle.putInt(ConstValue.ITEM_ID, items.get(0).getId());
        bundle.putInt(ConstValue.CATEGORY_ID, mCategoryId);
        bundle.putInt(ConstValue.PRACTIC_ID, practicId);
        bundle.putString(ConstValue.SEND_CATEGORY_NAME, mCategoryName);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void setmRecyclerViewType() {
        getType();
        mLayoutManagerType = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mAdapterType = new RecyclerViewTypeAdapter(this, types);
        mRecyclerViewType.setLayoutManager(mLayoutManagerType);
        mRecyclerViewType.setAdapter(mAdapterType);
        mAdapterType.setOnItemClickListener(new RecyclerViewTypeAdapter.IOnItemClickListener() {
            @Override
            public void setOnItemClick(int position) {
                mPracticId = position;
                mLayoutManagerType.scrollToPosition(position);
                mAdapterType.notifyDataSetChanged();
            }
        });
    }

    public void actionActivity(int position) {
        Bundle bundle = new Bundle();
        Intent intent = new Intent(MainActivity.this, VocabularyActivity.class);
        bundle.putInt(ConstValue.SENDDATA, position);
        intent.putExtras(bundle);
        startActivity(intent);
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
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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

    @Override
    public void onClick(View v) {

        switch (mPracticId) {
            case 0:
                actionActivity(mPosition);
                break;
            case 1:
                actionPracticActivity(1);
                break;
            case 2:
                actionPracticActivity(2);
                break;
            case 3:
                actionPracticActivity(3);
                break;
            case 4:
                actionPracticActivity(4);
                break;
            case 5:
                actionPracticActivity(5);
                break;
        }
    }
}

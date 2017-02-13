package anhnt.pickidlearning.activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import anhnt.pickidlearning.FinalValue;
import anhnt.pickidlearning.R;
import anhnt.pickidlearning.ReadJson;
import anhnt.pickidlearning.adapter.DetailPagerAdapter;
import anhnt.pickidlearning.adapter.RecyclerViewAdapter;
import anhnt.pickidlearning.models.Category;
import anhnt.pickidlearning.models.Item;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.IOnItemClickListener {
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;
    private List<Category> categories;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        try {
            setmRecyclerView();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        categories = new ArrayList<>();
    }

    private void setmRecyclerView() throws IOException, JSONException {
        getCategories();
        mAdapter = new RecyclerViewAdapter(this, categories);
        mLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(MainActivity.this);
    }

    private void getCategories() throws IOException, JSONException {
        categories.addAll(ReadJson.readCategory(MainActivity.this));

    }

    @Override
    public void setOnItemClick(int position) {
        actionIntent(position);
    }

    private void actionIntent(int position){
        Intent intent = new Intent(MainActivity.this,DetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(FinalValue.SENDDATA,categories.get(position).getId());
        intent.putExtras(bundle);
        startActivity(intent);
    }
}

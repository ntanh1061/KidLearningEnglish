package anhnt.pickidlearning.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import anhnt.pickidlearning.FinalValue;
import anhnt.pickidlearning.R;
import anhnt.pickidlearning.ReadJson;
import anhnt.pickidlearning.adapter.RecyclerViewAdapter;
import anhnt.pickidlearning.adapter.RecyclerViewHomeAdapter;
import anhnt.pickidlearning.models.Category;

/**
 * Created by AnhNT on 2/15/2017.
 */

public class HomeActivity extends AppCompatActivity {
    private List<Category> mTypeList;
    private RecyclerViewHomeAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();
        getTypeList();
        setRecyclerView();
        mAdapter.setOnItemClick(new RecyclerViewAdapter.IOnItemClickListener() {
            @Override
            public void setOnItemClick(int position) {
                Bundle bundle = new Bundle();
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                bundle.putInt(FinalValue.SEND_TYPE_ID, mTypeList.get(position).getId());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void setRecyclerView() {
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new RecyclerViewHomeAdapter(this, mTypeList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void init() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mTypeList = new ArrayList<>();
    }

    private void getTypeList() {
        try {
            mTypeList.addAll(ReadJson.getType(this));
            Log.d("Home", "getTypeList: " + mTypeList.get(0).getPathImage());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

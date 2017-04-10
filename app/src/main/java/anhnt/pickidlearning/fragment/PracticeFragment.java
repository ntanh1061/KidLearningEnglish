package anhnt.pickidlearning.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import anhnt.pickidlearning.ConstValue;
import anhnt.pickidlearning.R;
import anhnt.pickidlearning.ReadJson;
import anhnt.pickidlearning.activity.FindImageActivity;
import anhnt.pickidlearning.adapter.RecyclerViewAdapter;
import anhnt.pickidlearning.adapter.RecyclerViewHorizontalAdapter;
import anhnt.pickidlearning.models.Category;

/**
 * Created by AnhNT on 3/1/2017.
 */

public class PracticeFragment extends Fragment implements View.OnClickListener {
    private ImageView mImgListenWrite;
    private ImageView mImgWriteWord;
    private ImageView mImgMatchWord;
    private ImageView mImgChooseWord;
    private ImageView mImgListenChoose;
    private ImageView mImgFindImage;
    private RecyclerView mRecyclerView;
    private RecyclerViewHorizontalAdapter mAdapter;
    private LinearLayoutManager mManager;
    private List<Category> categories;
    private int mCategoryId = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_practice, container, false);
        init(view);
        getCategories();
        setupRecyclerView(categories);
        return view;
    }

    private void getCategories() {
        try {
            categories.addAll(ReadJson.readCategory(getContext()));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setupRecyclerView(final List<Category> catagories) {
        mAdapter = new RecyclerViewHorizontalAdapter(getContext(), catagories);
        mManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new RecyclerViewHorizontalAdapter.IOnItemClickListener() {
            @Override
            public void setOnItemClick(int position) {
                mCategoryId = catagories.get(position).getId();
                mManager.scrollToPosition(position);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    public void init(View view) {
        mImgChooseWord = (ImageView) view.findViewById(R.id.img_choose_word);
        mImgChooseWord.setOnClickListener(this);
        mImgFindImage = (ImageView) view.findViewById(R.id.img_find_image);
        mImgFindImage.setOnClickListener(this);
        mImgListenChoose = (ImageView) view.findViewById(R.id.img_listen_choose);
        mImgListenChoose.setOnClickListener(this);
        mImgMatchWord = (ImageView) view.findViewById(R.id.img_match_word);
        mImgMatchWord.setOnClickListener(this);
        mImgListenWrite = (ImageView) view.findViewById(R.id.img_listen_write);
        mImgListenWrite.setOnClickListener(this);
        mImgWriteWord = (ImageView) view.findViewById(R.id.img_write_word);
        mImgWriteWord.setOnClickListener(this);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        categories = new ArrayList<>();
    }

    @Override
    public void onClick(View v) {
        if (mCategoryId == 0) {
            Toast.makeText(getContext(), "Please choose Category", Toast.LENGTH_SHORT).show();
            return;
        }
        switch (v.getId()) {
            case R.id.img_choose_word:
                break;
            case R.id.img_find_image:
                Intent intent = new Intent(getContext(), FindImageActivity.class);
                intent.putExtra(ConstValue.CATEGORY_ID,mCategoryId);
                startActivity(intent);
                break;
            case R.id.img_listen_choose:
                break;
            case R.id.img_match_word:
                break;
            case R.id.img_listen_write:
                break;
            case R.id.img_write_word:
                break;
        }
    }
}

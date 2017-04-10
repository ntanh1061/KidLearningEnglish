package anhnt.pickidlearning.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import anhnt.pickidlearning.ConstValue;
import anhnt.pickidlearning.R;
import anhnt.pickidlearning.ReadJson;
import anhnt.pickidlearning.activity.VocabularyActivity;
import anhnt.pickidlearning.adapter.RecyclerViewAdapter;
import anhnt.pickidlearning.models.Category;

/**
 * Created by AnhNT on 3/1/2017.
 */

public class VocabularyFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;
    private List<Category> categories;
    private RecyclerView.LayoutManager mLayoutManager;;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vocabulary, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        categories = new ArrayList<>();
        try {
            categories.addAll(ReadJson.readCategory(getContext()));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mAdapter = new RecyclerViewAdapter(getContext(), categories);
        mLayoutManager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new RecyclerViewAdapter.IOnItemClickListener() {
            @Override
            public void setOnItemClick(int position) {
                Bundle bundle = new Bundle();
                Intent intent = new Intent(getContext(),VocabularyActivity.class);
                bundle.putInt(ConstValue.SENDDATA, position);
//                bundle.putInt(ConstValue.SENDDATA, categories.get(position).getId());
//                bundle.putString(ConstValue.SEND_CATEGORY_NAME, categories.get(position).getName());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        return view;
    }
}

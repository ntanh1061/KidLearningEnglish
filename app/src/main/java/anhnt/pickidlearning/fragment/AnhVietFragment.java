package anhnt.pickidlearning.fragment;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import anhnt.pickidlearning.R;
import anhnt.pickidlearning.activity.DetailActivity;
import anhnt.pickidlearning.adapter.RecyclerViewDictionaryAdapter;
import anhnt.pickidlearning.databases.DataBaseHelper;
import anhnt.pickidlearning.databases.MyAssetsDatabase;
import anhnt.pickidlearning.databases.MyDatabaseFav;
import anhnt.pickidlearning.models.TuDien;
import anhnt.pickidlearning.myinterface.IOnItemClickListener;

/**
 * Created by AnhNT90
 */

public class AnhVietFragment extends Fragment implements IOnItemClickListener, View.OnClickListener {
    public static final int REQ_CODE_SPEECH_INPUT = 10000;
    public static final String SEND_ANH_VIET = "data";
    private RecyclerView mRecyclerView;
    private EditText mEdtSearch;
    private RecyclerViewDictionaryAdapter mAdapter;
    private List<TuDien> mTuDiens;
    private MyAssetsDatabase mDatabase;
    private MyDatabaseFav mMyDatabaseFav;
    private Context mContext;
    private RecyclerView.LayoutManager mManager;
    private ImageView mImgSpeechToText;
    private int mCheckExists;
    private List<TuDien> tuDiens;
    private int fav;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
        mDatabase = new MyAssetsDatabase(mContext);
        mMyDatabaseFav = new MyDatabaseFav(mContext);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dictionary, container, false);
        init(view);
        displayRecyclerView(view);
        mAdapter.setOnItemClickListener(this);
        searchTuDien();

        return view;
    }

    /*
    Tim kiem du lieu
     */
    public void searchTuDien() {
        mEdtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTuDiens.clear();
                mDatabase.open();
                char[] wordSearch = new char[s.toString().length()];
                for (int i = 0; i < wordSearch.length; i++) {
                    wordSearch[i] = s.toString().charAt(i);
                }
                try {
                    mTuDiens.addAll(mDatabase.getTuDienByWord(s.toString().charAt(0), wordSearch));
                    if (mTuDiens.size() == 0) {
                        Toast.makeText(mContext, "Cann't find [" + s + "]", Toast.LENGTH_SHORT).show();
                    }
                    mAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    mTuDiens.addAll(mDatabase.getAnhVietsByWord("a"));
                    mAdapter.notifyDataSetChanged();
                }

                mDatabase.close();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /*
    Khai bao cac view trong layout
     */
    public void init(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mImgSpeechToText = (ImageView) view.findViewById(R.id.img_speech_to_text);
        mEdtSearch = (EditText) view.findViewById(R.id.edtSearch);
        mMyDatabaseFav = new MyDatabaseFav(mContext);
        mImgSpeechToText.setOnClickListener(this);
    }

    /*
    Cai dat hien thi recycler view
     */
    public void displayRecyclerView(View view) {
        mDatabase.open();
        mTuDiens = new ArrayList<>();
        mTuDiens.addAll(mDatabase.getAnhVietsByWord("a"));
//        MyAsyncTask myAsyncTask = new MyAsyncTask();
//        myAsyncTask.execute();
        mAdapter = new RecyclerViewDictionaryAdapter(mContext, mTuDiens);
        mManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mDatabase.close();
        mMyDatabaseFav.close();
    }

    @Override
    public void setOnItemClick(int position) {

    }

    /*
        Click item tren recycler view
         */
    @Override
    public void itemClick(int position) {
        actionRecyclerViewItemClick(mTuDiens.get(position));
    }

    /*
    Click nut yeu thich tren recycler view
     */
    @Override
    public void yourWordClick(int position) {
        changeFavourite(position);
    }

    @Override
    public void speakerItemClick(int position) {

    }

    @Override
    public void itemLongClick(int position) {

    }

    public void changeFavourite(int position) {
        mMyDatabaseFav = new MyDatabaseFav(mContext);
        tuDiens = mMyDatabaseFav.getTuDienFav();

        TuDien tuDien = mTuDiens.get(position);
        int id = tuDien.getId();
        String word = tuDien.getWord();
        String content = tuDien.getContent();
        fav = tuDien.getFav();
        for (int i = 0; i < tuDiens.size(); i++) {
            if (id == tuDiens.get(i).getId()) {
                fav = tuDiens.get(i).getFav();
                mCheckExists++;
            }
        }
        if (mCheckExists > 0) {
            mMyDatabaseFav.updateTuDienFav((fav == 1) ? 0 : 1, id);
            Toast.makeText(mContext, (fav == 1) ? "[" + word + "] Remove from [Your Words]" : "[" + word + "] Add to [Your Words]", Toast.LENGTH_SHORT).show();
        } else {
            TuDien addTuDien = new TuDien(id, word, content, 1);
            mMyDatabaseFav.insertDatabase(addTuDien);
            Toast.makeText(mContext, "[" + word + "] Add to [Your Words]", Toast.LENGTH_SHORT).show();
        }
        mMyDatabaseFav.close();
    }

    /*
    Click nut micro
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_speech_to_text:
                speechToText();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_SPEECH_INPUT) {
            if (data != null) {
                ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                mDatabase.open();
                TuDien tuDien = mDatabase.getTuDien(results.get(0));
                if (tuDien.getWord().toString().trim().toLowerCase().equals(results.get(0))) {
                    actionRecyclerViewItemClick(tuDien);
                } else {
                    Toast.makeText(mContext, "Can't find [" + results.get(0) + "]", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(mContext, "Can't find [" + results.get(0) + "]", Toast.LENGTH_SHORT).show();
                mDatabase.close();
            } else {
                Toast.makeText(mContext, "No data!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void speechToText() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(mContext, "Speech is not support", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }

    /*
            Action khi click vao item
         */
    public void actionRecyclerViewItemClick(TuDien tuDien) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(SEND_ANH_VIET, tuDien);
        Intent intent = new Intent(mContext, DetailActivity.class);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMyDatabaseFav.close();
        mDatabase.close();
    }
}

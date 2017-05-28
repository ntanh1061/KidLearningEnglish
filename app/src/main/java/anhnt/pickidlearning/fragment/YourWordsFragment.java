package anhnt.pickidlearning.fragment;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import anhnt.pickidlearning.R;
import anhnt.pickidlearning.activity.DetailActivity;
import anhnt.pickidlearning.adapter.RecyclerViewRecentWordAdapter;
import anhnt.pickidlearning.databases.DataBaseHelper;
import anhnt.pickidlearning.databases.MyAssetsDatabase;
import anhnt.pickidlearning.databases.MyDatabaseFav;
import anhnt.pickidlearning.models.TuDien;
import anhnt.pickidlearning.myinterface.IOnItemClickListener;

/**
 * Created by AnhNT on 1/12/2017.
 */

public class YourWordsFragment extends Fragment {
    public static final String TAG = "YourWord";
    private Context mContext;
    private RecyclerView mRecyclerView;
    private RecyclerViewRecentWordAdapter mAdapter;
    private List<TuDien> mTuDiens;
    private MyDatabaseFav mMyDatabaseFav;
    private ImageView mImgLogo;
    private TextView mTvTitle;
    private TextToSpeech tts;
    private EditText mEdtSearch;
    private ImageView mImgSpeechToText;
    private MyAssetsDatabase mDatabase;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
        mMyDatabaseFav = new MyDatabaseFav(mContext);
        mDatabase = new MyAssetsDatabase(mContext);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_word, container, false);
        init(view);
        recyclerViewClick();
        searchFunc();
        clickSpeechToText();
        return view;
    }

    public void clickSpeechToText() {
        mImgSpeechToText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speechToText();
            }
        });
    }

    public void searchFunc() {
        mEdtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTuDiens.clear();
                mMyDatabaseFav = new MyDatabaseFav(mContext);
                List<TuDien> tuDiens = mMyDatabaseFav.getTuDienByWord(s.toString());
                try {
                    for (int i = 0; i < tuDiens.size(); i++) {
                        TuDien tuDien = tuDiens.get(i);
                        if (tuDien.getFav() == 1) {
                            mTuDiens.add(tuDien);
                        }
                    }

                    if (mTuDiens.size() == 0) {
                        Toast.makeText(mContext, "Can't find [" + s + "]", Toast.LENGTH_SHORT).show();
                    }
                    mAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    for (int i = 0; i < tuDiens.size(); i++) {
                        TuDien tuDien = tuDiens.get(i);
                        if (tuDien.getFav() == 1) {
                            mTuDiens.add(tuDien);
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                }
                mMyDatabaseFav.close();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void speechToText() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hint speech to text");
        try {
            startActivityForResult(intent, AnhVietFragment.REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(mContext, "Speech is not support", Toast.LENGTH_SHORT).show();
        }
    }

    public void init(View view) {
        mEdtSearch = (EditText) view.findViewById(R.id.edtRecentSearch);
        mImgSpeechToText = (ImageView) view.findViewById(R.id.img_speech_to_text);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recent_recycler_view);
        mTuDiens = new ArrayList<>();
        mMyDatabaseFav = new MyDatabaseFav(mContext);
        List<TuDien> tuDiens = mMyDatabaseFav.getTuDienFav();
        for (int i = 0; i < tuDiens.size(); i++) {
            TuDien tuDien = tuDiens.get(i);
            if (tuDien.getFav() == 1) {
                mTuDiens.add(tuDien);
            }
        }
        mAdapter = new RecyclerViewRecentWordAdapter(mContext, mTuDiens);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mAdapter);
        mMyDatabaseFav.close();
    }

    public void recyclerViewClick() {
        mAdapter.setItemListener(new IOnItemClickListener() {
            @Override
            public void setOnItemClick(int position) {

            }

            @Override
            public void itemClick(int position) {
                actionRecyclerViewItemClick(mTuDiens.get(position));
            }

            @Override
            public void yourWordClick(int position) {
                mMyDatabaseFav = new MyDatabaseFav(mContext);
                List<TuDien> tuDiens = mMyDatabaseFav.getTuDienFav();
                int id = tuDiens.get(position).getId();
                int fav = tuDiens.get(position).getFav();
                mMyDatabaseFav.updateTuDienFav((fav == 1) ? 0 : 1, id);
                mTuDiens.remove(position);
                mAdapter.notifyDataSetChanged();
                mMyDatabaseFav.close();
            }

            @Override
            public void speakerItemClick(int position) {
                textToSpeech(mTuDiens.get(position));
            }

            @Override
            public void itemLongClick(final int position) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                alertDialog.setTitle("Do you want delete?");
                alertDialog.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mMyDatabaseFav = new MyDatabaseFav(mContext);
                        mMyDatabaseFav.deleteYourWord(mTuDiens.get(position));
                        mTuDiens.remove(position);
                        mAdapter.notifyDataSetChanged();
                    }
                });
                alertDialog.setPositiveButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                alertDialog.create();
                alertDialog.show();
            }
        });
    }

    public void actionRecyclerViewItemClick(TuDien tuDien) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(AnhVietFragment.SEND_ANH_VIET, tuDien);
        Intent intent = new Intent(mContext, DetailActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void textToSpeech(final TuDien tuDien) {
        tts = new TextToSpeech(mContext, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(Locale.US);
                    String text = tuDien.getWord().toString();
                    if (text == null || "".equals(text)) {
                        text = " Content is not available";
                        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                    } else {
                        tts.speak(text, TextToSpeech.QUEUE_ADD, null);
                    }
                }
            }
        });
    }

    public void actionSpeechToText(Intent data) {
        if (data != null) {
            ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            mDatabase.open();
            TuDien tuDien = mDatabase.getTuDien(results.get(0));
            if (tuDien.getWord().toString().trim().toLowerCase().equals(results.get(0))) {
                actionRecyclerViewItemClick(tuDien);
            } else {
                Toast.makeText(mContext, "Can't find [" + results.get(0) + "]", Toast.LENGTH_SHORT).show();
            }
            mDatabase.close();
        } else {
            Toast.makeText(mContext, "No data!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AnhVietFragment.REQ_CODE_SPEECH_INPUT) {
            actionSpeechToText(data);
        }
    }
}

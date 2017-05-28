package anhnt.pickidlearning.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

import anhnt.pickidlearning.R;
import anhnt.pickidlearning.databases.MyAssetsDatabase;
import anhnt.pickidlearning.databases.MyDatabaseFav;
import anhnt.pickidlearning.fragment.AnhVietFragment;
import anhnt.pickidlearning.models.TuDien;

/**
 * Created by HCD-Fresher046 on 1/10/2017.
 */

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {
    private WebView mWebView;
    private Toolbar mToolbar;
    private TuDien mTuDien;
    private ActionBar mActionBar;
    private ImageView mImgTextToSpeechUK;
    private TextView mTvTitle;
    String mHtmlContent;
    private MyAssetsDatabase mDatabase;
    private MyDatabaseFav mMyDatabaseFav;
    List<TuDien> tuDiens;
    private TextToSpeech tts;
    private int fav;
    private int mCheckExists;
    private MenuItem mItemFav;
    private Dialog mDialog;
    private EditText mEdtAddNote;
    private String mWord;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        init();
        getDataFromMainActivity();
        displayWebView();
        setToolbar();
        clickContentInsideWebView();

    }

    public void textToSpeech(final Locale locale) {
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(locale);
                    String text = mWord;
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

    public void setToolbar() {
        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_action_back);
    }

    /*
    Setup view
     */
    public void init() {
        mWebView = (WebView) findViewById(R.id.webView);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mImgTextToSpeechUK = (ImageView) findViewById(R.id.imgTextToSpeechUK);
        mImgTextToSpeechUK.setOnClickListener(this);
        mDatabase = new MyAssetsDatabase(DetailActivity.this);
        mTvTitle = (TextView) findViewById(R.id.tvTitle);
        mTvTitle.setVisibility(View.VISIBLE);
    }

    /*
    Lay du lieu tu mainactivity gui qua
     */
    public void getDataFromMainActivity() {
        Bundle bundle = getIntent().getExtras();
        mTuDien = bundle.getParcelable(AnhVietFragment.SEND_ANH_VIET);
        mWord = mTuDien.getWord();
        mTvTitle.setText(mWord);
    }

    /**
     * Hien thi webview
     */
    public void displayWebView() {
        mHtmlContent = mTuDien.getContent();
        mWebView.loadDataWithBaseURL("", mHtmlContent, "html/text", "utf-8", "");
        mWebView.getSettings().setJavaScriptEnabled(true);
    }

    public void clickContentInsideWebView() {
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                reloadWebView(url);
                return true;

            }
        });
    }

    public void reloadWebView(String url) {
        mDatabase.open();
        String word = url.substring(8);
        try {
            mHtmlContent = mDatabase.getContentByWord(word.charAt(0), word);
            mWord = word;
            mTvTitle.setText(mWord);
            mWebView.loadDataWithBaseURL("", mHtmlContent, "html/text", "utf-8", "");
        } catch (Exception e) {
            Toast.makeText(DetailActivity.this, "Can't not find [" + word + "]", Toast.LENGTH_SHORT).show();
        }
        mDatabase.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_menu, menu);
        mItemFav = menu.findItem(R.id.action_add_your_word);
        MenuItem menuItem = menu.findItem(R.id.action_add);
        changeStateFavourite(mItemFav);
        return super.onCreateOptionsMenu(menu);
    }

    public void changeStateFavourite(MenuItem menuItem) {
        mMyDatabaseFav = new MyDatabaseFav(this);
        List<TuDien> tuDiens = mMyDatabaseFav.getTuDienFav();
        int id = mTuDien.getId();

        fav = mTuDien.getFav();
        for (int i = 0; i < tuDiens.size(); i++) {
            if (id == tuDiens.get(i).getId()) {
                fav = tuDiens.get(i).getFav();
            }
        }

        mTuDien.setFav((fav == 1) ? 0 : 1);
        menuItem.setIcon((fav == 1) ? R.mipmap.ic_start_selected : R.mipmap.ic_star_border_black);
    }

    @Override
    public void onClick(View v) {
        textToSpeech(Locale.UK);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_your_word:
                addToYourWords();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void addToYourWords() {
        mMyDatabaseFav = new MyDatabaseFav(this);
        tuDiens = mMyDatabaseFav.getTuDienFav();

        int id = mTuDien.getId();
        String word = mTuDien.getWord();
        String content = mTuDien.getContent();
        Log.d("FAV", "addToYourWords: " + fav);
        for (int i = 0; i < tuDiens.size(); i++) {
            int checkId = tuDiens.get(i).getId();
            if (id == checkId) {
                fav = tuDiens.get(i).getFav();
                mCheckExists++;
            }
        }
        Log.d("TestCheck", "addToYourWords: " + mCheckExists);
        if (mCheckExists > 0) {
            mMyDatabaseFav.updateTuDienFav((fav == 1) ? 0 : 1, id);
            Toast.makeText(this, (fav == 1) ? "[" + word + "] Remove from [Your Words]" : "[" + word + "] Add to [Your Words]", Toast.LENGTH_SHORT).show();
        } else {
            TuDien addTuDien = new TuDien(id, word, content, 1);
            mMyDatabaseFav.insertDatabase(addTuDien);
            Toast.makeText(this, "[" + word + "] Add to [Your Words]", Toast.LENGTH_SHORT).show();
        }
        mItemFav.setIcon((fav == 1) ? R.mipmap.ic_star_border_black : R.mipmap.ic_start_selected);

        mMyDatabaseFav.close();
    }
}

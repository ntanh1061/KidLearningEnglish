package anhnt.pickidlearning.activity;

import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;

import anhnt.pickidlearning.R;
import anhnt.pickidlearning.fragment.PracticeFragment;
import anhnt.pickidlearning.fragment.VocabularyFragment;

public class MainActivity extends AppCompatActivity {
    private TabLayout mTabLayout;
    private Toolbar mToolbar;
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        setupToobar();
        setupTabLayout();
    }

    private void init() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    }

    private void setupTabLayout() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fr_container, new VocabularyFragment())
                .commit();

        final TabLayout.Tab tabVocabulary = mTabLayout.newTab().setText("Vocabulary");
        TabLayout.Tab tabPractice = mTabLayout.newTab().setText("Practic");
        mTabLayout.addTab(tabVocabulary);
        mTabLayout.addTab(tabPractice);
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab == tabVocabulary) {
//                    tab.setIcon(R.mipmap.ic_vocabulary_on);
//                    tab.setText("Vocabulary");
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fr_container, new VocabularyFragment())
                            .commit();
                } else {
//                    tab.setIcon(R.mipmap.ic_practice_on);
//                    tab.setText("Practice");
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fr_container, new PracticeFragment())
                            .addToBackStack(null)
                            .commit();
                }
//                ViewGroup vg = (ViewGroup) mTabLayout.getChildAt(0);
//                int delay = 100; //this is starting delay
//                ViewGroup vgTab = (ViewGroup) vg.getChildAt(tab.getPosition());
//                vgTab.setScaleX(0f);
//                vgTab.setScaleY(0f);
//                vgTab.animate()
//                        .scaleX(1f)
//                        .scaleY(1f)
//                        .setStartDelay(delay)
//                        .start();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
//                tab.setText("");
//                if (tab == tabVocabulary) {
//                    tab.setIcon(R.mipmap.ic_vocabulary_off);
//                } else {
//                    tab.setIcon(R.mipmap.ic_practice_off);
//                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setupToobar() {
        setSupportActionBar(mToolbar);
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setTitle("PicKidLearing");
        mToolbar.setTitleMarginStart(30);
        mToggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.setDrawerListener(mToggle);
        mToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}

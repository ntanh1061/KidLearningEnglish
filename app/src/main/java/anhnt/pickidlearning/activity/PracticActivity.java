package anhnt.pickidlearning.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import anhnt.pickidlearning.ConstValue;
import anhnt.pickidlearning.R;

/**
 * Created by AnhNT on 4/26/2017.
 */

public class PracticActivity extends AppCompatActivity {
    private Bundle mBundle;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practic);
    }

}

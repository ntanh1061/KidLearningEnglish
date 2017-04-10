package anhnt.pickidlearning.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import anhnt.pickidlearning.ConstValue;
import anhnt.pickidlearning.R;

/**
 * Created by AnhNT on 2/15/2017.
 */

public class FlashActivity extends AppCompatActivity {
    private Handler mHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Bundle bundle = new Bundle();
                Intent intent = new Intent(FlashActivity.this, MainActivity.class);
                bundle.putInt(ConstValue.SEND_TYPE_ID, 0);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }
}


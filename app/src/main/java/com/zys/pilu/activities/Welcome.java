package com.zys.pilu.activities;

import android.content.Intent;
import android.os.Bundle;

import com.zys.pilu.utils.SongProvider;
import com.zys.pilu.R;
import java.util.Timer;
import java.util.TimerTask;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by tongyang on 16/7/12.
 */
public class Welcome extends SwipeBackActivity {
    final private String TAG = "Welcome";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(Welcome.this, pilu.class));
                finish();
            }
        }, 3200);

        Timer timer1 = new Timer();
        timer1.schedule(new TimerTask() {
            @Override
            public void run() {
                SongProvider.getSongList();
            }
        }, 800);
    }

}
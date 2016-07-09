package com.zys.pilu.activities;

import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.zys.pilu.R;
import com.zys.pilu.customviews.DragView;

public class pilu extends AppCompatActivity {
    private String TAG = "Yobey";
    private RadioGroup tabs;
    private RadioButton tab_home;
    private RadioButton tab_list;
    private RadioButton tab_artist;
    private RadioButton tab_player;
    private DragView dragView;
    private ViewPager pager;

    private int currentFragment = 0;

    private Drawable homeDrawableWhite;
    private Drawable listDrawableWhite;
    private Drawable artistDrawableWhite;
    private Drawable settingsDrawableWhite;
    private Drawable homeDrawableGrey;
    private Drawable listDrawableGrey;
    private Drawable artistDrawableGrey;
    private Drawable settingsDrawableGrey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilu);

        findViewsById();
        setOnClickListener();
        dragView.setAnimation(0);

        initTabs();
    }

    private void findViewsById() {
        tabs = (RadioGroup)findViewById(R.id.tabs);
        tab_home = (RadioButton)findViewById(R.id.homeTab);
        tab_artist = (RadioButton)findViewById(R.id.artistTab);
        tab_list = (RadioButton)findViewById(R.id.listTab);
        tab_player = (RadioButton)findViewById(R.id.playerTab);
        dragView = (DragView)findViewById(R.id.drag);
        pager = (ViewPager)findViewById(R.id.pager);
    }
    private void setOnClickListener() {
        tabs.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.homeTab:
                        dragView.setAnimation(0);
                        pager.setCurrentItem(0, true);
                        setWhiteText(tab_home, 0);
                        Log.e(TAG, "homeTab");
                        break;
                    case R.id.listTab:
                        dragView.setAnimation(1);
                        pager.setCurrentItem(1, true);
                        setWhiteText(tab_list, 1);
                        Log.e(TAG, "listTab");
                        break;
                    case R.id.artistTab:
                        dragView.setAnimation(2);
                        pager.setCurrentItem(2, true);
                        setWhiteText(tab_artist, 2);
                        Log.e(TAG, "artistTab");
                        break;
                    case R.id.playerTab:
                        dragView.setAnimation(3);
                        pager.setCurrentItem(3, true);
                        setWhiteText(tab_player, 3);
                        Log.e(TAG, "playerTab");
                        break;
                    default:
                        break;
                }
            }
        });
    }
    private void initTabs() {
        homeDrawableWhite = getResources().getDrawable(R.drawable.home_white);
        homeDrawableWhite.setBounds(0, 0, 50, 50);
        homeDrawableGrey = getResources().getDrawable(R.drawable.home_grey);
        homeDrawableGrey.setBounds(0, 0, 50, 50);

        listDrawableGrey = getResources().getDrawable(R.drawable.list_grey);
        listDrawableGrey.setBounds(0, 0, 50, 50);
        listDrawableWhite = getResources().getDrawable(R.drawable.list_white);
        listDrawableWhite.setBounds(0, 0, 50, 50);

        artistDrawableGrey = getResources().getDrawable(R.drawable.artist_grey);
        artistDrawableGrey.setBounds(0, 0, 50, 50);
        artistDrawableWhite = getResources().getDrawable(R.drawable.artist_white);
        artistDrawableWhite.setBounds(0, 0, 50, 50);

        settingsDrawableGrey = getResources().getDrawable(R.drawable.settings_grey);
        settingsDrawableGrey.setBounds(0, 0, 50, 50);
        settingsDrawableWhite = getResources().getDrawable(R.drawable.settings_white);
        settingsDrawableWhite.setBounds(0, 0, 50, 50);


        tab_player.setCompoundDrawables(null, settingsDrawableGrey, null, null);
        tab_home.setCompoundDrawables(null, homeDrawableWhite, null, null);
        tab_artist.setCompoundDrawables(null, artistDrawableGrey, null, null);
        tab_list.setCompoundDrawables(null, listDrawableGrey, null, null);
    }
    private void setWhiteText(RadioButton tab, int position) {
        tab_player.setTextColor(getResources().getColor(R.color.qianhui));
        tab_list.setTextColor(getResources().getColor(R.color.qianhui));
        tab_artist.setTextColor(getResources().getColor(R.color.qianhui));
        tab_home.setTextColor(getResources().getColor(R.color.qianhui));

        tab_player.setCompoundDrawables(null, settingsDrawableGrey, null, null);
        tab_list.setCompoundDrawables(null, listDrawableGrey, null, null);
        tab_artist.setCompoundDrawables(null, artistDrawableGrey, null, null);
        tab_home.setCompoundDrawables(null, homeDrawableGrey, null, null);

        Drawable[] temps = {homeDrawableWhite, listDrawableWhite, artistDrawableWhite, settingsDrawableWhite};
        tab.setTextColor(getResources().getColor(R.color.baise));
        tab.setCompoundDrawables(null, temps[position], null, null);

    }
}

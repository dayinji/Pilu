package com.zys.pilu.activities;

import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zys.pilu.models.Song;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by tongyang on 16/7/9.
 */
public class SongList {
    private final String TAG = "SongListActivity";

    private ListView songListView;
    private ImageView preBt;
    private ImageView playBt;
    private ImageView nextBt;
    private ImageView playingPhoto;
    private TextView playingName;
    private TextView playingArtist;
    private RelativeLayout bottomLayout;
    private TextView selectorText;

    private boolean isPlay = false;
    private int current = 0;
    private long currentSongId = 0;
    private int currentTime;
    private boolean isFirstTime = true;
    private String listName;
    private ListReceiver listReceiver;
    //private SongListAdapter mySongListAdapter;
    /* private AnimationDrawable animPlay;
     private AnimationDrawable animNext;
     private AnimationDrawable animPre;*/
    private ObjectAnimator fadeSelectorAnim;
    private MyOnScrollListener myScrollListener;
    private int[] animId = new int[] {};
    /*
     * 0 = LoopPlaying
     * 1 = SingPlaying
     * 2 = RandomPlaying
     */
    private int mode = 0;

    //private SongListAdapter songListAdapter;
    private List<Song> songList = new ArrayList<Song>();

    //@Override
    protected void onCreate(Bundle savedInstanceState) {

    }

    private class MyOnScrollListener implements AbsListView.OnScrollListener{
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        }
    }


    //@Override
    public void onDestroy() {

    }



    /*
     * Find All Views When Create the Home Activity
     */
    private void findViewsById() {

    }

    /*
     * Set the ClickListener to Views
     */
    private void setClickListener() {

    }

    //@Override
    public void onClick(View view) {


    }

    /*
     * Receive the Broad from Sevice for Updating UI
     */
    private class ListReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    }

    private void updateBar(int currentTime) {
        this.currentTime = currentTime;
    }

    private void playDrawableAnim(ImageView view, int id, AnimationDrawable animDrawable) {

    }

    private void playAnim(final ImageView view, final int id, float middlePoint, boolean isZoomOut) {

    }
}

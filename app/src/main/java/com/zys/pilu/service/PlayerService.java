package com.zys.pilu.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.WindowManager;
import android.widget.RemoteViews;

import com.zys.pilu.R;
import com.zys.pilu.common.Constants;
import com.zys.pilu.db.DBManager;
import com.zys.pilu.models.Song;
import com.zys.pilu.utils.SongProvider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zys on 2016/7/10.
 */
public class PlayerService extends Service {
    private final String TAG = "PlayerService";
    private int duration;
    private int current = 0;
    private int currentTime = 0;
    private Long currentSongId;
    private boolean isPlay = false;
    private String listName;
    private boolean hasNotify = true;
    private NotificationManager nm;
    /*
     * 0 = LoopPlaying
     * 1 = SingPlaying
     * 2 = RandomPlaying
     */
    private int mode = 0;
    private List<Integer> randIndex;
    private MediaPlayer player;
    private List<Song> songList;
    private Handler handler;
    private Timer timer;
    private boolean isShowWl = true;
    private boolean isLockWL = false;
    private SharedPreferences sharedPref;

    private DBManager dbMgr;

    // When Calling Phone, Stop Playing Music
    private boolean mResumeAfterCall = false;
    private PhoneStateListener phoneStateListener = new PhoneStateListener(){
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            if (state == TelephonyManager.CALL_STATE_RINGING) {
                if (isPlay && !mResumeAfterCall) {
                    pause();
                    mResumeAfterCall = true;
                }
            } else if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
                if (isPlay && !mResumeAfterCall) {
                    pause();
                    mResumeAfterCall = true;
                }
            } else if (state == TelephonyManager.CALL_STATE_IDLE) {
                // start playing again
                if (mResumeAfterCall) {
                    resum();
                    mResumeAfterCall = false;
                }
            }
            boardCurrentState();
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

        // Init sharedPref
        sharedPref = getSharedPreferences(
                Constants.Preferences.PREFERENCES_KEY, Context.MODE_PRIVATE);

        // When Calling Phone, Stop Playing Music
        TelephonyManager tmgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        tmgr.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);


        dbMgr = new DBManager();
        isPlay = false;
        nm  = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        listName = Constants.ListName.LIST_ALL;
        songList = new ArrayList<Song>();
        songList = SongProvider.getSongList();
        currentSongId = songList.get(current).getId();
        player = new MediaPlayer();
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                updateDB(true, current);
                playNext();
                boardCurrentState();
            }
        });
        init();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    try {
                        Intent sendIntent = new Intent(Constants.UiControl.UPDATE_CURRENT);
                        currentTime = player.getCurrentPosition();
                        sendIntent.putExtra("currentTime", currentTime);
                        sendBroadcast(sendIntent);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(TAG, "Player has destory!");
                    }
                }
            }
        };
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(1);
            }
        }, 0, 500);

    }
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags,  int startId) {
        Log.e(TAG, "intent == null? " + (intent == null));
        Log.e(TAG, intent.getExtras().getString("controlMsg"));
        switch (intent.getExtras().getString("controlMsg")) {
            case Constants.PlayerControl.PRE_SONG_MSG:
                if (currentTime > 20*1000 && currentTime < duration - 30*1000)
                    updateDB(false, current);
                else if (currentTime >= duration-30*1000)
                    updateDB(true, current);
                playPre();
                break;
            case Constants.PlayerControl.NEXT_SONG_MSG:
                if (currentTime > 20 * 1000 && currentTime < duration - 30*1000)
                    updateDB(false, current);
                else if (currentTime >= duration-30*1000)
                    updateDB(true, current);
                playNext();
                break;
            case Constants.PlayerControl.PAUSE_PLAYING_MSG:
                pause();
                break;
            case Constants.PlayerControl.CONTINUE_PLAYING_MSG:
                resum();
                break;
            case Constants.PlayerControl.PLAYING_MSG:
                current = intent.getExtras().getInt("current");
                currentSongId = songList.get(current).getId();
                currentTime = intent.getExtras().getInt("currenTime");
                play(currentTime);
                break;
            case Constants.PlayerControl.UPDATE_CURRENTTIME:
                updateCurrentTime(intent.getExtras().getInt("currentTime"));
                break;
            case Constants.PlayerControl.INIT_GET_CURRENT_INFO:
                // Only for Get Current SongId And Other Info
                break;
            case Constants.PlayerControl.CHANGE_MODE:
                mode = mode + 1 >= 3 ? 0 : mode + 1;
                break;
            case Constants.PlayerControl.UPDATE_LIST:
                //updateList(intent.getExtras().getString("listName"));
                return START_STICKY;
            case Constants.PlayerControl.CHANGE_LIST:
                changeList(intent.getExtras().getString("listName"));
                return START_STICKY;
            case Constants.PlayerControl.INIT_SERVICE:
                changeList(intent.getExtras().getString("listName"));
                current = intent.getExtras().getInt("current");
                try {
                    if (songList.size() > current) {
                        currentSongId = songList.get(current).getId();
                    } else if (songList.size() != 0) {
                        current = 0;
                        currentSongId = songList.get(current).getId();
                    } else {
                        changeList(Constants.ListName.LIST_ALL);
                        current = 0;
                        currentSongId = songList.get(current).getId();
                    }
                    init();
                } catch (Exception e) {
                    Log.e(TAG, "Exception");
                    e.printStackTrace();
                }
                return START_STICKY;
            default:
                break;
        }
        boardCurrentState();
        /*
         * THe Big Big Bug had Happened Here! I Return StartId Instead of START_STICKY Before.
         */
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
        timer.cancel();
        hasNotify = false;
        nm.cancel(950520);
        nm.cancel(950521);


        super.onDestroy();
    }

    private void init() {
        try {
            player.reset();
            player.setDataSource(songList.get(current).getFileName());
            player.prepare();
            duration = songList.get(current).getDuration();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void play(int seekPos) {
        try {
            if (player.isPlaying())
                player.stop();
            player.reset();
            player.setDataSource(songList.get(current).getFileName());
            player.prepare();
            player.setOnPreparedListener(new MyPreparedListener(seekPos));
            //player.start();
            isPlay = true;
            duration = songList.get(current).getDuration();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * Get A Random Num that Is No Equal to CurrentNum
     */
    private int getRandom(int current) {
        return (int)(Math.random()*songList.size());
    }

    /*
     * Play Pre Song
     */
    private void playPre() {
        //Log.d(TAG, "Play Pre!");
        if (mode == 0) {
            current--;
            current = current < 0 ? songList.size() - 1 : current;  // current trun to max position if current < 0
            currentSongId = songList.get(current).getId();
            play(0);
        } else if (mode == 1) {
            play(0);
        } else {
            current = getRandom(current);
            currentSongId = songList.get(current).getId();
            play(0);
        }
    }

    /*
     * Play Next Song
     */
    private void playNext() {
        //Log.d(TAG, "Play Next!");
        if (mode == 0) {
            current++;
            current = current > (songList.size() - 1) ? 0 : current;  // current trun to 0 position if current > max
            currentSongId = songList.get(current).getId();
            play(0);
        } else if (mode == 1) {
            play(0);
        } else {
            current = getRandom(current);
            currentSongId = songList.get(current).getId();
            play(0);
        }
    }

    /*
     * Pause Music
     */
    private void pause() {
        //Log.d(TAG, "Pause!");
        if (isPlay) {
            player.pause();
        }
        isPlay = false;
    }

    /*
     * Resum Music
     */
    private void resum() {
        //Log.d(TAG, "Resum!");
        if (!isPlay) {
            player.start();
        }
        isPlay = true;
    }

    /*
     * Update CurrentTime
     */
    private void updateCurrentTime(int currentTime) {
        this.currentTime = currentTime;
        player.seekTo(currentTime);
    }

    /*
     * the Class For Playing Music
     */
    private class MyPreparedListener implements MediaPlayer.OnPreparedListener {
        private int seekPos;
        public MyPreparedListener(int seekPos) {
            this.seekPos = seekPos;
        }
        @Override
        public void onPrepared(MediaPlayer mp) {
            player.start();
            if (seekPos > 0) {
                player.seekTo(seekPos);
            }
        }
    }
    private void updateDB(boolean isCompleted, int current) {
        Log.e(TAG, "updateDB");
        Log.e(TAG, "isCompleted = " +  isCompleted);
        Log.e(TAG, "name = " + songList.get(current).getName());
        Song temp = songList.get(current);
        if (!dbMgr.inSongDetail(temp)) {
            dbMgr.addToSongDetail(temp);
        }
        dbMgr.updatePlayCount(temp);
        if (!isCompleted) {
            dbMgr.updateSwicthCount(temp);
        }
        dbMgr.updateDateCountPlay(isCompleted);
    }
    /*
     * Update the SongList EveryTime the User Open the List
     */
    private void updateList(String listName) {
        if (listName.equals(this.listName)) {
            songList = SongProvider.getSongListByName(listName);
            this.listName = listName;
        }
    }
    /*
     * Change the SongList EveryTime the User Click the List Item to Play Music
     */
    private void changeList(String listName) {
        // Before Change List and Play a New Song
        // Save the Play Count
        if (currentTime > 20 * 1000 && currentTime < duration - 30*1000)
            updateDB(false, current);
        else if (currentTime >= duration-30*1000)
            updateDB(true, current);

        if (listName.equals(this.listName))
            return;
        else {
            songList = SongProvider.getSongListByName(listName);
            this.listName = listName;
        }
    }
    /*
     * Boardcast Current State
     */
    private void boardCurrentState() {
        Intent sendIntent = new Intent(Constants.UiControl.UPDATE_UI);
        sendIntent.putExtra("current", current);
        sendIntent.putExtra("isPlay", isPlay);
        currentTime = player.getCurrentPosition();
        sendIntent.putExtra("currentTime", currentTime);
        sendIntent.putExtra("songId", currentSongId);
        sendIntent.putExtra("mode", mode);
        sendIntent.putExtra("listName", listName);
        sendBroadcast(sendIntent);
    }


}

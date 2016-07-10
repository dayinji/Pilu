package com.zys.pilu.fragments;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.zys.pilu.R;
import com.zys.pilu.adapter.ArtistAdapter;
import com.zys.pilu.customviews.PinyinBar;
import com.zys.pilu.utils.SongProvider;

import java.util.List;

public class Artists extends Fragment {
    private View root;
    private ListView artistListView;
    private ArtistAdapter adapter;
    private PinyinBar pinyinBar;
    private TextView selectorText;
    private ObjectAnimator fadeSelectorAnim;
    private List<com.zys.pilu.models.Artist> artistList;

    public Artists() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root =  inflater.inflate(R.layout.fragment_artists, container, false);
        findViewsById();

        artistList = SongProvider.getArtistList();

        adapter = new ArtistAdapter(getActivity());
        artistListView.setAdapter(adapter);
        artistListView.setVerticalScrollBarEnabled(false);
        // Init fadeSelectorAnim
        fadeSelectorAnim = ObjectAnimator.ofFloat(selectorText, "alpha", 1f, 0f).setDuration(300);
        fadeSelectorAnim.setStartDelay(300);

        pinyinBar.callback = new PinyinBar.PinyinBarCallBack() {
            @Override
            public void onBarChange(int current) {
                if (artistList.size() != 0) {

                    char[] letters = {'#', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
                            'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
                            'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
                            'Y', 'Z'
                    };
                    int position = adapter.getPositionByLetter(letters[current]);
                    artistListView.setSelection(position);
                    if (fadeSelectorAnim != null && fadeSelectorAnim.isRunning())
                        fadeSelectorAnim.cancel();
                    selectorText.setAlpha(1);
                    String name = artistList.get(position).getName();
                    selectorText.setText(name.substring(0, 1));
                    fadeSelectorAnim.start();
                }
            }
        };

        return root;
    }
    private void findViewsById() {
        artistListView = (ListView)root.findViewById(R.id.artistList);
        pinyinBar = (PinyinBar)root.findViewById(R.id.pinyinBar);
        selectorText = (TextView)root.findViewById(R.id.selectorText);
    }



}

package com.zys.pilu.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zys.pilu.R;
import com.zys.pilu.activities.SongList;
import com.zys.pilu.adapter.ListsAdapter;
import com.zys.pilu.common.Constants;
/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Lists.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Lists#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Lists extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    //private static final String ARG_PARAM1 = "param1";
    //private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    //private String mParam1;
    //private String mParam2;
    private String TAG = "Lists";
    private ListView lists;
    private View root;

    private final String[] listNames = {
            Constants.ListName.LIST_ALL,
            Constants.ListName.LIST_FAVORITE,
            Constants.ListName.LIST_RECOMMEND,
            Constants.ListName.LIST_RECENTLY,
            Constants.ListName.LIST_AGO,
    };
    //private OnFragmentInteractionListener mListener;

    public Lists() {
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
        View view =  inflater.inflate(R.layout.fragment_lists, container, false);
        root = view;
        findViewsById();
        lists.setAdapter(new ListsAdapter());
        lists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startList(position);
            }
        });
        return view;
    }

    private void findViewsById() {
        lists = (ListView)root.findViewById(R.id.lists);
    }

    private void startList(int position) {
        Intent intent = new Intent(getActivity(), SongList.class);
        intent.putExtra("cata", listNames[position]);
        startActivity(intent);
    }
}

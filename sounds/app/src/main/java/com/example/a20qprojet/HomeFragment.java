package com.example.a20qprojet;

import android.content.Intent;
import android.icu.util.GregorianCalendar;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements AdapterFeed.OnEventListener {

    RecyclerView recyclerView;
    ArrayList<EventPost> eventFeedArrayList = new ArrayList<>();
    AdapterFeed adapterFeed;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapterFeed = new AdapterFeed(view.getContext(), eventFeedArrayList,this);
        recyclerView.setAdapter(adapterFeed);

        populateRecyclerView();

        return  view;
    }


    public void populateRecyclerView() {

        EventPost modelFeed = new EventPost(1, 9, 2, R.drawable.ic_baseline_person_24, 0,
                "Sajin Maharjan", "2 hrs", "The cars we drive say a lot about us.");
        eventFeedArrayList.add(modelFeed);
        modelFeed = new EventPost(2, 26, 6, R.drawable.ic_baseline_person_24, 0,
                "Karun Shrestha", "9 hrs", "Don't be afraid of your fears. They're not there to scare you. They're there to \n" +
                "let you know that something is worth it.");
        eventFeedArrayList.add(modelFeed);
        modelFeed = new EventPost(3, 17, 5, R.drawable.ic_baseline_person_24, 0,
                "Lakshya Ram", "13 hrs", "That reflection!!!");
        eventFeedArrayList.add(modelFeed);

        adapterFeed.notifyDataSetChanged();
    }



    @Override
    public void onEventClick(int position) {

        //Toast.makeText(getActivity(),"Touched "+ position,Toast.LENGTH_LONG).show();
        switch (position){
            case 0:
        }
    }


}
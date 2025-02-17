package com.example.a20qprojet;

import android.content.Intent;
import android.icu.util.GregorianCalendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements AdapterFeed.OnEventListener, View.OnClickListener {

    RecyclerView recyclerView;
    ArrayList<EventPost> eventFeedArrayList = new ArrayList<>();
    AdapterFeed adapterFeed;
    FloatingActionButton createPostButton;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    CollectionReference postsCollection;
    Stack<EventPost> postsStack = new Stack<EventPost>();

    FirebaseFirestore st = FirebaseFirestore.getInstance();
    private StorageReference mStorageRef;

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

        postsCollection=db.collection("posts");
        createPost();

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
        createPostButton =(FloatingActionButton) view.findViewById(R.id.createpost);
        createPostButton.setOnClickListener(this);

        final SwipeRefreshLayout pullToRefresh = view.findViewById(R.id.swiperefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
                pullToRefresh.setRefreshing(false);
            }
        });

        return  view;
    }


    public void populateRecyclerView() {

        while(!postsStack.empty()) {
            Log.d(TAG, ""+eventFeedArrayList.size());
            eventFeedArrayList.add(postsStack.pop());
        }
    }

    public void createPost(){

        db.collection("posts").orderBy("id", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {


                                    if(document.get("imageUrl")==null){
                                        Log.d(TAG, "img null");
                                        long id = (Long) document.get("id");
                                        String status= (String) document.get("status");
                                        String postType= (String) document.get("type");
                                        String imageUrl=(String) document.get("imageUrl");

                                        long propic = (long) document.get("propic");

                                        String name = (String) document.get("name");
                                        String time = (String) document.get("time");

                                        EventPost p = new EventPost(id,propic,name,time,status,null,postType);
                                        eventFeedArrayList.add(p);
                                        Log.d(TAG, document.getId() + " => " +p.toString());

                                    }else{
                                        Log.d(TAG, "img exists");
                                        long id = (Long) document.get("id");
                                        String status= (String) document.get("status");
                                        String postType= (String) document.get("type");
                                        String imageUrl=(String) document.get("imageUrl");

                                        long propic = (long) document.get("propic");

                                        String name = (String) document.get("name");
                                        String time = (String) document.get("time");

                                        EventPost p = new EventPost(id,propic,name,time,status,imageUrl,postType);
                                        eventFeedArrayList.add(p);
                                        Log.d(TAG, document.getId() + " => " +p.toString());
                                    }



                            }
                        adapterFeed.notifyDataSetChanged();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }


    @Override
    public void onEventClick(int position) {

        //Toast.makeText(getActivity(),"Touched "+ position,Toast.LENGTH_LONG).show();
        switch (position){
            case 0:
        }
    }


    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.createpost){

            Intent intent = new Intent(HomeFragment.this.getContext(),CreatePost.class);
            getActivity().finish();
            startActivity(intent);

        }
    }

    public void refresh(){
        eventFeedArrayList.clear();
        createPost();
    }
}
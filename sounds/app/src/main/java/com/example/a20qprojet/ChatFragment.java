package com.example.a20qprojet;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment {

    public static class Users {

        private String firstName, lastName, emailAdress, uClass;

        public Users(){ }

        public String getEmailAdress() {
            return emailAdress;
        }

        public void setEmailAdress(String emailAdress) {
            this.emailAdress = emailAdress;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }
        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getuClass() {
            return uClass;
        }

        public void setuClass(String uClass) {
            this.uClass = uClass;
        }

        public Users(String firstName, String lastName, String emailAdress, String uClass) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.emailAdress = emailAdress;
            this.uClass = uClass;
        }
    }
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText editText;
    private ImageButton button;
    private RecyclerView recyclerView;
    private DatabaseReference usersReference;


    public ChatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatFragment newInstance(String param1, String param2) {
        ChatFragment fragment = new ChatFragment();
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
        usersReference = FirebaseDatabase.getInstance().getReference("Users");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Toast.makeText(getActivity(),"Hello",Toast.LENGTH_SHORT).show();

        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editText = (EditText) getView().findViewById(R.id.search_field);
        button = (ImageButton) getView().findViewById(R.id.search_btn);
        recyclerView = (RecyclerView) getView().findViewById(R.id.result_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String searchText = editText.getText().toString();
                firebaseUserSearch(searchText);
            }
        });
    }

    private void firebaseUserSearch(String searchText) {
        Toast.makeText(getActivity(), "Started Search", Toast.LENGTH_LONG).show();

        Query firebaseSearchQuery = usersReference.orderByChild("firstName").startAt(searchText).endAt(searchText + "\uf8ff");

        FirebaseRecyclerAdapter<Users, UsersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Users, UsersViewHolder>(

                Users.class,
                R.layout.resultlist_layout,
                UsersViewHolder.class,
                firebaseSearchQuery

        ) {
            @Override
            protected void populateViewHolder(UsersViewHolder viewHolder, Users model, int position) {

                viewHolder.setDetails(getActivity(), model.getFirstName(),model.getLastName(),model.getuClass());

            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    public static class UsersViewHolder extends  RecyclerView.ViewHolder{

        View mView;

        public UsersViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setDetails(Context ctx, String fN, String lN, String uC){
            Toast.makeText(ctx,"ReHello",Toast.LENGTH_SHORT).show();
            TextView first_name = (TextView) mView.findViewById(R.id.firstName_text);
            TextView last_name = (TextView) mView.findViewById(R.id.lastName_text);
            TextView user_class = (TextView) mView.findViewById(R.id.uClass_text);
            //ImageView user_image = (ImageView) mView.findViewById(R.id.profile_image);

            first_name.setText(fN);
            last_name.setText(lN);
            user_class.setText(uC);

            //Glide.with(ctx).load(userImage).into(user_image);

        }
    }
}
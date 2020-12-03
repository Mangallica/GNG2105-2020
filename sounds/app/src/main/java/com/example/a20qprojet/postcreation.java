package com.example.a20qprojet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.sql.Time;
import java.util.Date;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link postcreation#newInstance} factory method to
 * create an instance of this fragment.
 */
public class postcreation extends Fragment  {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText postText;
    private String postTextString;
    private ImageView postImage,addImageButton;
    private FloatingActionButton closeButton,postButton;
    private FirebaseUser user;
    DatabaseReference postRef ;
    DatabaseReference userRef ;
    private Uri imageUri;
    String postType;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference mStorageRef;

    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    String userID;

    private long postID;
    private String username;


    private static final int PICK_IMAGE = 1;
    private static final int PERMISSION_REQUEST=2;

    public postcreation() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment postcreation.
     */
    // TODO: Rename and change types and number of parameters
    public static postcreation newInstance(String param1, String param2) {
        postcreation fragment = new postcreation();
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

        View view=inflater.inflate(R.layout.fragment_postcreation, container, false);
        mStorageRef = FirebaseStorage.getInstance().getReference("postPictures");
        postText= (EditText) view.findViewById(R.id.postDescription);
        postType=getArguments().getString("Type");
        postTextString=postText.getText().toString();

        closeButton = (FloatingActionButton) view.findViewById(R.id.closebutton2);
        postButton=(FloatingActionButton) view.findViewById(R.id.postbutton2);
        postImage=(ImageView) view.findViewById(R.id.postimage);
        postImage.setVisibility(View.GONE);
        addImageButton=(ImageView) view.findViewById(R.id.addimagebutton2);

        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions, PERMISSION_REQUEST);
                    } else {
                        addImage();
                    }
                }

            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent i=new Intent(getActivity(),HomeActivity.class);

            getActivity().finish();

            startActivity(i);
        }
    });

        postButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            post();
            Intent i=new Intent(getActivity(),HomeActivity.class);
            getActivity().finish();
            startActivity(i);
        }
    });

    user= FirebaseAuth.getInstance().getCurrentUser();
    userID=user.getUid();


    userRef=database.getReference().child("Users");
    postRef=database.getReference().child("posts");

        postRef.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {

            postID=(Long) snapshot.getValue();

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });

        userRef.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            username=snapshot.child(userID).child("firstName").getValue().toString()+" "+snapshot.child(userID).child("lastName").getValue().toString();


        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });

        return view;

}




    public void post(){

        if(postImage.getDrawable()==null){
            Log.w(TAG, "Image is null");

            String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date());

            EventPost p = new EventPost(postID, R.drawable.ic_baseline_person_24, username, currentDateTimeString, postText.getText().toString(), postType);

            db.collection("posts").add(p).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    postRef.setValue(postID + 1);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, "Error adding document", e);
                }
            });
        }else {
            final StorageReference ref = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
            ref.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date());
                            Log.d(TAG, "Document: " + uri.toString());


                            EventPost p = new EventPost(postID, R.drawable.ic_baseline_person_24, username, currentDateTimeString, postText.getText().toString(), uri.toString(), postType);

                            db.collection("posts").add(p).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                    postRef.setValue(postID + 1);
                                }
                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error adding document", e);
                                        }
                                    });
                        }
                    });
                }
            });
        }

    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR= getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cR.getType(uri));
    }

    public void addImage(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,PICK_IMAGE);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode==RESULT_OK) {


            imageUri=data.getData();
            postImage.setImageURI(data.getData());
            postImage.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_REQUEST:{
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    addImage();
                }
            }
        }
    }
}
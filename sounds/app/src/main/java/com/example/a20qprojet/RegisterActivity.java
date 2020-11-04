package com.example.a20qprojet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaCodec;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private FirebaseAuth mAuth;

    private EditText firstName,lastName,password,confirmPassword,emailAddress;
    private Button registerUser;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();

         firstName=findViewById(R.id.firstNameField);
         lastName=findViewById(R.id.lastNameField);
         emailAddress=findViewById(R.id.emailField);
         password=findViewById(R.id.passwordField);
         confirmPassword=findViewById(R.id.confirmPasswordField);

         registerUser=findViewById(R.id.Registerbutton);
         registerUser.setOnClickListener(this);

         progressBar = findViewById(R.id.progressBar);

        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text= adapterView.getItemAtPosition(i).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.Registerbutton:
                registerUser();
                break;
        }
    }

    private void registerUser() {
        //Get text from fields
        final String email= emailAddress.getText().toString().trim();
        final String fName= firstName.getText().toString().trim();
        final String lName= lastName.getText().toString().trim();
        String pwd=password.getText().toString().trim();
        String confirmPwd= confirmPassword.getText().toString().trim();


        if(fName.isEmpty()||lName.isEmpty()||pwd.isEmpty()||confirmPwd.isEmpty()||email.isEmpty()){
            firstName.setError("Please enter your first name");
            firstName.requestFocus();
            return;
        }
        if(lName.isEmpty()){
            lastName.setError("Please enter your Last name");
            lastName.requestFocus();
            return;
        }
        if(email.isEmpty()){
            emailAddress.setError("Please enter a valid email address");
            emailAddress.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailAddress.setError("Please enter a valid email address");
            emailAddress.requestFocus();
            return;
        }

        if(pwd.isEmpty()){
            password.setError("Please enter a valid Password");
            password.requestFocus();
            return;
        }

        if(pwd.length()<6){
            password.setError("Please enter a valid Password");
            password.requestFocus();
            return;
        }

        if(confirmPwd.isEmpty()){
            confirmPassword.setError("Please enter a valid Password");
            confirmPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,pwd)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            GenericUser user = new GenericUser(lName,fName,email);
                            FirebaseDatabase.getInstance().getReference("Users").
                                child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){

                                        Toast.makeText(RegisterActivity.this,"Succesfully Registered",Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                        Intent i = new Intent(RegisterActivity.this,LoginActivity.class);
                                        startActivity(i);

                                    }else {
                                        Toast.makeText(RegisterActivity.this,"Unable to register",Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }else{
                            Toast.makeText(RegisterActivity.this,"Unable to register",Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });



    }
}

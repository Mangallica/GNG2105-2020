package com.example.a20qprojet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView register;
    private EditText emailField,passwordField;

    private Button loginButton;

    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        emailField=findViewById(R.id.EmailField);
        passwordField=findViewById(R.id.passwordField);

        progressBar=findViewById(R.id.progressBar2);

        mAuth=FirebaseAuth.getInstance();

        loginButton = findViewById(R.id.LoginButton);
        loginButton.setOnClickListener(this);
        register = findViewById(R.id.Register);
        register.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.Register:
                startActivity(new Intent(this,RegisterActivity.class));
                break;

            case R.id.LoginButton:
                userLogin();
                break;

        }
    }

    private void userLogin() {
        String email = emailField.getText().toString().trim();
        String pwd = passwordField.getText().toString().trim();

        if(email.isEmpty()){
            emailField.setError("Please enter a valid email address");
            emailField.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailField.setError("Please enter a valid email address");
            emailField.requestFocus();
            return;
        }

        if(pwd.isEmpty()){
            passwordField.setError("Please enter a valid Password");
            passwordField.requestFocus();
            return;
        }
        if(pwd.length()<6){
            passwordField.setError("Please enter a valid Password");
            passwordField.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email,pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this,"Login Successful",Toast.LENGTH_LONG).show();
                    Intent i = new Intent(LoginActivity.this,HomeActivity.class);
                    startActivity(i);
                    LoginActivity.this.finish();
                }else{
                    Toast.makeText(LoginActivity.this,"Login Failed.Please check your credentials",Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}

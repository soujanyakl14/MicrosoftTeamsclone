package com.example.microsoftclone.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.microsoftclone.R;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;


public class signinActivity extends AppCompatActivity {
      EditText emailEt,pswdEt;
      MaterialButton signinButton;
      ProgressBar signinpb;
      String email,password;
      FirebaseAuth mauth;
      FirebaseDatabase database;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);


        //findviewbyids
        emailEt=findViewById(R.id.emailEt);
        pswdEt=findViewById(R.id.pswdEt);
        signinButton=findViewById(R.id.signinbutton);
        signinpb=findViewById(R.id.signinpb);



        //Firebase instances
        mauth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();



        //onclicklisteners

        // signin button
        signinButton.setOnClickListener(v -> signin());
        //create text
        findViewById(R.id.free).setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), signupActivity.class)));

    }

    // method to signin  using firebase authentication and making sure entered details are valid
    public void signin(){
        email=emailEt.getText().toString().trim();
        password=pswdEt.getText().toString().trim();
        if(email.isEmpty()){
            emailEt.setError("Email is required");
            emailEt.requestFocus();
            return;}
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEt.setError(" Enter a valid email id");
            emailEt.requestFocus();
            return;}

        if(password.isEmpty()){
            pswdEt.setError("Password is required");
            pswdEt.requestFocus();
            return;}

        signinpb.setVisibility(View.VISIBLE);
//firebase signin
        mauth.signInWithEmailAndPassword(email, password).addOnCompleteListener((Task<AuthResult> task) -> {
            signinpb.setVisibility(View.GONE);
            if (task.isSuccessful()) {
                finish();
                Intent intent = new Intent(signinActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


//if user is already sign in go to mainactivity
    @Override
    protected void onStart() {
        super.onStart();

        if (mauth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}
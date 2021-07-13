package com.example.microsoftclone.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.microsoftclone.R;
import com.example.microsoftclone.model.User;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Objects;

public class signupActivity extends AppCompatActivity {
        EditText inputfn, inputln, inputemail, inputpswd, inputconfirmpswd;
        MaterialButton create;
        ProgressBar createpb;
        FirebaseAuth mauth;
        FirebaseDatabase database;
        ProgressDialog progressDialog;
        String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Creating Your Account");




//findviewbyids
        inputfn = findViewById(R.id.fnEt);
        inputln = findViewById(R.id.lnEt);
        inputemail = findViewById(R.id.emailEt);
        inputpswd = findViewById(R.id.pswdEt);
        inputconfirmpswd = findViewById(R.id.cfpEt);
        create = findViewById(R.id.createButton);
        createpb = findViewById(R.id.createpg);

//Firebase instances
        mauth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> token=task.getResult());


        //onclicklisteners
        findViewById(R.id.backImage).setOnClickListener(v -> onBackPressed());
        findViewById(R.id.signin).setOnClickListener(v -> {
            finish();
            onBackPressed();
        });
        create.setOnClickListener(v -> signup());
    }


    //method to signup as well as to store userdata in firebase
    public void signup() {
        String email = inputemail.getText().toString().trim();
        String password = inputpswd.getText().toString().trim();
        if (inputfn.getText().toString().trim().isEmpty()) {
            inputfn.setError("Enter your FirstName");
            inputfn.requestFocus();
            return;
        }
        if (inputln.getText().toString().trim().isEmpty()) {
            inputln.setError("Enter your LastName");
            inputln.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            inputemail.setError("Enter your email id");
            inputemail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputemail.setError("Please enter a valid email");
            inputemail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            inputpswd.setError("Enter your password");
            inputpswd.requestFocus();
            return;
        }

        if (password.length() < 6) {
            inputpswd.setError("Minimum length of password should be 6");
            inputpswd.requestFocus();
            return;
        }

        if (inputconfirmpswd.getText().toString().trim().isEmpty()) {
            inputconfirmpswd.setError("Confirm your password");
            inputconfirmpswd.requestFocus();
            return;
        }

        if (!password.equals(inputconfirmpswd.getText().toString())) {
            inputconfirmpswd.setError("Password and Confirm password doesn't match");
            inputconfirmpswd.requestFocus();
            return;
        }

        progressDialog.show();
        //creating account in firebase auth
        mauth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            progressDialog.dismiss();
            if (task.isSuccessful()) {
                //storing data in firebasedatabase as child of users
                String id= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                User user = new User(inputfn.getText().toString().trim(), inputln.getText().toString().trim(), email, token,id);
                database.getReference("Users").child(id).setValue(user);
                finish();
                startActivity(new Intent(signupActivity.this, MainActivity.class));
            }
            else {
                if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                    Toast.makeText(getApplicationContext(), "You are already registered", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}





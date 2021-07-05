package com.example.microsoftclone.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.microsoftclone.R;
import com.example.microsoftclone.utilities.PreferenceManager;
import com.example.microsoftclone.utilities.constants;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class signupActivity extends AppCompatActivity {
     EditText inputfn,inputln,inputemail,inputpswd,inputconfirmpswd;
     MaterialButton create;
     ProgressBar createpb;
     PreferenceManager preferenceManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        findViewById(R.id.backImage).setOnClickListener(v -> onBackPressed());
        findViewById(R.id.signin).setOnClickListener(v -> onBackPressed());
        inputfn= findViewById(R.id.fnEt);
        inputln= findViewById(R.id.lnEt);
        inputemail= findViewById(R.id.emailEt);
        inputpswd= findViewById(R.id.pswdEt);
        inputconfirmpswd= findViewById(R.id.cfpEt);
        create=findViewById(R.id.createButton);
        createpb=findViewById(R.id.createpg);
        preferenceManager=new PreferenceManager(getApplicationContext());
        create.setOnClickListener(v -> {
            if(inputfn.getText().toString().trim().isEmpty())
                Toast.makeText(signupActivity.this, "Enter your First Name", Toast.LENGTH_SHORT).show();
            else if(inputln.getText().toString().trim().isEmpty())
                Toast.makeText(signupActivity.this, "Enter your Last Name", Toast.LENGTH_SHORT).show();
            else if(inputemail.getText().toString().trim().isEmpty())
                Toast.makeText(signupActivity.this, "Enter your mail id", Toast.LENGTH_SHORT).show();
            else if(inputpswd.getText().toString().trim().isEmpty())
                Toast.makeText(signupActivity.this, "Enter your password", Toast.LENGTH_SHORT).show();
            else if(inputfn.getText().toString().trim().isEmpty())
                Toast.makeText(signupActivity.this, "Confirm your password", Toast.LENGTH_SHORT).show();
            else if(!inputpswd.getText().toString().equals(inputconfirmpswd.getText().toString()))
                Toast.makeText(signupActivity.this, "Password and Confirm password doesn't match", Toast.LENGTH_SHORT).show();
            else
                signup();

        });
    }
    private void signup(){
        create.setVisibility(View.INVISIBLE);
        createpb.setVisibility(View.VISIBLE);
        FirebaseFirestore database= FirebaseFirestore.getInstance();
        HashMap<String,Object> user=new HashMap<>();
        user.put(constants.KEY_FIRST_NAME,inputfn.getText().toString());
        user.put(constants.KEY_LAST_NAME,inputln.getText().toString());
        user.put(constants.KEY_EMAIL,inputemail.getText().toString());
        user.put(constants.KEY_PASSWORD,inputpswd.getText().toString());

        database.collection(constants.KEY_COLLECTION_USERS)
                .add(user)
                .addOnSuccessListener(documentReference -> {
                  preferenceManager.putBoolean(constants.KEY_IS_SIGNED_IN,true);
                  preferenceManager.putString(constants.KEY_USER_ID,documentReference.getId());
                  preferenceManager.putString(constants.KEY_FIRST_NAME,inputfn.getText().toString());
                  preferenceManager.putString(constants.KEY_LAST_NAME,inputln.getText().toString());
                  preferenceManager.putString(constants.KEY_EMAIL,inputemail.getText().toString());
                  Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                  startActivity(intent);


                })
                .addOnFailureListener(e -> {
                    create.setVisibility(View.VISIBLE);
                    createpb.setVisibility(View.INVISIBLE);
                    Toast.makeText(signupActivity.this, "Error"+e.getMessage(), Toast.LENGTH_SHORT).show();
                });



    }
}
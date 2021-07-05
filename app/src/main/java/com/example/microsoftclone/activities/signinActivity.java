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
import com.example.microsoftclone.utilities.PreferenceManager;
import com.example.microsoftclone.utilities.constants;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class signinActivity extends AppCompatActivity {
     private EditText emailEt,pswdEt;
     private MaterialButton signinButton;
     private ProgressBar signinpb;
     private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        findViewById(R.id.free).setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), signupActivity.class)));
        emailEt=findViewById(R.id.emailEt);
        pswdEt=findViewById(R.id.pswdEt);
        signinButton=findViewById(R.id.signinbutton);
        signinpb=findViewById(R.id.signinpb);
        preferenceManager=new PreferenceManager(getApplicationContext());
        if(preferenceManager.getBoolean(constants.KEY_IS_SIGNED_IN)){
            Intent intent =new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
        }

        signinButton.setOnClickListener(v -> {
             if(emailEt.getText().toString().trim().isEmpty())
                Toast.makeText(signinActivity.this, "Enter your mail id", Toast.LENGTH_SHORT).show();
             else if(!Patterns.EMAIL_ADDRESS.matcher(emailEt.getText().toString()).matches()){
                 Toast.makeText(signinActivity.this, "Enter a valid email id", Toast.LENGTH_SHORT).show();
             }
            else if(pswdEt.getText().toString().trim().isEmpty())
                Toast.makeText(signinActivity.this, "Enter your password", Toast.LENGTH_SHORT).show();
            else{
                signin();
             }
        });

    }

public void signin(){
    signinButton.setVisibility(View.INVISIBLE);
    signinpb.setVisibility(View.VISIBLE);
    FirebaseFirestore database= FirebaseFirestore.getInstance();
    database.collection(constants.KEY_COLLECTION_USERS)
            .whereEqualTo(constants.KEY_EMAIL,emailEt.getText().toString())
            .whereEqualTo(constants.KEY_PASSWORD,pswdEt.getText().toString())
            .get()
            .addOnCompleteListener(task -> {
                if(task.isSuccessful() && task.getResult()!=null && task.getResult().getDocuments().size()>0){
                    DocumentSnapshot documentSnapshot=task.getResult().getDocuments().get(0);
                    preferenceManager.putBoolean(constants.KEY_IS_SIGNED_IN,true);
                    preferenceManager.putString(constants.KEY_USER_ID, documentSnapshot.getId());
                    preferenceManager.putString(constants.KEY_FIRST_NAME,documentSnapshot.getString(constants.KEY_FIRST_NAME));
                    preferenceManager.putString(constants.KEY_LAST_NAME,documentSnapshot.getString(constants.KEY_LAST_NAME));
                    preferenceManager.putString(constants.KEY_EMAIL,documentSnapshot.getString(constants.KEY_EMAIL));
                    Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                else{
                    signinButton.setVisibility(View.VISIBLE);
                    signinpb.setVisibility(View.INVISIBLE);
                    Toast.makeText(signinActivity.this, "Unable to sign in", Toast.LENGTH_SHORT).show();
                }
            });

}}
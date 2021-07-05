package com.example.microsoftclone.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.microsoftclone.R;
import com.example.microsoftclone.adapter.UserAdapter;
import com.example.microsoftclone.model.user;
import com.example.microsoftclone.utilities.PreferenceManager;
import com.example.microsoftclone.utilities.constants;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    PreferenceManager preferenceManager;
    List<user> Users;
    UserAdapter userAdapter;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//To get FCM_token of user for messaging
        preferenceManager=new PreferenceManager(getApplicationContext());
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if(task.isSuccessful())
                sendfcmtoken(task.getResult());
        });


//bottom navigation view
        bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setOnItemSelectedListener(item -> {
                int ide=item.getItemId();
                if (ide==R.id.navigation_home)
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                else if( ide==R.id.navigation_settings)
                    startActivity(new Intent(getApplicationContext(),SettingsActivity.class));
                else if(ide== R.id.navigation_notifications)
                    startActivity(new Intent(getApplicationContext(),NotificationsActivity.class));
                else if(ide==R.id.navigation_sign_out)
                    signout();
            return false;
        });
//recycler view for the display of list of users
        recyclerView=findViewById(R.id.recyclerview);

        Users=new ArrayList<>();
        userAdapter=new UserAdapter(Users);
        recyclerView.setAdapter(userAdapter);
        swipeRefreshLayout=findViewById(R.id.refresh);
        swipeRefreshLayout.setOnRefreshListener(this::getusers);
        getusers();
    }
//method to get the users and data fom firebase database
    private void getusers(){
        swipeRefreshLayout.setRefreshing(true);
        FirebaseFirestore data=FirebaseFirestore.getInstance();
        data.collection(constants.KEY_COLLECTION_USERS).get().addOnCompleteListener(task -> {
            String userid=preferenceManager.getString(constants.KEY_USER_ID);
            if(task.isSuccessful() && task.getResult()!=null){
                swipeRefreshLayout.setRefreshing(false);
                Users.clear();
                 for(QueryDocumentSnapshot documentSnapshot: task.getResult()){
                     if(userid.equals(documentSnapshot.getId())){
                         preferenceManager.putString(constants.REMOTE_MSG_INVITER_TOKEN,documentSnapshot.getString(constants.KEY_FCM_TOKEN));
                         continue;
                     }
                         user User=new user();
                         User.firstname=documentSnapshot.getString(constants.KEY_FIRST_NAME);
                         User.lastname=documentSnapshot.getString(constants.KEY_LAST_NAME);
                         User.email=documentSnapshot.getString(constants.KEY_EMAIL);
                         User.token=documentSnapshot.getString(constants.KEY_FCM_TOKEN);
                         User.uid=documentSnapshot.getId();
                         Users.add(User);
                 }
                 if(Users.size()>0)
                     userAdapter.notifyDataSetChanged();


            }
            else{
                Toast.makeText(this, "No Users are available", Toast.LENGTH_SHORT).show();
            }
        });
    }
//method to send fcm_token to database(firebase firestore)
    private void sendfcmtoken(String token){
    FirebaseFirestore data= FirebaseFirestore.getInstance();
    DocumentReference documentReference= data.collection(constants.KEY_COLLECTION_USERS)
                                        .document(preferenceManager.getString(constants.KEY_USER_ID));
    documentReference.update(constants.KEY_FCM_TOKEN,token);
}

//method to go back to sign in page when logged out
private void signout(){
    FirebaseFirestore data= FirebaseFirestore.getInstance();
    DocumentReference documentReference= data.collection(constants.KEY_COLLECTION_USERS)
            .document(preferenceManager.getString(constants.KEY_USER_ID));
    HashMap<String,Object> updates=new HashMap<>();
    updates.put(constants.KEY_FCM_TOKEN, FieldValue.delete());
    documentReference.update(updates).addOnSuccessListener(unused -> {
        preferenceManager.clearPreferences();
        startActivity(new Intent(getApplicationContext(),signinActivity.class));
        finish();
    })
    .addOnFailureListener(e -> Toast.makeText(MainActivity.this, "Unable to sign out", Toast.LENGTH_SHORT).show());
}}

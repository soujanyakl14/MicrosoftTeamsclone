package com.example.microsoftclone.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.microsoftclone.R;
import com.example.microsoftclone.adapter.UserAdapter;
import com.example.microsoftclone.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
       BottomNavigationView bottomNavigationView;
       List<User> Users;
       UserAdapter userAdapter;
       RecyclerView recyclerView;
    FirebaseAuth mauth;
       FirebaseDatabase database;
       String s;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //firebase instances
        database=FirebaseDatabase.getInstance();
        mauth=FirebaseAuth.getInstance();

        updatetoken();


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
                { mauth.signOut();
                startActivity(new Intent(MainActivity.this,signinActivity.class));}

            return false;
        });


//recycler view for the display of list of users
        recyclerView=findViewById(R.id.recyclerview);

        Users=new ArrayList<>();
        userAdapter=new UserAdapter(Users,this);
        recyclerView.setAdapter(userAdapter);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        //getting users from database for display
        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                Users.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User User=dataSnapshot.getValue(User.class);
                    assert User != null;
                    if(!User.getId().equals(Objects.requireNonNull(mauth.getCurrentUser()).getUid()))
                       Users.add(User);
                }
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });
    }


    //method to update token in database incase if app is uninstalled
    public void updatetoken(){
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> s=task.getResult());
       DatabaseReference dr =database.getReference("Users");
       dr.child(Objects.requireNonNull(mauth.getCurrentUser()).getUid()).child("token");
       Map<String, Object> updates = new HashMap<>();
       updates.put("token", s);
       dr.updateChildren(updates);
     }
}

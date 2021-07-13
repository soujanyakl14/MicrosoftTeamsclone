package com.example.microsoftclone.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.microsoftclone.R;
import com.example.microsoftclone.adapter.UserAdapter;
import com.example.microsoftclone.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
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
       String id,s;
      SearchView searchView;
      TextView usericon;
      ImageView bgView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        bgView=findViewById(R.id.bgView);

        //firebase instances
        database=FirebaseDatabase.getInstance();
        mauth=FirebaseAuth.getInstance();
        id=mauth.getCurrentUser().getUid();

        //recycler view for the display of list of users
        recyclerView=findViewById(R.id.recyclerview);
        Users=new ArrayList<>();
        userAdapter=new UserAdapter(Users,this);
        recyclerView.setAdapter(userAdapter);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);


        //search of users
        searchView=findViewById(R.id.searchview);
        searchView.setQueryHint("Search Users");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if(!TextUtils.isEmpty(query.trim()))
                {searchusers(query);
                bgView.setAlpha((float) 0.25);}
                else
               {//readusers();
                   bgView.setAlpha((float) 1.0);
               }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(!TextUtils.isEmpty(newText.trim()))
                    searchusers(newText);
                else
                    readusers();
                return false;
            }
        });

        usericon=findViewById(R.id.usericon);

        database.getReference("Users").child(id).addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
               String ui=snapshot.child("firstname").getValue().toString().charAt(0)+ snapshot.child("lastname").getValue().toString().substring(0,1);
               usericon.setText(ui);
           }

           @Override
           public void onCancelled(@NonNull @NotNull DatabaseError error) {

           }
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
               //logout
                else if(ide==R.id.navigation_sign_out)
                { mauth.signOut();
                startActivity(new Intent(MainActivity.this,signinActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
               }

                return false;
        });
    }


    // method to get users from database and  display in mainactivity
    private void readusers() {
        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    Users.clear();
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        User User=dataSnapshot.getValue(User.class);
                        assert User != null;
                        if(!User.getId().equals((mauth.getCurrentUser()).getUid()))
                            Users.add(User);
                    }
                    userAdapter.notifyDataSetChanged();
                }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });
    }



    //method to search user in search view
    private void searchusers(String search) {

        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                Users.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User User=dataSnapshot.getValue(User.class);
                    assert User != null;
                    if(!User.getId().equals((mauth.getCurrentUser()).getUid()))
                    {  if((User.getFirstname()+User.getLastname()).toLowerCase().contains(search.toLowerCase()))
                        Users.add(User);}
                }

                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }




}

package com.example.microsoftclone.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.microsoftclone.R;
import com.example.microsoftclone.adapter.ChatAdapter;
import com.example.microsoftclone.adapter.UserAdapter;
import com.example.microsoftclone.model.message;
import com.example.microsoftclone.model.user;

import java.util.ArrayList;
import java.util.List;

public class dpActivity extends AppCompatActivity {
    RecyclerView chatrecyclerView;
    UserAdapter chatAdapter;
    List<user> messages;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dp);

chatrecyclerView=findViewById(R.id.a);
         messages=new ArrayList<>();
         chatAdapter=new UserAdapter( messages);
        chatrecyclerView.setAdapter(chatAdapter);
        LinearLayoutManager linearLayout=new LinearLayoutManager(this);
        chatrecyclerView.setLayoutManager(linearLayout);
    }
}
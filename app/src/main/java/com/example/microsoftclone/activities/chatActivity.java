package com.example.microsoftclone.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.microsoftclone.R;
import com.example.microsoftclone.adapter.ChatAdapter;
import com.example.microsoftclone.model.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class chatActivity extends AppCompatActivity {
        String receiver_token,sender_token,username, userinfo,sender_id,receiverid;
        TextView textuser,textusericon;
        ImageView vm,am,back,send;
        RecyclerView chatrecyclerView;
        EditText editText;
        FirebaseDatabase database;
        FirebaseAuth mauth;
        ChatAdapter chatAdapter;
        List<Message> messages;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //firebase instances
        database=FirebaseDatabase.getInstance();
        mauth=FirebaseAuth.getInstance();



        //user token
        sender_token= FirebaseMessaging.getInstance().getToken().toString();
        sender_id= Objects.requireNonNull(mauth.getCurrentUser()).getUid();
        receiverid=getIntent().getStringExtra("USER_ID");
        receiver_token=getIntent().getStringExtra("FCM_TOKEN");
        username=getIntent().getStringExtra("FIRST_NAME")+" "+getIntent().getStringExtra("LAST_NAME");
        userinfo =getIntent().getStringExtra("FIRST_NAME").charAt(0)+getIntent().getStringExtra("LAST_NAME").substring(0,1);

        //findviewbyids
        textuser=findViewById(R.id.textuser);
        textusericon=findViewById(R.id.textUsericon);
        vm=findViewById(R.id.vm);
        am=findViewById(R.id.am);
        back=findViewById(R.id.back);
        chatrecyclerView=findViewById(R.id.chatrecyclerview);
        send=findViewById(R.id.send);
        editText=findViewById(R.id.editTextmsg);



        //setting textviews with intents from useradapter
        textuser.setText(username);
        textusericon.setText(userinfo);



       // onclicklistener for videocall
        vm.setOnClickListener(v -> {
            Intent intent=new Intent(getApplicationContext(),outgoingcallActivity.class);
            intent.putExtra("sender_token",sender_token);
            intent.putExtra("receiver_token",receiver_token);
            intent.putExtra("username",username);
            intent.putExtra("usericon", userinfo);
            intent.putExtra("type","Video");
            startActivity(intent);
        });




        // onclicklistener for audiocall
        am.setOnClickListener(v -> {
            Intent intent=new Intent(getApplicationContext(),outgoingcallActivity.class);
            intent.putExtra("sender_token",sender_token);
            intent.putExtra("receiver_token",receiver_token);
            intent.putExtra("username",username);
            intent.putExtra("usericon", userinfo);
            intent.putExtra("type","Audio");
            startActivity(intent);
        });
        back.setOnClickListener(v -> onBackPressed());



        //recycler view and adapter for display of chat messages
        messages=new ArrayList<>();
        chatAdapter=new ChatAdapter( messages,this);
        chatrecyclerView.setAdapter(chatAdapter);
        LinearLayoutManager linearLayout=new LinearLayoutManager(this);
        chatrecyclerView.setLayoutManager(linearLayout);


        final String sender=sender_id+receiverid;
        final String receiver=receiverid+sender_id;




        //getting messages from firebase database
        database.getReference("Chats")
                .child(sender).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                messages.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Message msg=dataSnapshot.getValue(Message.class);
                    messages.add(msg);
                }
                chatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });




//onclicklistener to send chat data to firebase
        send.setOnClickListener(v -> {
            String text = editText.getText().toString();
            if(!text.equals(""))
            { final Message msg = new Message(sender_id, text);
              msg.setTimestamp(String.valueOf(System.currentTimeMillis()));
                editText.setText("");
                database.getReference("Chats")
                        .child(sender)
                        .push()
                        .setValue(msg)
                        .addOnSuccessListener(unused -> database.getReference("Chats")
                                .child(receiver)
                                .push()
                                .setValue(msg).addOnSuccessListener(unused1 -> {
                                }));
            }
            else
                Toast.makeText(this, "Can't send empty message", Toast.LENGTH_SHORT).show();});




}}


package com.example.microsoftclone.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.microsoftclone.R;
import com.example.microsoftclone.adapter.ChatAdapter;
import com.example.microsoftclone.adapter.UserAdapter;
import com.example.microsoftclone.model.message;
import com.example.microsoftclone.model.user;
import com.example.microsoftclone.utilities.PreferenceManager;
import com.example.microsoftclone.utilities.constants;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class chatActivity extends AppCompatActivity {
    String receiver_token,sender_token,username,usericon,senderid,receiverid;
    PreferenceManager preferenceManager;
    TextView textuser,textusericon;
    ImageView vm,am,back,send;
    RecyclerView chatrecyclerView;
    EditText editText;
    FirebaseDatabase database;
    UserAdapter userAdapter;
 //  List<user> Users;
    ChatAdapter chatAdapter;
   List<message> messages;
//List<Integer> messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        preferenceManager=new PreferenceManager(getApplicationContext());
        //user token
        sender_token=preferenceManager.getString(constants.KEY_FCM_TOKEN);
        senderid=preferenceManager.getString(constants.KEY_USER_ID);
        receiverid=getIntent().getStringExtra("USER_ID");
        receiver_token=getIntent().getStringExtra("FCM_TOKEN");
        username=getIntent().getStringExtra("FIRST_NAME")+" "+getIntent().getStringExtra("LAST_NAME");
        usericon=getIntent().getStringExtra("FIRST_NAME").charAt(0)+getIntent().getStringExtra("LAST_NAME").substring(0,1);

        textuser=findViewById(R.id.textuser);
        textusericon=findViewById(R.id.textUsericon);
        vm=findViewById(R.id.vm);
        am=findViewById(R.id.am);
        back=findViewById(R.id.back);
      chatrecyclerView=findViewById(R.id.chatrecyclerview);
        send=findViewById(R.id.send);
        editText=findViewById(R.id.editTextmsg);
        database=FirebaseDatabase.getInstance();

        textuser.setText(username);
        textusericon.setText(usericon);
        vm.setOnClickListener(v -> {
            Intent intent=new Intent(getApplicationContext(),outgoingcallActivity.class);
            intent.putExtra("sender_token",sender_token);
            intent.putExtra("receiver_token",receiver_token);
            intent.putExtra("username",username);
            intent.putExtra("usericon",usericon);
            intent.putExtra("type","Video");
            startActivity(intent);
        });
        am.setOnClickListener(v -> {
            Intent intent=new Intent(getApplicationContext(),outgoingcallActivity.class);
            intent.putExtra("sender_token",sender_token);
            intent.putExtra("receiver_token",receiver_token);
            intent.putExtra("username",username);
            intent.putExtra("usericon",usericon);
            intent.putExtra("type","Audio");
            startActivity(intent);
        });
        back.setOnClickListener(v -> onBackPressed());
  //    recyclerView=findViewById(R.id.recyclerview);

//        Users=new ArrayList<>();
//        userAdapter=new UserAdapter(Users);
//        recyclerView.setAdapter(userAdapter);
//        messages=new ArrayList<>();
//        chatAdapter=new ChatAdapter( messages);
//        chatrecyclerView.setAdapter(chatAdapter);
//        LinearLayoutManager linearLayout=new LinearLayoutManager(this);
//        chatrecyclerView.setLayoutManager(linearLayout);

         messages=new ArrayList<message>();
         chatAdapter=new ChatAdapter( messages,this);
        chatrecyclerView.setAdapter(chatAdapter);
        LinearLayoutManager linearLayout=new LinearLayoutManager(this);
        chatrecyclerView.setLayoutManager(linearLayout);
        final String sender=senderid+receiverid;
        final String receiver=receiverid+senderid;
//
//
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editText.getText().toString();
                final message msg = new message(text, senderid);
                editText.setText("");
                database.getReference().child("Chats")
                        .child(sender)
                        .push()
                        .setValue(msg);
//                    .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void unused) {
//                        database.getReference().child("Chats")
//                                .child(receiver)
//                                .push()
//                                .setValue(msg).addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void unused) {
//
//                            }
//                        });
//                    }});
//
//
           }
       });
    }
}
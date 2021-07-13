package com.example.microsoftclone.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.microsoftclone.R;
import com.example.microsoftclone.networking.apiService;
import com.example.microsoftclone.networking.apiclient;
import com.example.microsoftclone.utilities.constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.Objects;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class outgoingcallActivity extends AppCompatActivity {
    TextView textCallingtype,textUsername,textUsericon;
    ImageView end;
    String calltype,room,callername,callericon,id,sendertoken;
    FirebaseAuth mauth;
    FirebaseDatabase database;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outgoingcall);


        //findviewbyids
        textCallingtype=findViewById(R.id.textCallingtype);
        textUsername=findViewById(R.id.textUsername);
        textUsericon=findViewById(R.id.textUsericon);
        end=findViewById(R.id.end);
        mauth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();

        calltype=getIntent().getStringExtra("type")+" Calling";


        //setting textviews with intents from chatActivity
        textCallingtype.setText(calltype);
        textUsericon.setText(getIntent().getStringExtra("usericon"));
        textUsername.setText(getIntent().getStringExtra("username"));


        end.setOnClickListener((View v) -> {
            callcancel(getIntent().getStringExtra("receiver_token"));
            finish();
        });


        initiatecall(getIntent().getStringExtra("type"),getIntent().getStringExtra("receiver_token"));
    }



    //method to send remote msgs to sever
    private void initiatecall(String calltype,String receiver_token){
        id= Objects.requireNonNull(mauth.getCurrentUser()).getUid();
        database.getReference("Users").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                callername= Objects.requireNonNull(snapshot.child("firstname").getValue()).toString()+" "+ Objects.requireNonNull(snapshot.child("lastname").getValue()).toString();
                callericon= Objects.requireNonNull(snapshot.child("firstname").getValue()).toString().charAt(0)+ Objects.requireNonNull(snapshot.child("lastname").getValue()).toString().substring(0,1);
                sendertoken= Objects.requireNonNull(snapshot.child("token").getValue()).toString();
                try {
                    JSONArray tokens=new JSONArray();
                    tokens.put(receiver_token);
                    JSONObject body=new JSONObject();
                    JSONObject data=new JSONObject();
                    data.put(constants.REMOTE_MSG_TYPE,constants.REMOTE_MSG_CALL);
                    data.put(constants.REMOTE_MSG_CALL_TYPE,calltype);
                    data.put(constants.KEY_USER_NAME,callername);
                    data.put(constants.KEY_USER_ICON,callericon);
                    data.put(constants.REMOTE_MSG_INVITER_TOKEN,sendertoken);
                    room= mauth.getCurrentUser().getUid()+"_"+UUID.randomUUID().toString().substring(0,6);
                    data.put(constants.KEY_ROOM,room);
                    body.put(constants.REMOTE_MSG_DATA,data);
                    body.put(constants.REMOTE_MSG_REGISTRATION_IDS,tokens);
                    sendremotemsgs(body.toString(),constants.REMOTE_MSG_CALL);

                }
                catch (Exception exception){
                    Toast.makeText(getApplicationContext(),exception.getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(outgoingcallActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //method to send data of response(cancelled) of caller to server
    private void callcancel(String receiver_token){
        try {
            JSONArray tokens=new JSONArray();
            tokens.put(receiver_token);
            JSONObject body=new JSONObject();
            JSONObject data=new JSONObject();
            data.put(constants.REMOTE_MSG_TYPE,constants.REMOTE_MSG_CALL_RESPONSE);
            data.put(constants.REMOTE_MSG_CALL_RESPONSE,constants.REMOTE_MSG_CALL_CANCELLED);
            body.put(constants.REMOTE_MSG_DATA,data);
            body.put(constants.REMOTE_MSG_REGISTRATION_IDS,tokens);
            sendremotemsgs(body.toString(),constants.REMOTE_MSG_CALL_RESPONSE);
        }
        catch (Exception exception){
            Toast.makeText(this,exception.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        }
    }


    //api calling using retrofit classes
    private void sendremotemsgs(String remotemsgbody,String type){
        apiclient.getclient().create(apiService.class).sendremotemessages(constants.getHeaders(),remotemsgbody)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NotNull Call<String> call, @NotNull Response<String> response) {
                        if(response.isSuccessful()){
                            if(type.equals(constants.REMOTE_MSG_CALL))
                                Toast.makeText(outgoingcallActivity.this, "Calling succesfully", Toast.LENGTH_SHORT).show();
                             else if(type.equals(constants.REMOTE_MSG_CALL_RESPONSE))
                                Toast.makeText(outgoingcallActivity.this, "Call Cancelled", Toast.LENGTH_SHORT).show();

                        }
                        else
                        {Toast.makeText(outgoingcallActivity.this,response.message(), Toast.LENGTH_SHORT).show();
                            finish();}
                    }

                    @Override
                    public void onFailure(@NotNull Call<String> call, @NotNull Throwable t) {
                        Toast.makeText(outgoingcallActivity.this,t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }



//broadcastreceiver which is triggered by intent of onmessageservice used to get response of receiver to caller
    private final BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String type=intent.getStringExtra(constants.REMOTE_MSG_CALL_RESPONSE);
            if(type!=null){
                if(type.equals(constants.REMOTE_MSG_CALL_ACCEPTED))
                { try {
                    URL server=new URL("https://meet.jit.si");
                    JitsiMeetConferenceOptions.Builder jc =new JitsiMeetConferenceOptions.Builder();
                    jc.setServerURL(server);
                    jc.setWelcomePageEnabled(false);
                    jc.setRoom(room);
                    jc.build();
                    if(getIntent().getStringExtra(constants.REMOTE_MSG_CALL_TYPE)=="Audio")
                    {
                        jc.setAudioOnly(true);
                    }
                    JitsiMeetActivity.launch(outgoingcallActivity.this,jc.build());
                    finish();


                }
                catch (Exception exception){
                    Toast.makeText(outgoingcallActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                }}
                if(type.equals(constants.REMOTE_MSG_CALL_REJECTED))
                {
                    Toast.makeText(context, "Call Rejected", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    };


    //registering broadcastreceiver
    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(broadcastReceiver,new IntentFilter(constants.REMOTE_MSG_CALL_RESPONSE));

    }
//unregestring
    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(broadcastReceiver);
    }
}
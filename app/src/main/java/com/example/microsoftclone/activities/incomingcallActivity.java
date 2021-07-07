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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class incomingcallActivity extends AppCompatActivity {
    TextView textCallingType,textUserName,textUserIcon;
    ImageView imageAccept,imageReject;
    FirebaseDatabase database;
    FirebaseAuth mauth;
     String call;
     public String callername,callericon,id;

    public incomingcallActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incomingcall);
        textCallingType=findViewById(R.id.textCallingtype);
        textUserName=findViewById(R.id.textUsername);
        textUserIcon=findViewById(R.id.textUsericon);
        imageAccept=findViewById(R.id.imageAccept);
        imageReject=findViewById(R.id.imageReject);

        mauth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();


        call="Incoming "+getIntent().getStringExtra(constants.REMOTE_MSG_CALL_TYPE)+" Call";
        textCallingType.setText(call);
//        Toast.makeText(this, getIntent().getStringExtra(constants.KEY_USER_NAME), Toast.LENGTH_SHORT).show();
        textUserName.setText(getIntent().getStringExtra(constants.KEY_USER_NAME));
        textUserIcon.setText(getIntent().getStringExtra(constants.KEY_USER_ICON));
        imageAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callresponse(constants.REMOTE_MSG_CALL_ACCEPTED,getIntent().getStringExtra(constants.REMOTE_MSG_INVITER_TOKEN));
            }
        });
        imageReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callresponse(constants.REMOTE_MSG_CALL_REJECTED,getIntent().getStringExtra(constants.REMOTE_MSG_INVITER_TOKEN));
            }
        });

    }

    private void callresponse(String response,String receiver_token){
        try {
            JSONArray tokens=new JSONArray();
            tokens.put(receiver_token);
            JSONObject body=new JSONObject();
            JSONObject data=new JSONObject();
            data.put(constants.REMOTE_MSG_TYPE,constants.REMOTE_MSG_CALL_RESPONSE);
            data.put(constants.REMOTE_MSG_CALL_RESPONSE,response);
            body.put(constants.REMOTE_MSG_DATA,data);
            body.put(constants.REMOTE_MSG_REGISTRATION_IDS,tokens);
            sendremotemsgs(body.toString(),response);

        }
        catch (Exception exception){
            Toast.makeText(this,exception.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void sendremotemsgs(String remotemsgbody,String type){
        apiclient.getclient().create(apiService.class).sendremotemessages(constants.getHeaders(),remotemsgbody)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NotNull Call<String> call, @NotNull Response<String> response) {
                        if(response.isSuccessful()){
                            if(type.equals(constants.REMOTE_MSG_CALL_ACCEPTED))
                            {
                                try {
                                    URL server=new URL("https://meet.jit.si");
                                    JitsiMeetConferenceOptions.Builder jc =new JitsiMeetConferenceOptions.Builder();
                                    jc.setServerURL(server);
                                    jc.setWelcomePageEnabled(false);
                                    jc.setRoom(getIntent().getStringExtra(constants.KEY_ROOM));
                                    jc.build();
                                    if(getIntent().getStringExtra(constants.REMOTE_MSG_CALL_TYPE)=="Audio")
                                    {
                                        jc.setAudioOnly(true);
                                    }
                                    JitsiMeetActivity.launch(incomingcallActivity.this,jc.build());

                                    finish();


                                }
                                catch (Exception exception){
                                    Toast.makeText(incomingcallActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }
                            else
                            { Toast.makeText(incomingcallActivity.this, "Call Rejected", Toast.LENGTH_SHORT).show();
                                finish();}
                        }
                        else
                        {Toast.makeText(incomingcallActivity.this,response.message(), Toast.LENGTH_SHORT).show();
                            finish();}

                    }

                    @Override
                    public void onFailure(@NotNull Call<String> call, @NotNull Throwable t) {
                        Toast.makeText(incomingcallActivity.this,t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
}

    private BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String type=intent.getStringExtra(constants.REMOTE_MSG_CALL_RESPONSE);
            if(type!=null){
                if(type.equals(constants.REMOTE_MSG_CALL_CANCELLED))
                {Toast.makeText(context, "Call Cancelled", Toast.LENGTH_SHORT).show();
                  finish();
                }
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(broadcastReceiver,new IntentFilter(constants.REMOTE_MSG_CALL_RESPONSE));

    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(broadcastReceiver);
    }
}

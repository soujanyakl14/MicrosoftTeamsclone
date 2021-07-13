package com.example.microsoftclone.firebase;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.microsoftclone.activities.incomingcallActivity;
import com.example.microsoftclone.utilities.constants;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.jetbrains.annotations.NotNull;

public class MessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull @org.jetbrains.annotations.NotNull String s) {
        super.onNewToken(s);

    }

    //
    @Override
    public void onMessageReceived(@NotNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String type = remoteMessage.getData().get(constants.REMOTE_MSG_TYPE);

        if (type != null) {
            //if the remotemsg received is of type call send the intent to incomingcall activity of receiver
            if (type.equals(constants.REMOTE_MSG_CALL)) {
                Intent intent = new Intent(getApplicationContext(), incomingcallActivity.class);
                intent.putExtra(constants.REMOTE_MSG_CALL_TYPE, remoteMessage.getData().get(constants.REMOTE_MSG_CALL_TYPE));
                intent.putExtra(constants.KEY_USER_NAME, remoteMessage.getData().get(constants.KEY_USER_NAME));
                intent.putExtra(constants.KEY_USER_ICON, remoteMessage.getData().get(constants.KEY_USER_ICON));
                intent.putExtra(constants.REMOTE_MSG_INVITER_TOKEN, remoteMessage.getData().get(constants.REMOTE_MSG_INVITER_TOKEN));
                intent.putExtra(constants.KEY_ROOM, remoteMessage.getData().get(constants.KEY_ROOM));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }


            //if it is of call response then create a intent to trigger broadcastmanager to send the respective responses to caller and receiver
            else if (type.equals(constants.REMOTE_MSG_CALL_RESPONSE)) {
                Intent intent = new Intent(constants.REMOTE_MSG_CALL_RESPONSE);
                intent.putExtra(constants.REMOTE_MSG_CALL_RESPONSE, remoteMessage.getData().get(constants.REMOTE_MSG_CALL_RESPONSE));
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
            }
        } }}


//        }
//    }


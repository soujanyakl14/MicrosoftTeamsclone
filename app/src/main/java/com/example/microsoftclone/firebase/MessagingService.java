package com.example.microsoftclone.firebase;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.microsoftclone.activities.incomingcallActivity;
import com.example.microsoftclone.tpActivity;
import com.example.microsoftclone.utilities.constants;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MessagingService extends FirebaseMessagingService {
//    public static final String CHANNEL_ID ="SWC";
//    private static final String CHANNEL_NAME = "TIME_TABLE";
//    private static final String CHANNEL_DES = "ONESTOP_TIMETABLE";
//
//    NotificationManager mNotificationManager;
//
//
//    @Override
//    public void onMessageReceived( RemoteMessage remoteMessage) {
//        super.onMessageReceived(remoteMessage);
//        Intent intent
//                =new Intent(getApplicationContext(), tpActivity.class);
//
//
//// playing audio and vibration when user se request
//        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
//        r.play();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//            r.setLooping(false);
//        }
//
//        // vibration
//        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//        long[] pattern = {100, 300, 300, 300};
//        v.vibrate(pattern, -1);
//
//
//        //int resourceImage = getResources().getIdentifier(remoteMessage.getNotification().getIcon(), "drawable", getPackageName());
//
//
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
////            builder.setSmallIcon(R.drawable.icontrans);
//            builder.setSmallIcon(R.drawable.ic_launcher_background);
//        } else {
////            builder.setSmallIcon(R.drawable.icon_kritikar);
//            builder.setSmallIcon(R.drawable.ic_launcher_background);
//        }
//
//
//
//        Intent resultIntent = new Intent(getApplicationContext(), chatActivity.class);// why the hell did you use a intent here
//        // resultIntent will help us to take that page where we needed to head after clicking on the notification part
//
//
//        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//
//        builder.setContentTitle(remoteMessage.getNotification().getTitle());
//        builder.setContentText(remoteMessage.getNotification().getBody());
//        builder.setContentIntent(pendingIntent);
//        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(remoteMessage.getNotification().getBody()));
//        builder.setAutoCancel(true);
//        builder.setPriority(Notification.PRIORITY_MAX);
//
//        mNotificationManager =
//                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
//
//
//
//
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
//        {
//            String channelId = "Your_channel_id";
//            NotificationChannel channel = new NotificationChannel(
//                    channelId,
//                    "Channel human readable title",
//                    NotificationManager.IMPORTANCE_HIGH);
//            mNotificationManager.createNotificationChannel(channel);
//            builder.setChannelId(channelId);
//        }
//
//
//
//        // notificationId is a unique int for each notification that you must define
//        mNotificationManager.notify(100, builder.build());
//

    //  }
    @Override
    public void onNewToken(@NonNull @org.jetbrains.annotations.NotNull String s) {
        super.onNewToken(s);

    }

    //
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
//        Intent intent = new Intent(getApplicationContext(), tpActivity.class);
//        startActivity(intent);
        String type = remoteMessage.getData().get(constants.REMOTE_MSG_TYPE);



        if (type != null) {
            if (type.equals(constants.REMOTE_MSG_CALL)) {
                Intent intent = new Intent(getApplicationContext(), incomingcallActivity.class);
                intent.putExtra(constants.REMOTE_MSG_CALL_TYPE, remoteMessage.getData().get(constants.REMOTE_MSG_CALL_TYPE));
                intent.putExtra(constants.KEY_USER_NAME, remoteMessage.getData().get(constants.KEY_USER_NAME));
                intent.putExtra(constants.KEY_USER_ICON, remoteMessage.getData().get(constants.KEY_USER_ICON));
                intent.putExtra(constants.REMOTE_MSG_INVITER_TOKEN,remoteMessage.getData().get(constants.REMOTE_MSG_INVITER_TOKEN));
                intent.putExtra(constants.KEY_ROOM,remoteMessage.getData().get(constants.KEY_ROOM));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
            else if(type.equals(constants.REMOTE_MSG_CALL_RESPONSE)){
                Intent intent=new Intent(constants.REMOTE_MSG_CALL_RESPONSE);
                intent.putExtra(constants.REMOTE_MSG_CALL_RESPONSE,remoteMessage.getData().get(constants.REMOTE_MSG_CALL_RESPONSE));
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
            }

        }
    }
}

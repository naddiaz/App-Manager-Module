package com.naddiaz.tfg.bletasker.webservices;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.naddiaz.tfg.bletasker.R;
import com.naddiaz.tfg.bletasker.database.Work;
import com.naddiaz.tfg.bletasker.utils.RSACrypt;
import com.naddiaz.tfg.bletasker.utils.UserPrefecences;
import com.naddiaz.tfg.bletasker.widget.MainActivity;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class GcmIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;


    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public GcmIntentService() {
        super("GcmIntentService");
    }
    public static final String TAG = "GCM Demo";

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM will be
             * extended in the future with new message types, just ignore any message types you're
             * not interested in, or that you don't recognize.
             */
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification("Send error",extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("Deleted messages on server", extras.toString());
                // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                new WSLoadWorks(getApplication(),new UserPrefecences(getApplication()).readPreferences().getHash()).getWorks();
                sendNotification(intent.getStringExtra("sign"),intent.getStringExtra("message"));
                Log.i(TAG, "Received: " + extras.toString());
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(String sign, String message) {
        String description = RSACrypt.decrypt(message);
        if(!RSACrypt.verify(sign, description)) {
            Log.i(TAG, "No show message because origin doesn't know");
        }
        else {
            mNotificationManager = (NotificationManager)
                    this.getSystemService(Context.NOTIFICATION_SERVICE);

            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(Work.PENDING_TASK, true);
            PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, 0);


            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.logo_mini)
                            .setContentTitle("BLE Tasker")
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText(description))
                            .setContentText(description);

            mBuilder.setContentIntent(contentIntent);
            mBuilder.setAutoCancel(true);

            //Sonido
            Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            mBuilder.setSound(defaultSound);

            //Vibración
            long[] pattern = new long[]{0, 100, 100, 100, 100, 100, 1000};
            mBuilder.setVibrate(pattern);

            mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        }
    }
}
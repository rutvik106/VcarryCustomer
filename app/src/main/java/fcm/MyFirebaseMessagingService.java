package fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import extra.Log;
import io.fusionbit.vcarrycustomer.ActivityHome;
import io.fusionbit.vcarrycustomer.App;
import io.fusionbit.vcarrycustomer.R;

/**
 * Created by rutvik on 1/17/2017 at 9:59 AM.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService
{
    private static final String TAG = App.APP_TAG + MyFirebaseMessagingService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage message)
    {
        String from = message.getFrom();
        Map data = message.getData();
        Log.i(TAG, "DATA: " + data.toString() + " FROM: " + from);
        sendNotification(data);
    }

    private void sendNotification(Map data)
    {
        Intent intent = new Intent(this, ActivityHome.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.logo_small)
                .setContentTitle(data.get("title").toString())
                .setContentText(data.get("message").toString())
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

}

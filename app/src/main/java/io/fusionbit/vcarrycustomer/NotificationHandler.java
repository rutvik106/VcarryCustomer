package io.fusionbit.vcarrycustomer;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import models.BookedTrip;

/**
 * Created by rutvik on 1/24/2017 at 12:38 PM.
 */

public class NotificationHandler
{
    private static final String TAG = App.APP_TAG + NotificationHandler.class.getSimpleName();

    Context context;

    RemoteMessage remoteMessage;

    Map data;

    Realm realm;

    public NotificationHandler(Context context, RealmConfiguration realmConfiguration, RemoteMessage remoteMessage)
    {
        this.context = context;
        this.realm = Realm.getInstance(realmConfiguration);
        this.remoteMessage = remoteMessage;
        this.data = remoteMessage.getData();
    }

    public void handleNotification()
    {
        Map data = remoteMessage.getData();

        switch (data.get("type").toString())
        {
            case Constants.NotificationType.SIMPLE:
                showNotification(0);
                break;
            case Constants.NotificationType.TRIP_CONFIRMATION:
                confirmTrip();
                break;
            case Constants.NotificationType.DRIVER_ALLOCATED:
                break;
        }

    }

    private void confirmTrip()
    {
        Log.i(TAG, "extra: " + data.get("extra").toString());
        JsonElement jsonElement = new Gson().fromJson(data.get("extra").toString(), JsonElement.class);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        int customerTripId = Integer.parseInt(jsonObject.get("customer_trip_id").getAsString());
        final BookedTrip bookedTrip = new BookedTrip(customerTripId);
        bookedTrip.setStatus(1);
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(bookedTrip);
        realm.commitTransaction();
        Log.i(TAG, "Customer Trip ID: " + customerTripId);
        showNotification(customerTripId);
    }


    private void showNotification(int id)
    {
        Intent intent = new Intent(context, ActivityHome.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, id /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.logo_small)
                .setContentTitle(data.get("title").toString())
                .setContentText(data.get("message").toString())
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(id /* ID of notification */, notificationBuilder.build());
    }

}

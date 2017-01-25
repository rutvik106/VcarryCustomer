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

import org.json.JSONException;
import org.json.JSONObject;

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

    JSONObject data;

    JSONObject extra;

    Realm realm;

    public NotificationHandler(Context context, RealmConfiguration realmConfiguration, RemoteMessage remoteMessage)
    {
        this.context = context;
        this.realm = Realm.getInstance(realmConfiguration);
        this.remoteMessage = remoteMessage;
        try
        {
            this.data = new JSONObject(remoteMessage.getData().get("data"));
            this.extra = new JSONObject(remoteMessage.getData().get("extra"));
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    public void handleNotification()
    {

        for (Map.Entry<String, String> entry : remoteMessage.getData().entrySet())
        {
            String key = entry.getKey();
            String value = entry.getValue();
            Log.d(TAG, "key, " + key + " value " + value);
        }

        try
        {
            switch (data.get("type").toString())
            {
                case Constants.NotificationType.SIMPLE:
                    showNotification(0, data.getString("title"), data.getString("message"));
                    break;
                case Constants.NotificationType.TRIP_CONFIRMATION:
                    confirmTrip();
                    break;
                case Constants.NotificationType.DRIVER_ALLOCATED:
                    confirmDriver();
                    break;
            }
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    private void confirmDriver()
    {
        try
        {
            final int customerTripId = Integer.parseInt(extra.getString("customer_trip_id"));
            final int driverTripId = Integer.parseInt(extra.getString("trip_id"));
            final String driverName = extra.getString("driver_name");

            final BookedTrip bookedTrip = realm.where(BookedTrip.class)
                    .equalTo("tripId", customerTripId).findFirst();
            realm.beginTransaction();
            bookedTrip.setStatus(Constants.DRIVER_ALLOCATED);
            bookedTrip.setDriverName(driverName);
            bookedTrip.setDriverTripId(extra.getString("trip_id"));
            bookedTrip.setDriverDeviceToken(extra.getString("driver_device_token"));
            realm.copyToRealmOrUpdate(bookedTrip);
            realm.commitTransaction();
            Log.i(TAG, "Customer Trip ID: " + customerTripId);
            showNotification(driverTripId, data.getString("title"), "Driver " + driverName +
                    "has been allocated to your trip. From " + bookedTrip.getTripFrom() +
                    " To " + bookedTrip.getTripTo());
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    private void confirmTrip()
    {
        try
        {
            int customerTripId = Integer.parseInt(extra.getString("customer_trip_id"));
            final BookedTrip bookedTrip = realm.where(BookedTrip.class)
                    .equalTo("tripId", customerTripId).findFirst();
            realm.beginTransaction();
            bookedTrip.setStatus(Constants.TRIP_CONFIRMED);
            realm.copyToRealmOrUpdate(bookedTrip);
            realm.commitTransaction();
            Log.i(TAG, "Customer Trip ID: " + customerTripId);
            showNotification(customerTripId, data.getString("title"), data.getString("message"));
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
    }


    private void showNotification(final int id, final String title, final String message)
    {
        Intent intent = new Intent(context, ActivityHome.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, id /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.logo_small)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(id /* ID of notification */, notificationBuilder.build());
    }

}

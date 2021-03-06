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

import api.API;
import api.RetrofitCallbacks;
import apimodels.TripByCustomerId;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import models.BookedTrip;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by rutvik on 1/24/2017 at 12:38 PM.
 */

public class NotificationHandler {
    private static final String TAG = App.APP_TAG + NotificationHandler.class.getSimpleName();

    final Context context;

    final RemoteMessage remoteMessage;
    final API api;
    private final RealmConfiguration realmConfiguration;
    JSONObject data;
    JSONObject extra;

    public NotificationHandler(Context context, RealmConfiguration realmConfiguration, API api,
                               RemoteMessage remoteMessage) {
        this.context = context;
        this.realmConfiguration = realmConfiguration;
        this.api = api;
        this.remoteMessage = remoteMessage;
        try {
            if (remoteMessage != null) {
                if (remoteMessage.getData() != null) {
                    if (remoteMessage.getData().get("data") != null) {
                        this.data = new JSONObject(remoteMessage.getData().get("data"));
                        this.extra = new JSONObject(remoteMessage.getData().get("extra"));
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void handleNotification() {

        for (Map.Entry<String, String> entry : remoteMessage.getData().entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            Log.d(TAG, "key, " + key + " value " + value);
        }

        try {
            if (data != null) {
                if (data.get("type") != null) {
                    switch (data.get("type").toString()) {
                        case Constants.NotificationType.SIMPLE:
                            showNotification(0, data.getString("title"), data.getString("message"));
                            break;
                        case Constants.NotificationType.TRIP_REJECTION:
                            rejectTrip();
                            break;
                        case Constants.NotificationType.TRIP_CONFIRMATION:
                            confirmTrip();
                            break;
                        case Constants.NotificationType.DRIVER_ALLOCATED:
                            confirmDriver();
                            break;
                        case Constants.NotificationType.TRIP_STARTED:
                            tripStarted();
                            break;
                        case Constants.NotificationType.TRIP_FINISHED:
                            finishTrip();
                            break;
                        case Constants.NotificationType.DRIVER_CURRENT_LOCATION:
                            sendBroadcastGetDriverCurrentLocation();
                            break;

                    }
                } else {
                    showNotification(0, "Test Notification", "Test Notification");
                }
            } else {
                showNotification(0, "Test Notification", "Test Notification");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void rejectTrip() {

        try {
            final int customerTripIdInt = Integer.parseInt(extra.getString("customer_trip_id"));
            final String customerTripId = extra.getString("customer_trip_id");

            final Realm realm = Realm.getInstance(realmConfiguration);
            final BookedTrip bookedTrip = realm.where(BookedTrip.class)
                    .equalTo("customerTripId", customerTripId)
                    .findFirst();

            if (bookedTrip != null) {
                realm.beginTransaction();
                bookedTrip.deleteFromRealm();
                realm.commitTransaction();
            }

            showNotification(customerTripIdInt, data.getString("title"), data.getString("message"));

        } catch (JSONException e) {
            try {
                showNotification(0, data.getString("title"), data.getString("message"));
            } catch (JSONException e1) {
                showNotification(0, "V-carry Trip Rejected", "Your Trip  has been Rejected By Vcarry");
            }
        }
    }

    private void sendBroadcastGetDriverCurrentLocation() {
        try {
            final Intent locationDetails = new Intent(Constants.NotificationType.DRIVER_CURRENT_LOCATION);
            locationDetails.putExtra("LAT", extra.getString("lat"));
            locationDetails.putExtra("LNG", extra.getString("lng"));
            try {
                locationDetails.putExtra("DRIVER_TYPE", extra.getInt("driver_type"));
            } catch (Exception e) {
                locationDetails.putExtra("DRIVER_TYPE", -1);
            }
            context.sendBroadcast(locationDetails);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void confirmTrip() {
        try {
            try {
                String customerTripId = extra.getString("customer_trip_id");
                int tripIdInt = Integer.parseInt(extra.getString("trip_id"));

                final Realm r = Realm.getInstance(realmConfiguration);

                final BookedTrip b = r.where(BookedTrip.class)
                        .equalTo("customerTripId", customerTripId).findFirst();

                final String tripId = extra.getString("trip_id");
                final String fare = extra.getString("fare");
                final String tripNo = extra.getString("trip_no");

                if (b == null) {
                    showNotification(tripIdInt, data.getString("title"), data.getString("message"));
                    api.getTripDetailsByTripId(extra.getString("trip_id"),
                            new RetrofitCallbacks<TripByCustomerId>() {
                                @Override
                                public void onResponse(Call<TripByCustomerId> call, Response<TripByCustomerId> response) {
                                    super.onResponse(call, response);
                                    if (response.isSuccessful()) {
                                        final Realm realm = Realm.getInstance(realmConfiguration);
                                        realm.beginTransaction();
                                        realm.copyToRealmOrUpdate(response.body());
                                        realm.commitTransaction();
                                    }
                                }
                            });
                    return;
                }

                final BookedTrip bookedTrip = r.copyFromRealm(b);

                api.getTripDetailsByTripId(tripId,
                        new RetrofitCallbacks<TripByCustomerId>() {
                            @Override
                            public void onResponse(Call<TripByCustomerId> call, Response<TripByCustomerId> response) {
                                super.onResponse(call, response);
                                if (response.isSuccessful()) {
                                    if (response.body() != null) {
                                        final Realm realm = Realm.getInstance(realmConfiguration);
                                        realm.beginTransaction();
                                        bookedTrip.setTripStatus(Constants.TRIP_STATUS_NEW);
                                        bookedTrip.setTripId(tripId);
                                        bookedTrip.setTripFare(fare);
                                        bookedTrip.setTripNo(tripNo);
                                        TripByCustomerId tripByCustomerId = realm.copyToRealmOrUpdate(response.body());
                                        tripByCustomerId.setNote(bookedTrip.getNote());
                                        bookedTrip.setCustomerTripId(tripByCustomerId);
                                        realm.copyToRealmOrUpdate(bookedTrip);
                                        realm.commitTransaction();
                                    }
                                }
                            }
                        });
                Log.i(TAG, "tripIdInt Trip ID: " + tripIdInt);
                showNotification(tripIdInt, data.getString("title"), data.getString("message"));
            } catch (NumberFormatException e) {
                showNotification(0, data.getString("title"), data.getString("message"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void confirmDriver() {
        try {
            try {
                final String customerTripId = extra.getString("customer_trip_id");
                final int tripIdInt = Integer.parseInt(extra.getString("trip_id"));
                final String driverName = extra.getString("driver_name");
                final String driverNumber = extra.getString("driver_contact_no");
                final String tripId = extra.getString("trip_id");
                final String driverDeviceToken = extra.getString("driver_device_token");

                final Realm r = Realm.getInstance(realmConfiguration);

                final BookedTrip b = r.where(BookedTrip.class)
                        .equalTo("customerTripId", extra.getString("customer_trip_id")).findFirst();

                if (b == null) {
                    showNotification(tripIdInt, data.getString("title"), data.getString("message"));
                    api.getTripDetailsByTripId(tripId,
                            new RetrofitCallbacks<TripByCustomerId>() {
                                @Override
                                public void onResponse(Call<TripByCustomerId> call, Response<TripByCustomerId> response) {
                                    super.onResponse(call, response);
                                    if (response.isSuccessful()) {
                                        final Realm realm = Realm.getInstance(realmConfiguration);
                                        realm.beginTransaction();
                                        realm.copyToRealmOrUpdate(response.body());
                                        realm.commitTransaction();
                                    }
                                }
                            });
                    return;
                }

                final BookedTrip bookedTrip = r.copyFromRealm(b);

                api.getTripDetailsByTripId(tripId,
                        new RetrofitCallbacks<TripByCustomerId>() {
                            @Override
                            public void onResponse(Call<TripByCustomerId> call, Response<TripByCustomerId> response) {
                                super.onResponse(call, response);
                                if (response.isSuccessful()) {
                                    final TripByCustomerId tripByCustomerId = response.body();
                                    final Realm realm = Realm.getInstance(realmConfiguration);
                                    realm.beginTransaction();
                                    bookedTrip.setTripStatus(Constants.TRIP_STATUS_DRIVER_ALLOCATED);
                                    bookedTrip.setDriverContactNo(driverNumber);
                                    bookedTrip.setDriverName(driverName);
                                    bookedTrip.setTripId(tripId);
                                    bookedTrip.setDriverDeviceToken(driverDeviceToken);
                                    tripByCustomerId.setDriverNumber(driverNumber);
                                    tripByCustomerId.setDeviceToken(driverDeviceToken);
                                    bookedTrip.setCustomerTripId(realm
                                            .copyToRealmOrUpdate(tripByCustomerId));
                                    realm.copyToRealmOrUpdate(bookedTrip);
                                    realm.commitTransaction();
                                }
                            }
                        });


                Log.i(TAG, "Customer Trip ID: " + customerTripId);
                showBigNotification(tripIdInt, data.getString("title"), "Motorist " + driverName +
                        "has been allocated to your trip. From " + bookedTrip.getTripFrom() +
                        " To " + bookedTrip.getTripTo());
            } catch (NumberFormatException e) {
                showNotification(0, data.getString("title"), data.getString("message"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void tripStarted() {
        try {
            try {
                final String customerTripId = extra.getString("customer_trip_id");
                final int tripIdInt = Integer.parseInt(extra.getString("trip_id"));
                final String tripId = extra.getString("trip_id");

                final Realm r = Realm.getInstance(realmConfiguration);

                final BookedTrip b = r.where(BookedTrip.class)
                        .equalTo("customerTripId", customerTripId).findFirst();

                if (b == null) {
                    showNotification(tripIdInt, data.getString("title"), data.getString("message"));
                    api.getTripDetailsByTripId(tripId,
                            new RetrofitCallbacks<TripByCustomerId>() {
                                @Override
                                public void onResponse(Call<TripByCustomerId> call, Response<TripByCustomerId> response) {
                                    super.onResponse(call, response);
                                    if (response.isSuccessful()) {
                                        final Realm realm = Realm.getInstance(realmConfiguration);
                                        realm.beginTransaction();
                                        realm.copyToRealmOrUpdate(response.body());
                                        realm.commitTransaction();
                                    }
                                }
                            });
                    return;
                }

                final BookedTrip bookedTrip = r.copyFromRealm(b);

                api.getTripDetailsByTripId(tripId,
                        new RetrofitCallbacks<TripByCustomerId>() {
                            @Override
                            public void onResponse(Call<TripByCustomerId> call, Response<TripByCustomerId> response) {
                                super.onResponse(call, response);
                                if (response.isSuccessful()) {
                                    final Realm realm = Realm.getInstance(realmConfiguration);
                                    realm.beginTransaction();
                                    bookedTrip.setTripStatus(Constants.TRIP_STATUS_TRIP_STARTED);
                                    bookedTrip.setCustomerTripId(realm
                                            .copyToRealmOrUpdate(response.body()));
                                    realm.copyToRealmOrUpdate(bookedTrip);
                                    realm.commitTransaction();
                                }
                            }
                        });

                Log.i(TAG, "Customer Trip ID: " + customerTripId);
                showNotification(tripIdInt, data.getString("title"), data.getString("message"));
            } catch (NumberFormatException e) {
                showNotification(0, data.getString("title"), data.getString("message"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void finishTrip() {
        try {
            try {
                final String customerTripId = extra.getString("customer_trip_id");
                final int tripIdInt = Integer.parseInt(extra.getString("trip_id"));
                final String tripId = extra.getString("trip_id");

                final Realm r = Realm.getInstance(realmConfiguration);
                final BookedTrip b = r.where(BookedTrip.class)
                        .equalTo("customerTripId", customerTripId).findFirst();

                if (b == null) {
                    showNotification(tripIdInt, data.getString("title"), data.getString("message"));
                    api.getTripDetailsByTripId(tripId,
                            new RetrofitCallbacks<TripByCustomerId>() {
                                @Override
                                public void onResponse(Call<TripByCustomerId> call, Response<TripByCustomerId> response) {
                                    super.onResponse(call, response);
                                    if (response.isSuccessful()) {
                                        final Realm realm = Realm.getInstance(realmConfiguration);
                                        realm.beginTransaction();
                                        realm.copyToRealmOrUpdate(response.body());
                                        realm.commitTransaction();
                                    }
                                }
                            });
                    return;
                }

                final BookedTrip bookedTrip = r.copyFromRealm(b);

                api.getTripDetailsByTripId(tripId,
                        new RetrofitCallbacks<TripByCustomerId>() {
                            @Override
                            public void onResponse(Call<TripByCustomerId> call, Response<TripByCustomerId> response) {
                                super.onResponse(call, response);
                                if (response.isSuccessful()) {
                                    final Realm realm = Realm.getInstance(realmConfiguration);
                                    realm.beginTransaction();
                                    bookedTrip.setTripStatus(Constants.TRIP_STATUS_FINISHED);
                                    bookedTrip.setCustomerTripId(realm
                                            .copyToRealmOrUpdate(response.body()));
                                    realm.copyToRealmOrUpdate(bookedTrip);
                                    realm.commitTransaction();
                                }
                            }
                        });

                showNotification(tripIdInt, data.getString("title"), data.getString("message"));
            } catch (NumberFormatException e) {
                showNotification(0, data.getString("title"), data.getString("message"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void showNotification(final int id, final String title, final String message) {
        Intent intent = new Intent(context, ActivityHome.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, id /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.logo_small)
                .setContentTitle(title)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(message))
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(id /* ID of notification */, notificationBuilder.build());
    }

    private void showBigNotification(final int id, final String title, final String message) {
        Intent intent = new Intent(context, ActivityHome.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, id /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.logo_small)
                .setContentTitle(title)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(message))
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(id /* ID of notification */, notificationBuilder.build());
    }

}

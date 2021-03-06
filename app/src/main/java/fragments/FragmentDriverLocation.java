package fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import api.API;
import api.RetrofitCallbacks;
import apimodels.DriverLocationDetails;
import apimodels.FcmResponse;
import apimodels.TripByCustomerId;
import extra.Log;
import fcm.FCM;
import io.fusionbit.vcarrycustomer.App;
import io.fusionbit.vcarrycustomer.Constants;
import io.fusionbit.vcarrycustomer.R;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by rutvik on 1/26/2017 at 4:52 PM.
 */

public class FragmentDriverLocation extends Fragment implements OnMapReadyCallback {

    private static final String TAG = App.APP_TAG + FragmentDriverLocation.class.getSimpleName();

    public GoogleMap mMap;
    public boolean isReady = false;
    TripByCustomerId trip;
    Realm realm;
    FCM.FCMCallbackListener listener;
    private SyncedMapFragment mapFragment;
    private Context context;
    private String tripId;

    private Callbacks callbacks;
    private Call<DriverLocationDetails> callGetLocation;

    public static FragmentDriverLocation newInstance(String tripId, Context context, Realm realm,
                                                     FCM.FCMCallbackListener listener, Callbacks callbackListener) {
        FragmentDriverLocation fragmentDriverLocation = new FragmentDriverLocation();
        fragmentDriverLocation.context = context;
        fragmentDriverLocation.tripId = tripId;
        fragmentDriverLocation.realm = realm;
        fragmentDriverLocation.listener = listener;
        fragmentDriverLocation.callbacks = callbackListener;
        return fragmentDriverLocation;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_driver_location, container, false);

        mapFragment = (SyncedMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.frag_map);

        getTripDetailsFromRealm();

        loadMapNow();

        return view;
    }

    private void getTripDetailsFromRealm() {
        trip = realm
                .copyFromRealm(realm.
                        where(TripByCustomerId.class).equalTo("tripId", tripId).findFirst());

        Log.i(TAG, "TRIP ID: " + tripId);
        Log.i(TAG, "TRIP DRIVER NAME: " + trip.getDriverName());
        android.util.Log.i(TAG, "getDriverDeviceToken: " + trip.getDriverDeviceToken());
        android.util.Log.i(TAG, "getDeviceToken: " + trip.getDeviceToken());
    }

    private void loadMapNow() {
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        isReady = true;
        mMap = googleMap;

        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setTiltGesturesEnabled(false);
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(false);

        getDriverLocation();
        //sendPushRequestForDriverLocation();
    }

    private void getDriverLocation() {
        cancelApiCall();
        callGetLocation = API.getInstance().getDriverRecentLocation(trip.getDriverId(), new RetrofitCallbacks<DriverLocationDetails>() {

            @Override
            public void onResponse(Call<DriverLocationDetails> call, Response<DriverLocationDetails> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getResponse().getError().getError_code() == 0) {
                            if (response.body().getResponse().getDriver_details().getRecent_location() != null) {
                                showDriverLocation(response.body().getResponse().getDriver_details().getRecentLatLng(),
                                        response.body().getResponse().getDriver_details().getLocation_time_ago());
                            } else {
                                Toast.makeText(getContext(), "Driver location not found, Please try again later.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "API ERROR: " + response.body().getResponse().getError().getMsg(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Response body is null", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Response not successful http code " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DriverLocationDetails> call, Throwable t) {
                super.onFailure(call, t);
                if (!call.isCanceled()) {
                    Toast.makeText(getContext(), "FAILED: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendPushRequestForDriverLocation() {
        Log.i(TAG, "sendPushRequestForDriverLocation");
        if (trip != null) {
            final String customerDeviceToken = PreferenceManager.getDefaultSharedPreferences(context)
                    .getString(Constants.FCM_INSTANCE_ID, null);

            final JSONObject extra = new JSONObject();
            try {
                extra.put("customer_device_token", customerDeviceToken);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            final String deviceToken = trip.getDeviceToken();

            if (deviceToken == null) {
                Toast.makeText(context, R.string.motorist_no_app, Toast.LENGTH_SHORT).show();
                callbacks.driverDeviceTokenNotFound(trip);
            } else {
                new AsyncTask<Void, Void, Void>() {
                    int status = 0;

                    FcmResponse response;

                    @Override
                    protected Void doInBackground(Void... voids) {
                        FCM.sendPushNotification(Constants.NotificationType.GET_DRIVER_LOCATION,
                                "Location Request", "Sending your location to customer",
                                deviceToken, extra, new FCM.FCMCallbackListener() {
                                    @Override
                                    public void sentNotificationHttpStatus(int statusCode,
                                                                           FcmResponse fcmResponse) {
                                        status = statusCode;
                                        response = fcmResponse;
                                    }
                                });
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        listener.sentNotificationHttpStatus(status, response);
                    }
                }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }

            /*if (trip.getDriverLastKnownLocation() != null) {
                showDriverLocation(trip.getDriverLastKnownLocation());
            }*/

        } else {
            Log.i(TAG, "BOOKED TRIP WAS NOT FOUND IN REALM");
            Toast.makeText(context, "This tip was not found, Cannot get location.", Toast.LENGTH_SHORT).show();
        }

    }

    public void showDriverLocation(LatLng latLng, String time) {
        MarkerOptions m = new MarkerOptions();
        m.position(latLng);
        m.title(trip.getDriverName() + " (" + time + ")");
        final Marker marker = mMap.addMarker(m);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
        marker.showInfoWindow();

        /*trip.setLastKnownDriverLocation(latLng.latitude, latLng.longitude);
        trip.setDriverLocationLastAccessTime(System.currentTimeMillis());
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(trip);
        realm.commitTransaction();*/

        callbacks.locationFound();

    }

    public void cancelApiCall() {
        if (callGetLocation != null) {
            if (!callGetLocation.isCanceled()) {
                callGetLocation.cancel();
            }
            callGetLocation = null;
        }
    }

    public interface Callbacks {
        void driverDeviceTokenNotFound(TripByCustomerId trip);

        void locationFound();
    }

}

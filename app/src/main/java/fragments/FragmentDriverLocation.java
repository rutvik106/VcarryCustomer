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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import extra.Log;
import fcm.FCM;
import io.fusionbit.vcarrycustomer.App;
import io.fusionbit.vcarrycustomer.Constants;
import io.fusionbit.vcarrycustomer.R;
import io.realm.Realm;
import models.BookedTrip;

/**
 * Created by rutvik on 1/26/2017 at 4:52 PM.
 */

public class FragmentDriverLocation extends Fragment implements OnMapReadyCallback
{

    private static final String TAG = App.APP_TAG + FragmentDriverLocation.class.getSimpleName();

    public GoogleMap mMap;
    public boolean isReady = false;
    BookedTrip bookedTrip;
    Realm realm;
    private SyncedMapFragment mapFragment;
    private Context context;
    private int customerTripId;

    public static FragmentDriverLocation newInstance(int customerTripId, Context context, Realm realm)
    {
        FragmentDriverLocation fragmentDriverLocation = new FragmentDriverLocation();
        fragmentDriverLocation.context = context;
        fragmentDriverLocation.customerTripId = customerTripId;
        fragmentDriverLocation.realm = realm;
        return fragmentDriverLocation;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_driver_location, container, false);

        mapFragment = (SyncedMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.frag_map);

        getTripDetailsFromRealm();

        loadMapNow();

        return view;
    }

    private void getTripDetailsFromRealm()
    {
        bookedTrip = realm
                .copyFromRealm(realm.
                        where(BookedTrip.class).equalTo("customerTripId", customerTripId).findFirst());
    }

    private void loadMapNow()
    {
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        isReady = true;
        mMap = googleMap;

        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setTiltGesturesEnabled(false);
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(false);

        sendPushRequestForDriverLocation();
    }

    private void sendPushRequestForDriverLocation()
    {
        if (bookedTrip != null)
        {
            final String customerDeviceToken = PreferenceManager.getDefaultSharedPreferences(context)
                    .getString(Constants.FCM_INSTANCE_ID, null);

            final JSONObject extra = new JSONObject();
            try
            {
                extra.put("customer_device_token", customerDeviceToken);
            } catch (JSONException e)
            {
                e.printStackTrace();
            }

            new AsyncTask<Void, Void, Void>()
            {
                @Override
                protected Void doInBackground(Void... voids)
                {
                    FCM.sendPushNotification(Constants.NotificationType.GET_DRIVER_LOCATION,
                            "Location Request", "Sending your location to customer",
                            bookedTrip.getDriverDeviceToken(), extra);
                    return null;
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        } else
        {
            Log.i(TAG, "BOOKED TRIP WAS NOT FOUND IN REALM");
        }

    }

    public void showDriverLocation(LatLng latLng)
    {
        MarkerOptions m = new MarkerOptions();
        m.position(latLng);
        m.title(bookedTrip.getDriverName());
        final Marker marker = mMap.addMarker(m);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
        marker.showInfoWindow();
    }
}

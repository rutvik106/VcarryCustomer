package io.fusionbit.vcarrycustomer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import javax.inject.Inject;

import apimodels.TripByCustomerId;
import butterknife.BindView;
import butterknife.ButterKnife;
import fcm.FCM;
import fragments.FragmentDriverLocation;
import io.realm.Realm;

public class ActivityDriverLocation extends AppCompatActivity implements FCM.FCMCallbackListener, FragmentDriverLocation.Callbacks
{

    final OnReceiveDriverLocation onReceiveDriverLocation = new OnReceiveDriverLocation();
    @BindView(R.id.fl_driverLocationFragmentContainer)
    FrameLayout flDriverLocationFragmentContainer;
    FragmentDriverLocation fragmentDriverLocation;
    @Inject
    Realm realm;
    @BindView(R.id.activity_driver_location)
    CoordinatorLayout activityDriverLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_location);

        ButterKnife.bind(this);

        ((App) getApplication()).getUser().inject(this);

        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setTitle(R.string.getting_driver_location);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        fragmentDriverLocation = FragmentDriverLocation
                .newInstance(getIntent().getStringExtra(Constants.TRIP_ID), this, realm, this, this);


        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl_driverLocationFragmentContainer, fragmentDriverLocation)
                .commitAllowingStateLoss();


    }

    @Override
    protected void onStart()
    {
        super.onStart();
        registerReceiver(onReceiveDriverLocation, new IntentFilter(Constants
                .NotificationType.DRIVER_CURRENT_LOCATION));
    }


    @Override
    protected void onStop()
    {
        unregisterReceiver(onReceiveDriverLocation);
        super.onStop();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void sentNotificationHttpStatus(final int statusCode)
    {
        Toast.makeText(this, "Http Status Code: " + statusCode, Toast.LENGTH_SHORT).show();

        if (statusCode > 300)
        {
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    if (getSupportActionBar() != null)
                    {
                        getSupportActionBar().setTitle(R.string.cannot_get_location);
                        Toast.makeText(ActivityDriverLocation.this, "STATUS: " + statusCode, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public void driverDeviceTokenNotFound(TripByCustomerId trip)
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                if (getSupportActionBar() != null)
                {
                    getSupportActionBar().setTitle(R.string.not_eligible_for_tracking);
                }
                Snackbar.make(activityDriverLocation, R.string.not_eligible_for_tracking, Snackbar.LENGTH_INDEFINITE)
                        .show();
            }
        });

    }

    class OnReceiveDriverLocation extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context context, Intent intent)
        {
            final LatLng latLng = new LatLng(Double.parseDouble(intent.getStringExtra("LAT")),
                    Double.parseDouble(intent.getStringExtra("LNG")));

            final int driverType = intent.getIntExtra("DRIVER_TYPE", -1);

            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    if (getSupportActionBar() != null)
                    {
                        getSupportActionBar().setTitle(getString(R.string.driver_location));
                    }

                    if (driverType == 1)
                    {
                        Snackbar.make(activityDriverLocation, R.string.cannot_track_multi_trip_drivers, Snackbar.LENGTH_INDEFINITE)
                                .show();
                    }

                }
            });

            fragmentDriverLocation.showDriverLocation(latLng);
        }
    }

}

package io.fusionbit.vcarrycustomer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.gms.maps.model.LatLng;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import fcm.FCM;
import fragments.FragmentDriverLocation;
import io.realm.Realm;

public class ActivityDriverLocation extends AppCompatActivity implements FCM.FCMCallbackListener
{

    final OnReceiveDriverLocation onReceiveDriverLocation = new OnReceiveDriverLocation();
    @BindView(R.id.fl_driverLocationFragmentContainer)
    FrameLayout flDriverLocationFragmentContainer;
    FragmentDriverLocation fragmentDriverLocation;
    @Inject
    Realm realm;

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
                .newInstance(getIntent().getStringExtra(Constants.TRIP_ID), this, realm, this);


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
    public void sentNotificationHttpStatus(int statusCode)
    {
        if (statusCode > 300)
        {
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    if (getSupportActionBar() != null)
                    {
                        getSupportActionBar().setTitle("Cannot Get Location");
                    }
                }
            });
        }
    }

    class OnReceiveDriverLocation extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context context, Intent intent)
        {
            final LatLng latLng = new LatLng(Double.parseDouble(intent.getStringExtra("LAT")),
                    Double.parseDouble(intent.getStringExtra("LNG")));

            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    if (getSupportActionBar() != null)
                    {
                        getSupportActionBar().setTitle(getString(R.string.driver_location));
                    }
                }
            });

            fragmentDriverLocation.showDriverLocation(latLng);
        }
    }

}

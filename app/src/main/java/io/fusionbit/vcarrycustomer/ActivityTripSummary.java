package io.fusionbit.vcarrycustomer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import apimodels.TripByCustomerId;
import butterknife.BindView;
import io.realm.Realm;
import models.BookedTrip;

public class ActivityTripSummary extends BaseActivity {

    private static final String TAG = App.APP_TAG + ActivityTripSummary.class.getSimpleName();

    Snackbar sbNoInternet;
    @BindView(R.id.tv_pendingTrips)
    TextView tvPendingTrips;
    @BindView(R.id.tv_finishedTrips)
    TextView tvFinishedTrips;
    @BindView(R.id.tv_canceledByCustomerTrips)
    TextView tvCanceledByCustomerTrips;
    @BindView(R.id.tv_canceledByDriverTrips)
    TextView tvCanceledByDriverTrips;
    @BindView(R.id.tv_canceledByVcarryTrips)
    TextView tvCanceledByVcarryTrips;

    Realm realm = Realm.getDefaultInstance();
    @BindView(R.id.tv_activeTrips)
    TextView tvActiveTrips;

    public static void start(Context context) {
        context.startActivity(new Intent(context, ActivityTripSummary.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.trip_summary);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        getTotalTripsFromRealm();

        getActiveTripsFromRealm();

        getPendingTripsFromRealm();

        getFinishedTripsFromRealm();

        getCanceledByDriverTripsFromRealm();

        getCancelledByCustomerTripsFromRealm();

        getCancelledByVcarryTripsFromRealm();

    }

    private void getTotalTripsFromRealm() {
        final int count = realm.where(TripByCustomerId.class)
                .findAll()
                .size();

        Log.i(TAG, "TOTAL TRIPS IN REALM: " + count);
    }

    private void getActiveTripsFromRealm() {
        final int count = realm.where(TripByCustomerId.class)
                .equalTo("tripStatus", Constants.TRIP_STATUS_TRIP_STARTED)
                .or()
                .equalTo("tripStatus", Constants.TRIP_STATUS_DRIVER_ALLOCATED)
                .or()
                .equalTo("tripStatus", Constants.TRIP_STATUS_LOADING)
                .or()
                .equalTo("tripStatus", Constants.TRIP_STATUS_UNLOADING)
                .or()
                .equalTo("tripStatus", Constants.TRIP_STATUS_NEW)
                .findAll()
                .size();

        tvActiveTrips.setText(count + "");
    }

    private void getCancelledByVcarryTripsFromRealm() {
        final int count = realm.where(TripByCustomerId.class)
                .equalTo("tripStatus", Constants.TRIP_STATUS_CANCELLED_BY_VCARRY)
                .findAll()
                .size();

        tvCanceledByVcarryTrips.setText(count + "");
    }

    private void getCancelledByCustomerTripsFromRealm() {
        final int count = realm.where(TripByCustomerId.class)
                .equalTo("tripStatus", Constants.TRIP_STATUS_CANCELLED_BY_CUSTOMER)
                .findAll()
                .size();

        tvCanceledByCustomerTrips.setText(count + "");
    }

    private void getCanceledByDriverTripsFromRealm() {
        final int count = realm.where(TripByCustomerId.class)
                .equalTo("tripStatus", Constants.TRIP_STATUS_CANCELLED_BY_DRIVER)
                .findAll()
                .size();

        tvCanceledByDriverTrips.setText(count + "");
    }

    private void getFinishedTripsFromRealm() {
        final int count = realm.where(TripByCustomerId.class)
                .equalTo("tripStatus", Constants.TRIP_STATUS_FINISHED)
                .findAll()
                .size();

        tvFinishedTrips.setText(count + "");
    }

    private void getPendingTripsFromRealm() {
        final int count = realm.where(BookedTrip.class)
                .equalTo("tripStatus", Constants.TRIP_STATUS_PENDING)
                .findAll()
                .size();

        tvPendingTrips.setText(count + "");
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_trip_summary;
    }

    @Override
    protected void internetNotAvailable() {
        /*if (sbNoInternet == null)
        {
            sbNoInternet = Snackbar.make(clActivityTripSummary, R.string.no_internet, Snackbar.LENGTH_INDEFINITE);
            sbNoInternet.show();
        }*/
    }

    @Override
    protected void internetAvailable() {
        /*if (sbNoInternet != null)
        {
            if (sbNoInternet.isShown())
            {
                sbNoInternet.dismiss();
                sbNoInternet = null;
            }
        }*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}

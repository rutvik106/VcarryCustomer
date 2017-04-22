package io.fusionbit.vcarrycustomer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.MenuItem;
import android.widget.TextView;

import javax.inject.Inject;

import apimodels.TripByCustomerId;
import butterknife.BindView;
import io.realm.Realm;
import models.BookedTrip;

public class ActivityTripSummary extends BaseActivity
{

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
    @BindView(R.id.cl_activityTripSummary)
    CoordinatorLayout clActivityTripSummary;

    @Inject
    Realm realm;

    public static void start(Context context)
    {
        context.startActivity(new Intent(context, ActivityTripSummary.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setTitle(R.string.trip_summary);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ((App) getApplication()).getUser().inject(this);

        getPendingTripsFromRealm();

        getFinishedTripsFromRealm();

        getCanceledByDriverTripsFromRealm();

        getCancelledByCustomerTripsFromRealm();

        getCancelledByVcarryTripsFromRealm();

    }

    private void getCancelledByVcarryTripsFromRealm()
    {
        final int count = realm.where(TripByCustomerId.class)
                .equalTo("tripStatus", Constants.TRIP_STATUS_CANCELLED_BY_VCARRY)
                .findAll()
                .size();

        tvCanceledByVcarryTrips.append(count + "");
    }

    private void getCancelledByCustomerTripsFromRealm()
    {
        final int count = realm.where(TripByCustomerId.class)
                .equalTo("tripStatus", Constants.TRIP_STATUS_CANCELLED_BY_CUSTOMER)
                .findAll()
                .size();

        tvCanceledByCustomerTrips.append(count + "");
    }

    private void getCanceledByDriverTripsFromRealm()
    {
        final int count = realm.where(TripByCustomerId.class)
                .equalTo("tripStatus", Constants.TRIP_STATUS_CANCELLED_BY_DRIVER)
                .findAll()
                .size();

        tvCanceledByDriverTrips.append(count + "");
    }

    private void getFinishedTripsFromRealm()
    {
        final int count = realm.where(TripByCustomerId.class)
                .equalTo("tripStatus", Constants.TRIP_STATUS_FINISHED)
                .findAll()
                .size();

        tvFinishedTrips.append(count + "");
    }

    private void getPendingTripsFromRealm()
    {
        final int count = realm.where(BookedTrip.class)
                .equalTo("tripStatus", Constants.TRIP_STATUS_PENDING)
                .findAll()
                .size();

        tvPendingTrips.append(count + "");
    }

    @Override
    protected int getLayoutResourceId()
    {
        return R.layout.activity_trip_summary;
    }

    @Override
    protected void internetNotAvailable()
    {
        if (sbNoInternet == null)
        {
            sbNoInternet = Snackbar.make(clActivityTripSummary, R.string.no_internet, Snackbar.LENGTH_INDEFINITE);
            sbNoInternet.show();
        }
    }

    @Override
    protected void internetAvailable()
    {
        if (sbNoInternet != null)
        {
            if (sbNoInternet.isShown())
            {
                sbNoInternet.dismiss();
                sbNoInternet = null;
            }
        }
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
}

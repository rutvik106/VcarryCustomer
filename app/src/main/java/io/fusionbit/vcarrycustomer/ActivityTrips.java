package io.fusionbit.vcarrycustomer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import adapters.TripsAdapter;
import apimodels.TripByCustomerId;
import butterknife.BindView;
import io.realm.Realm;
import models.BookedTrip;

public class ActivityTrips extends BaseActivity
{

    @BindView(R.id.rv_accountTrips)
    RecyclerView rvTrips;
    @Inject
    Realm realm;
    private String tripsType = null;
    private TripsAdapter adapter;
    private List<TripByCustomerId> trips;

    public static void start(Context context, String accountTripType, List<TripByCustomerId> trips)
    {
        final Intent i = new Intent(context, ActivityTrips.class);
        i.putExtra(Constants.ACCOUNT_TRIP_TYPE, accountTripType);
        i.putParcelableArrayListExtra(Constants.PARCELABLE_TRIP_LIST,
                new ArrayList<Parcelable>(trips));
        context.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        ((App) getApplication()).getUser().inject(this);

        tripsType = getIntent().getStringExtra(Constants.ACCOUNT_TRIP_TYPE);

        trips = getIntent().getParcelableArrayListExtra(Constants.PARCELABLE_TRIP_LIST);

        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            switch (tripsType)
            {
                case Constants.AccountTripType.TODAY:
                    getSupportActionBar().setTitle(R.string.trips_today);
                    break;
                case Constants.AccountTripType.THIS_MONTH:
                    getSupportActionBar().setTitle(R.string.trips_this_month);
                    break;
                case Constants.AccountTripType.TOTAL:
                    getSupportActionBar().setTitle(R.string.total_trips);
                    break;
            }
        }

        rvTrips.setLayoutManager(new LinearLayoutManager(this));
        rvTrips.setHasFixedSize(true);

        adapter = new TripsAdapter(this);

        rvTrips.setAdapter(adapter);

        for (TripByCustomerId trip : trips)
        {
            final BookedTrip bookedTrip = realm.where(BookedTrip.class)
                    .equalTo("tripId", trip.getTripId()).findFirst();

            if (bookedTrip != null)
            {
                realm.beginTransaction();
                bookedTrip.setStatus(Integer.valueOf(trip.getTripStatus()));
                bookedTrip.setTripCost(trip.getFare());
                trip.setBookedTrip(realm
                        .copyFromRealm(realm
                                .copyToRealmOrUpdate(bookedTrip)));
                realm.commitTransaction();
            }

            adapter.addTrip(trip);
        }

    }

    @Override
    protected int getLayoutResourceId()
    {
        return R.layout.activity_trips;
    }

    @Override
    protected void internetNotAvailable()
    {

    }

    @Override
    protected void internetAvailable()
    {

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

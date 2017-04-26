package io.fusionbit.vcarrycustomer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import java.util.List;

import javax.inject.Inject;

import adapters.TripDetailsAdapter;
import api.API;
import api.RetrofitCallbacks;
import apimodels.TripByCustomerId;
import butterknife.BindView;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import models.BookedTrip;
import retrofit2.Call;
import retrofit2.Response;

public class ActivityOnGoingTrips extends BaseActivity
{
    public static final String TAG = App.APP_TAG + ActivityOnGoingTrips.class.getSimpleName();

    @BindView(R.id.rv_onGoingTrips)
    RecyclerView rvOnGoingTrips;
    @BindView(R.id.cl_activityOnGoingTrips)
    CoordinatorLayout clActivityOnGoingTrips;

    Snackbar sbNoInternet;

    @Inject
    Realm realm;

    @Inject
    API api;
    TripDetailsAdapter adapter;
    @BindView(R.id.fl_noActiveTrips)
    FrameLayout flNoActiveTrips;
    private Call<List<TripByCustomerId>> call;
    private RealmResults<TripByCustomerId> tripByCustomerIds;

    public static void start(Context context)
    {
        context.startActivity(new Intent(context, ActivityOnGoingTrips.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setTitle(R.string.active_trips);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ((App) getApplication()).getUser().inject(this);

        setupRecyclerView();

        getOnGoingTripsFromRealm();

        getTripsFromAPI();

    }

    private void setupRecyclerView()
    {
        rvOnGoingTrips.setLayoutManager(new LinearLayoutManager(this));
        rvOnGoingTrips.setHasFixedSize(true);
        adapter = new TripDetailsAdapter(this);
        rvOnGoingTrips.setAdapter(adapter);
    }

    private void getOnGoingTripsFromRealm()
    {

        final RealmResults<BookedTrip> bookedTrips =
                realm.where(BookedTrip.class)
                        .equalTo("tripStatus", Constants.TRIP_STATUS_PENDING)
                        .findAll();

        for (BookedTrip bookedTrip : bookedTrips)
        {
            Log.i(TAG, "REALM TRIP STATUS: " + bookedTrip.getTripStatus());
            adapter.addBookedTrip(BookedTrip.bakePendingTrip(bookedTrip));
        }

        bookedTrips.addChangeListener(new RealmChangeListener<RealmResults<BookedTrip>>()
        {
            @Override
            public void onChange(RealmResults<BookedTrip> element)
            {
                Log.i(TAG, "BOOKED TRIP ON CHANGED REALM");
                if (element.isEmpty())
                {
                    adapter.clearPendingTrips();
                } else
                {
                    Log.i(TAG, "BOOKED TRIP CHANGE SIZE: " + element.size());
                }
            }
        });

        tripByCustomerIds =
                realm.where(TripByCustomerId.class)
                        .notEqualTo("tripStatus", Constants.TRIP_STATUS_FINISHED)
                        .notEqualTo("tripStatus", Constants.TRIP_STATUS_CANCELLED_BY_CUSTOMER)
                        .notEqualTo("tripStatus", Constants.TRIP_STATUS_CANCELLED_BY_DRIVER)
                        .notEqualTo("tripStatus", Constants.TRIP_STATUS_CANCELLED_BY_VCARRY)
                        .findAll();

        for (TripByCustomerId trip : tripByCustomerIds)
        {
            adapter.addBookedTrip(trip);
        }


        tripByCustomerIds.addChangeListener(new RealmChangeListener<RealmResults<TripByCustomerId>>()
        {
            @Override
            public void onChange(RealmResults<TripByCustomerId> element)
            {

                adapter.clearAll();

                final RealmResults<BookedTrip> bookedTrips =
                        realm.where(BookedTrip.class)
                                .equalTo("tripStatus", Constants.TRIP_STATUS_PENDING)
                                .findAll();


                for (BookedTrip bookedTrip : bookedTrips)
                {
                    Log.i(TAG, "REALM TRIP STATUS: " + bookedTrip.getTripStatus());
                    adapter.addBookedTrip(BookedTrip.bakePendingTrip(bookedTrip));
                }


                Log.i(TAG, "tripByCustomerIds SIZE: " + tripByCustomerIds.size());
                Log.i(TAG, "element SIZE: " + element.size());
                for (TripByCustomerId trip : element)
                {
                    Log.i(TAG, "TRIP STATUS: " + trip.getStatus() + " STATUS NO: " + trip.getTripStatus());
                    Log.i(TAG, "ADDING TRIP FROM ON CHANGE REALM");
                    adapter.addBookedTrip(trip);
                }

                Log.i(TAG, "ADAPTER SIZE: " + adapter.getItemCount());

                if (adapter.getItemCount() > 0)
                {
                    flNoActiveTrips.setVisibility(View.GONE);
                } else
                {
                    flNoActiveTrips.setVisibility(View.VISIBLE);
                }

            }
        });

        Log.i(TAG, "ADAPTER SIZE: " + adapter.getItemCount());

        if (adapter.getItemCount() > 0)
        {
            flNoActiveTrips.setVisibility(View.GONE);
        }

    }

    private void getTripsFromAPI()
    {
        final String customerId = PreferenceManager.getDefaultSharedPreferences(this)
                .getString(Constants.CUSTOMER_ID, null);
        if (customerId != null)
        {
            if (call != null)
            {
                call.cancel();
            }
            call = api.getTripsByCustomerId(customerId, new RetrofitCallbacks<List<TripByCustomerId>>()
            {
                @Override
                public void onResponse(Call<List<TripByCustomerId>> call, Response<List<TripByCustomerId>> response)
                {
                    super.onResponse(call, response);
                    if (response.isSuccessful())
                    {
                        realm.beginTransaction();
                        for (TripByCustomerId trip : response.body())
                        {
                            realm.copyToRealmOrUpdate(trip);
                        }
                        realm.commitTransaction();
                    }
                }
            });
        }
    }

    @Override
    protected int getLayoutResourceId()
    {
        return R.layout.activity_on_going_trips;
    }

    @Override
    protected void internetNotAvailable()
    {
        if (sbNoInternet == null)
        {
            sbNoInternet = Snackbar.make(clActivityOnGoingTrips, R.string.no_internet, Snackbar.LENGTH_INDEFINITE);
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
                sbNoInternet.show();
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

    @Override
    protected void onDestroy()
    {
        if (call != null)
        {
            call.cancel();
        }
        super.onDestroy();
    }
}

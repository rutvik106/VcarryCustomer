package io.fusionbit.vcarrycustomer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import adapters.TripsAdapter;
import api.API;
import api.RetrofitCallbacks;
import apimodels.TripByCustomerId;
import butterknife.BindView;
import io.realm.Realm;
import models.BookedTrip;
import retrofit2.Call;
import retrofit2.Response;

public class ActivityTrips extends BaseActivity
{

    final String tripStatus = Constants.TRIP_STATUS_FINISHED + ","
            + Constants.TRIP_STATUS_CANCELLED_BY_DRIVER + ","
            + Constants.TRIP_STATUS_CANCELLED_BY_CUSTOMER;

    @BindView(R.id.rv_accountTrips)
    RecyclerView rvTrips;
    @Inject
    Realm realm;
    @Inject
    API api;
    @BindView(R.id.fl_noTripsFound)
    FrameLayout flNoTripsFound;
    @BindView(R.id.fl_somethingWentWrong)
    FrameLayout flSomethingWentWrong;
    @BindView(R.id.fl_loadingTrips)
    FrameLayout flLoadingTrips;
    @BindView(R.id.cl_activityTrips)
    CoordinatorLayout clActivityTrips;
    Snackbar sbNoInternet;
    private String tripsType = null;
    private TripsAdapter adapter;
    private List<TripByCustomerId> trips = new ArrayList<>();
    private String customerId;

    public static void start(Context context, String accountTripType, List<TripByCustomerId> trips)
    {
        final Intent i = new Intent(context, ActivityTrips.class);
        i.putExtra(Constants.ACCOUNT_TRIP_TYPE, accountTripType);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        ((App) getApplication()).getUser().inject(this);

        tripsType = getIntent().getStringExtra(Constants.ACCOUNT_TRIP_TYPE);

        customerId = PreferenceManager.getDefaultSharedPreferences(this)
                .getString(Constants.CUSTOMER_ID, null);

        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            switch (tripsType)
            {
                case Constants.AccountTripType.TODAY:
                    getSupportActionBar().setTitle(R.string.trips_today);
                    getTripForToday();
                    break;
                case Constants.AccountTripType.THIS_MONTH:
                    getSupportActionBar().setTitle(R.string.trips_this_month);
                    getTripForThisMonth();
                    break;
                case Constants.AccountTripType.TOTAL:
                    getSupportActionBar().setTitle(R.string.total_trips);
                    getTotalTrips();
                    break;
            }
        }

        rvTrips.setLayoutManager(new LinearLayoutManager(this));
        rvTrips.setHasFixedSize(true);

        adapter = new TripsAdapter(this);

        rvTrips.setAdapter(adapter);

    }

    @Override
    protected int getLayoutResourceId()
    {
        return R.layout.activity_trips;
    }


    private void getTripForToday()
    {
        final RetrofitCallbacks<List<TripByCustomerId>> onGetTripSummary =
                new RetrofitCallbacks<List<TripByCustomerId>>()
                {

                    @Override
                    public void onResponse(Call<List<TripByCustomerId>> call, Response<List<TripByCustomerId>> response)
                    {
                        super.onResponse(call, response);
                        if (response.isSuccessful())
                        {
                            if (response.body() != null)
                            {
                                for (TripByCustomerId tripsByDriverMail : response.body())
                                {
                                    trips.add(tripsByDriverMail);
                                }
                                updateRealmObjectsAndAddTripsToAdapter();
                            }
                        } else
                        {
                            flLoadingTrips.setVisibility(View.GONE);
                            flSomethingWentWrong.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<TripByCustomerId>> call, Throwable t)
                    {
                        super.onFailure(call, t);
                        flLoadingTrips.setVisibility(View.GONE);
                        flSomethingWentWrong.setVisibility(View.VISIBLE);
                    }
                };

        final String today = Utils.getDate(Calendar.getInstance().getTime());

        api.getTripSummary(customerId, tripStatus, today, today, null,
                onGetTripSummary);

    }

    private void getTripForThisMonth()
    {
        final RetrofitCallbacks<List<TripByCustomerId>> onGetTripSummary =
                new RetrofitCallbacks<List<TripByCustomerId>>()
                {
                    @Override
                    public void onResponse(Call<List<TripByCustomerId>> call, Response<List<TripByCustomerId>> response)
                    {
                        super.onResponse(call, response);
                        if (response.isSuccessful())
                        {
                            if (response.body() != null)
                            {
                                for (TripByCustomerId tripsByDriverMail : response.body())
                                {
                                    trips.add(tripsByDriverMail);
                                }
                                updateRealmObjectsAndAddTripsToAdapter();
                            }
                        } else
                        {
                            flLoadingTrips.setVisibility(View.GONE);
                            flSomethingWentWrong.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<TripByCustomerId>> call, Throwable t)
                    {
                        super.onFailure(call, t);
                        flLoadingTrips.setVisibility(View.GONE);
                        flSomethingWentWrong.setVisibility(View.VISIBLE);
                    }
                };

        final String today = Utils.getDate(Calendar.getInstance().getTime());
        final Date month = Utils.addDays(Calendar.getInstance().getTime(), -30);
        final String monthInString = Utils.getDate(month);

        api.getTripSummary(customerId, tripStatus, monthInString, today, null,
                onGetTripSummary);
    }

    private void getTotalTrips()
    {
        final RetrofitCallbacks<List<TripByCustomerId>> onGetTripSummary =
                new RetrofitCallbacks<List<TripByCustomerId>>()
                {

                    @Override
                    public void onResponse(Call<List<TripByCustomerId>> call, Response<List<TripByCustomerId>> response)
                    {
                        super.onResponse(call, response);
                        if (response.isSuccessful())
                        {
                            if (response.body() != null)
                            {
                                for (TripByCustomerId tripsByDriverMail : response.body())
                                {
                                    trips.add(tripsByDriverMail);
                                }
                                updateRealmObjectsAndAddTripsToAdapter();
                            }
                        } else
                        {
                            flLoadingTrips.setVisibility(View.GONE);
                            flSomethingWentWrong.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<TripByCustomerId>> call, Throwable t)
                    {
                        super.onFailure(call, t);
                        flLoadingTrips.setVisibility(View.GONE);
                        flSomethingWentWrong.setVisibility(View.VISIBLE);
                    }
                };

        api.getTripSummary(customerId, tripStatus, null, null, null,
                onGetTripSummary);
    }

    private void updateRealmObjectsAndAddTripsToAdapter()
    {
        flLoadingTrips.setVisibility(View.GONE);
        if (trips.isEmpty())
        {
            flNoTripsFound.setVisibility(View.VISIBLE);
            return;
        }
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
    protected void internetNotAvailable()
    {
        if (sbNoInternet == null)
        {
            sbNoInternet = Snackbar.make(clActivityTrips, R.string.no_internet, Snackbar.LENGTH_INDEFINITE);
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

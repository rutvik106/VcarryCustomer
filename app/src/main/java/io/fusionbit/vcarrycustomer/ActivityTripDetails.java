package io.fusionbit.vcarrycustomer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import javax.inject.Inject;

import api.API;
import api.RetrofitCallbacks;
import apimodels.TripByCustomerId;
import butterknife.BindView;
import extra.LocaleHelper;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import models.BookedTrip;
import retrofit2.Call;
import retrofit2.Response;

public class ActivityTripDetails extends BaseActivity
{
    @BindView(R.id.tv_tripStatus)
    TextView tvTripStatus;
    @BindView(R.id.tv_tripNumber)
    TextView tvTripNumber;
    @BindView(R.id.tv_tripDetailTime)
    TextView tvTripDetailTime;
    @BindView(R.id.tv_driverName)
    TextView tvDriverName;
    @BindView(R.id.tv_tripDriverLicenceNo)
    TextView tvTripDriverLicenceNo;
    @BindView(R.id.tv_vehicleNo)
    TextView tvVehicleNo;
    @BindView(R.id.ll_tripStartedDetails)
    LinearLayout llTripStartedDetails;
    @BindView(R.id.tv_tripFromCompanyName)
    TextView tvTripFromCompanyName;
    @BindView(R.id.tv_tripLocation)
    TextView tvTripLocation;
    @BindView(R.id.tv_tripToCompanyName)
    TextView tvTripToCompanyName;
    @BindView(R.id.tv_tripDestination)
    TextView tvTripDestination;
    @BindView(R.id.tv_tripFare)
    TextView tvTripFare;

    String tripId;

    TripByCustomerId tripDetails;

    @Inject
    Realm realm;

    @Inject
    API api;

    public static void start(Context context, String tripId)
    {
        Intent i = new Intent(context, ActivityTripDetails.class);
        i.putExtra(Constants.INTENT_EXTRA_TRIP_ID, tripId);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        tripId = getIntent().getStringExtra(Constants.INTENT_EXTRA_TRIP_ID);
        ((App) getApplication()).getUser().inject(this);
        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setTitle("Trip Details");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        if (tripId != null)
        {
            tryToGetFromRealm();
            getTripDetails();
        }
    }

    private void getTripDetails()
    {
        api.getTripDetailsByTripId(tripId, new RetrofitCallbacks<TripByCustomerId>()
        {

            @Override
            public void onResponse(Call<TripByCustomerId> call, Response<TripByCustomerId> response)
            {
                super.onResponse(call, response);
                if (response.isSuccessful())
                {
                    if (response.body() != null)
                    {
                        tripDetails = response.body();

                        final BookedTrip bookedTrip = realm.where(BookedTrip.class)
                                .equalTo("tripId", tripDetails.getTripId()).findFirst();

                        realm.beginTransaction();

                        if (bookedTrip != null)
                        {
                            bookedTrip.setStatus(Integer.valueOf(tripDetails.getTripStatus()));
                            bookedTrip.setTripCost(tripDetails.getFare());
                            tripDetails.setBookedTrip(realm.copyFromRealm(realm
                                    .copyToRealmOrUpdate(bookedTrip)));
                        }

                        realm.copyToRealmOrUpdate(tripDetails);

                        realm.commitTransaction();

                        bindDataToUi();
                    }
                }
            }
        });
    }

    private void tryToGetFromRealm()
    {
        tripDetails = realm.where(TripByCustomerId.class).equalTo("tripId", tripId).findFirst();

        if (tripDetails != null)
        {
            bindDataToUi();
            tripDetails.addChangeListener(new RealmChangeListener<TripByCustomerId>()
            {
                @Override
                public void onChange(TripByCustomerId tripDetails)
                {
                    bindDataToUi();
                }
            });
        }
    }

    private void bindDataToUi()
    {
        tvTripStatus.setText(tripDetails.getStatus());

        if (tripDetails.getTripNo() != null)
        {
            tvTripNumber.setText(tripDetails.getTripNo());
        } else
        {
            tvTripNumber.setVisibility(View.GONE);
        }

        tvTripDetailTime.setText(tripDetails.getTripDatetimeDmy());

        final BookedTrip bookedTrip = tripDetails.getBookedTrip();
        if (bookedTrip != null)
        {
            if (bookedTrip.getDriverName() != null)
            {
                tvDriverName.setText(bookedTrip.getDriverName());
            } else
            {
                llTripStartedDetails.setVisibility(View.GONE);
            }
        } else
        {
            llTripStartedDetails.setVisibility(View.GONE);
        }

        if (LocaleHelper.getLanguage(this).equalsIgnoreCase("gu"))
        {
            tvTripLocation.setText(tripDetails.getFromGujaratiAddress());
            tvTripDestination.setText(tripDetails.getToGujaratiAddress());
        } else
        {
            tvTripLocation.setText(tripDetails.getFromShippingLocation());
            tvTripDestination.setText(tripDetails.getToShippingLocation());
        }

        //tvTripFromCompanyName.setText(tripDetails.getna);

        tvTripFare.setText(getString(R.string.rs) + " " + tripDetails.getFare());


    }

    @Override
    protected int getLayoutResourceId()
    {
        return R.layout.activity_trip_details;
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

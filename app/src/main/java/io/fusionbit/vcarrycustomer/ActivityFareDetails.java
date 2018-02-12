package io.fusionbit.vcarrycustomer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import api.API;
import api.RetrofitCallbacks;
import apimodels.TripBreakUpDetails;
import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Response;

public class ActivityFareDetails extends BaseActivity
{


    Snackbar sbNoInternet;
    @BindView(R.id.ll_chargesItemContainer)
    LinearLayout llChargesItemContainer;
    @BindView(R.id.ll_chargesItemContainerView)
    LinearLayout llChargesItemContainerView;
    @BindView(R.id.tv_totalCharges)
    TextView tvTotalCharges;
    @BindView(R.id.cl_activityFareDetails)
    CoordinatorLayout clActivityFareDetails;


    double totalCharges = 0;
    @BindView(R.id.pb_loadingFareDetails)
    ProgressBar pbLoadingFareDetails;
    @BindView(R.id.tv_cashRecieved)
    TextView tvCashRecieved;

    public static void start(Context context, String tripId, String cashReceived)
    {
        final Intent i = new Intent(context, ActivityFareDetails.class);
        i.putExtra(Constants.INTENT_EXTRA_TRIP_ID, tripId);
        i.putExtra(Constants.INTENT_EXTRA_CASH_RECEIVED, cashReceived);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        ((App) getApplication()).getUser().inject(this);

        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setTitle(R.string.trip_charges_details);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        final String tripId = getIntent().getStringExtra(Constants.INTENT_EXTRA_TRIP_ID);
        final String cashReceived = getIntent().getStringExtra(Constants.INTENT_EXTRA_CASH_RECEIVED);

        if (tripId != null)
        {
            if (cashReceived != null)
            {
                tvCashRecieved.setText(getString(R.string.rs) + " " + cashReceived);
            } else
            {
                tvCashRecieved.setText("NA");
            }
            API.getInstance().getTripBreakUpDetails(tripId, new RetrofitCallbacks<List<TripBreakUpDetails>>()
            {
                @Override
                public void onResponse(Call<List<TripBreakUpDetails>> call, Response<List<TripBreakUpDetails>> response)
                {
                    super.onResponse(call, response);
                    pbLoadingFareDetails.setVisibility(View.GONE);
                    if (response.isSuccessful())
                    {
                        if (response.body() == null)
                        {
                            Toast.makeText(ActivityFareDetails.this, R.string.fare_details_not_found,
                                    Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                        llChargesItemContainerView.setVisibility(View.VISIBLE);

                        for (int i = 0; i < response.body().size(); i++)
                        {
                            totalCharges = totalCharges + Double.valueOf(response.body().get(i)
                                    .getNetAmount());

                            View v = LayoutInflater.from(ActivityFareDetails.this)
                                    .inflate(R.layout.single_charge_item, llChargesItemContainer, false);

                            TextView tv1 = (TextView) v.findViewById(R.id.tv_chargeItemName);
                            tv1.setText(response.body().get(i).getItemName() + " : " +
                                    response.body().get(i).getItemDesc());

                            TextView tv2 = (TextView) v.findViewById(R.id.tv_chargeAmount);
                            tv2.setText(getResources().getString(R.string.rs) + " " +
                                    response.body().get(i).getAmount());

                            TextView tv3 = (TextView) v.findViewById(R.id.tv_chargeDiscount);
                            tv3.setText(response.body().get(i).getDiscount() + " %");

                            TextView tv4 = (TextView) v.findViewById(R.id.tv_chargeNetAmount);
                            tv4.setText(getResources().getString(R.string.rs) + " " +
                                    response.body().get(i).getNetAmount());

                            llChargesItemContainer.addView(v);

                        }

                        tvTotalCharges.setText(getResources().getString(R.string.rs) + " " + totalCharges);

                    }
                }

                @Override
                public void onFailure(Call<List<TripBreakUpDetails>> call, Throwable t)
                {
                    super.onFailure(call, t);
                    Toast.makeText(ActivityFareDetails.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                    pbLoadingFareDetails.setVisibility(View.GONE);
                }
            });
        } else
        {
            Toast.makeText(this, "Trip Id NULL", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    @Override
    protected int getLayoutResourceId()
    {
        return R.layout.activity_fare_details;
    }

    @Override
    protected void internetNotAvailable()
    {
        if (sbNoInternet == null)
        {
            sbNoInternet = Snackbar.make(clActivityFareDetails, R.string.no_internet, Snackbar.LENGTH_INDEFINITE);
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
            sbNoInternet = null;
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

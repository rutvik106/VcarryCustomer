package fragments;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import adapters.AccountBalanceAdapter;
import api.API;
import api.RetrofitCallbacks;
import apimodels.AccountSummary;
import apimodels.AccountSummaryNew;
import apimodels.TripByCustomerId;
import io.fusionbit.vcarrycustomer.App;
import io.fusionbit.vcarrycustomer.Constants;
import io.fusionbit.vcarrycustomer.R;
import io.fusionbit.vcarrycustomer.Utils;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by rutvik on 11/17/2016 at 11:17 PM.
 */

public class FragmentAccBalance extends Fragment implements SwipeRefreshLayout.OnRefreshListener
{

    final AccountSummary accountSummary = new AccountSummary();
    final String tripStatus = Constants.TRIP_STATUS_FINISHED + ","
            + Constants.TRIP_STATUS_CANCELLED_BY_DRIVER + ","
            + Constants.TRIP_STATUS_CANCELLED_BY_CUSTOMER + ","
            + Constants.TRIP_STATUS_LOADING + ","
            + Constants.TRIP_STATUS_DRIVER_ALLOCATED + ","
            + Constants.TRIP_STATUS_NEW + ","
            + Constants.TRIP_STATUS_TRIP_STARTED + ","
            + Constants.TRIP_STATUS_UNLOADING + ","
            + Constants.TRIP_STATUS_CANCELLED_BY_VCARRY;


    RecyclerView rvAccountBalance;
    AccountBalanceAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    String customerId;
    @Inject
    API api;
    boolean busyLoadingData = false;

    public static FragmentAccBalance newInstance(int index)
    {
        FragmentAccBalance fragmentAccBalance = new FragmentAccBalance();
        Bundle b = new Bundle();
        b.putInt("index", index);
        fragmentAccBalance.setArguments(b);
        return fragmentAccBalance;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_account_balance, container, false);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.srl_refreshAccountBalance);

        swipeRefreshLayout.setOnRefreshListener(this);

        rvAccountBalance = (RecyclerView) view.findViewById(R.id.rv_accountBalance);
        rvAccountBalance.setHasFixedSize(true);
        rvAccountBalance.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new AccountBalanceAdapter(getActivity());

        rvAccountBalance.setAdapter(adapter);

        adapter.addAccountSummaryCard(accountSummary);

        getAccountBalance();

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        ((App) getActivity().getApplication()).getUser().inject(this);
        super.onCreate(savedInstanceState);
    }

    public void getAccountBalance()
    {
        if (!busyLoadingData)
        {
            busyLoadingData = true;
            swipeRefreshLayout.setRefreshing(true);
            accountSummary.clearData();
            //adapter.notifyDataSetChanged();
            customerId = PreferenceManager.getDefaultSharedPreferences(getActivity())
                    .getString(Constants.CUSTOMER_ID, null);
            if (customerId != null)
            {
                //getAccountBalanceForToday();

                getAccountBalanceSummary(customerId);

            }
        }
    }

    private void getAccountBalanceSummary(String customerId)
    {
        api.getAccountSummary(customerId,
                new RetrofitCallbacks<AccountSummaryNew>()
                {

                    @Override
                    public void onResponse(Call<AccountSummaryNew> call, Response<AccountSummaryNew> response)
                    {
                        super.onResponse(call, response);
                        if (busyLoadingData)
                        {
                            if (swipeRefreshLayout.isRefreshing())
                            {
                                swipeRefreshLayout.setRefreshing(false);
                            }
                            busyLoadingData = false;
                        }
                        if (response.isSuccessful())
                        {
                            if (response.body() instanceof AccountSummaryNew)
                            {
                                accountSummary.setAccountSummaryNew(response.body());
                                adapter.notifyDataSetChanged();
                                getTripForToday();
                                getTripForThisMonth();
                                getTotalTrips();
                            }
                        } else
                        {
                            Toast.makeText(getContext(), R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<AccountSummaryNew> call, Throwable t)
                    {
                        super.onFailure(call, t);
                        Toast.makeText(getContext(), R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                        if (busyLoadingData)
                        {
                            if (swipeRefreshLayout.isRefreshing())
                            {
                                swipeRefreshLayout.setRefreshing(false);
                            }
                            busyLoadingData = false;
                        }
                    }
                });
    }

    private void getAccountBalanceForToday()
    {
        final RetrofitCallbacks<AccountSummary> onGetAccountSummary =
                new RetrofitCallbacks<AccountSummary>()
                {

                    @Override
                    public void onResponse(Call<AccountSummary> call, Response<AccountSummary> response)
                    {
                        super.onResponse(call, response);
                        if (response.isSuccessful())
                        {
                            if (response.body() != null)
                            {
                                accountSummary.setReceivedToday(response.body().getReceived());
                                accountSummary.setReceivableToday(response.body().getReceivable());
                                getAccountBalanceForThisMonth();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<AccountSummary> call, Throwable t)
                    {
                        super.onFailure(call, t);
                        adapter.notifyDataSetChanged();
                        if (busyLoadingData)
                        {
                            if (swipeRefreshLayout.isRefreshing())
                            {
                                swipeRefreshLayout.setRefreshing(false);
                            }
                            busyLoadingData = false;
                        }
                    }
                };

        final String today = Utils.getDate(Calendar.getInstance().getTime());

        api.getAccountSummary(customerId, today, today, onGetAccountSummary);

    }

    private void getAccountBalanceForThisMonth()
    {
        final RetrofitCallbacks<AccountSummary> onGetAccountSummary =
                new RetrofitCallbacks<AccountSummary>()
                {

                    @Override
                    public void onResponse(Call<AccountSummary> call, Response<AccountSummary> response)
                    {
                        super.onResponse(call, response);
                        if (response.isSuccessful())
                        {
                            if (response.body() != null)
                            {
                                accountSummary.setReceivedThisMonth(response.body().getReceived());
                                accountSummary.setReceivableThisMonth(response.body().getReceivable());
                                getTotalAccountBalance();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<AccountSummary> call, Throwable t)
                    {
                        super.onFailure(call, t);
                        adapter.notifyDataSetChanged();
                        if (busyLoadingData)
                        {
                            if (swipeRefreshLayout.isRefreshing())
                            {
                                swipeRefreshLayout.setRefreshing(false);
                            }
                            busyLoadingData = false;
                        }
                    }
                };

        final String today = Utils.getDate(Calendar.getInstance().getTime());
        final Date month = Utils.addDays(Calendar.getInstance().getTime(),
                -1 * (Calendar.getInstance().get(Calendar.DAY_OF_MONTH) - 1));
        final String monthInString = Utils.getDate(month);

        api.getAccountSummary(customerId, monthInString, today, onGetAccountSummary);
    }

    private void getTotalAccountBalance()
    {
        final RetrofitCallbacks<AccountSummary> onGetAccountSummary =
                new RetrofitCallbacks<AccountSummary>()
                {

                    @Override
                    public void onResponse(Call<AccountSummary> call, Response<AccountSummary> response)
                    {
                        super.onResponse(call, response);
                        if (response.isSuccessful())
                        {
                            if (response.body() != null)
                            {
                                accountSummary.setTotalReceived(response.body().getReceived());
                                accountSummary.setTotalReceivable(response.body().getReceivable());
                                accountSummary.setBusyLoading(false);
                                adapter.notifyDataSetChanged();
                            }
                        }
                        if (busyLoadingData)
                        {
                            if (swipeRefreshLayout.isRefreshing())
                            {
                                swipeRefreshLayout.setRefreshing(false);
                            }
                            busyLoadingData = false;
                        }
                    }

                    @Override
                    public void onFailure(Call<AccountSummary> call, Throwable t)
                    {
                        super.onFailure(call, t);
                        adapter.notifyDataSetChanged();
                        if (busyLoadingData)
                        {
                            if (swipeRefreshLayout.isRefreshing())
                            {
                                swipeRefreshLayout.setRefreshing(false);
                            }
                            busyLoadingData = false;
                        }
                    }
                };

        api.getAccountSummary(customerId, "", "", onGetAccountSummary);
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
                                    accountSummary.getTripToday().put(tripsByDriverMail.getTripId(),
                                            tripsByDriverMail);
                                }
                                adapter.notifyDataSetChanged();
                            }
                        }
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
                                    accountSummary.getTripThisMonth().put(tripsByDriverMail.getTripId(),
                                            tripsByDriverMail);
                                }
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                };

        final String today = Utils.getDate(Calendar.getInstance().getTime());
        final Date month = Utils.addDays(Calendar.getInstance().getTime(),
                -1 * (Calendar.getInstance().get(Calendar.DAY_OF_MONTH) - 1));
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
                                    accountSummary.getTotalTrips().put(tripsByDriverMail.getTripId(),
                                            tripsByDriverMail);
                                }
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                };

        api.getTripsByCustomerId(customerId,
                onGetTripSummary);

    }


    @Override
    public void onRefresh()
    {
        getAccountBalance();
    }


}

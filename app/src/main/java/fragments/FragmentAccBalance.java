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

import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import adapters.AccountBalanceAdapter;
import api.API;
import api.RetrofitCallbacks;
import apimodels.AccountSummary;
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
            + Constants.TRIP_STATUS_CANCELLED_BY_CUSTOMER;
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
            accountSummary.clearData();
            //adapter.notifyDataSetChanged();
            customerId = PreferenceManager.getDefaultSharedPreferences(getActivity())
                    .getString(Constants.CUSTOMER_ID, null);
            if (customerId != null)
            {
                getAccountBalanceForToday();

                /**getTripForToday();
                 getTripForThisMonth();
                 getTotalTrips();*/
            }
        }
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
        final Date month = Utils.addDays(Calendar.getInstance().getTime(), -30);
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


    /**
     * private void getTripForToday()
     * {
     * final RetrofitCallbacks<List<TripsByDriverMail>> onGetTripSummary =
     * new RetrofitCallbacks<List<TripsByDriverMail>>()
     * {
     *
     * @Override public void onResponse(Call<List<TripsByDriverMail>> call, Response<List<TripsByDriverMail>> response)
     * {
     * super.onResponse(call, response);
     * if (response.isSuccessful())
     * {
     * if (response.body() != null)
     * {
     * for (TripsByDriverMail tripsByDriverMail : response.body())
     * {
     * accountSummary.getTripToday().add(tripsByDriverMail);
     * }
     * adapter.notifyDataSetChanged();
     * }
     * }
     * }
     * };
     * <p>
     * final String today = Utils.getDate(Calendar.getInstance().getTime());
     * <p>
     * API.getInstance().getTripSummary(customerId, tripStatus, today, today, null,
     * onGetTripSummary);
     * <p>
     * }
     * <p>
     * private void getTripForThisMonth()
     * {
     * final RetrofitCallbacks<List<TripsByDriverMail>> onGetTripSummary =
     * new RetrofitCallbacks<List<TripsByDriverMail>>()
     * {
     * @Override public void onResponse(Call<List<TripsByDriverMail>> call, Response<List<TripsByDriverMail>> response)
     * {
     * super.onResponse(call, response);
     * if (response.isSuccessful())
     * {
     * if (response.body() != null)
     * {
     * for (TripsByDriverMail tripsByDriverMail : response.body())
     * {
     * accountSummary.getTripThisMonth().add(tripsByDriverMail);
     * }
     * adapter.notifyDataSetChanged();
     * }
     * }
     * }
     * };
     * <p>
     * final String today = Utils.getDate(Calendar.getInstance().getTime());
     * final Date month = Utils.addDays(Calendar.getInstance().getTime(), -30);
     * final String monthInString = Utils.getDate(month);
     * <p>
     * API.getInstance().getTripSummary(customerId, tripStatus, monthInString, today, null,
     * onGetTripSummary);
     * }
     * <p>
     * private void getTotalTrips()
     * {
     * final RetrofitCallbacks<List<TripsByDriverMail>> onGetTripSummary =
     * new RetrofitCallbacks<List<TripsByDriverMail>>()
     * {
     * @Override public void onResponse(Call<List<TripsByDriverMail>> call, Response<List<TripsByDriverMail>> response)
     * {
     * super.onResponse(call, response);
     * if (response.isSuccessful())
     * {
     * if (response.body() != null)
     * {
     * for (TripsByDriverMail tripsByDriverMail : response.body())
     * {
     * accountSummary.getTotalTrips().add(tripsByDriverMail);
     * }
     * adapter.notifyDataSetChanged();
     * }
     * }
     * }
     * };
     * <p>
     * API.getInstance().getTripSummary(customerId, tripStatus, null, null, null,
     * onGetTripSummary);
     * }
     */


    @Override
    public void onRefresh()
    {
        getAccountBalance();
    }


}

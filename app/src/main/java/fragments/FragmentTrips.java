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
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import adapters.TripDetailsAdapter;
import api.API;
import api.RetrofitCallbacks;
import apimodels.TripByCustomerId;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.fusionbit.vcarrycustomer.App;
import io.fusionbit.vcarrycustomer.Constants;
import io.fusionbit.vcarrycustomer.R;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by rutvik on 11/20/2016 at 11:16 AM.
 */

public class FragmentTrips extends Fragment implements SwipeRefreshLayout.OnRefreshListener
{

    @BindView(R.id.rv_userActivity)
    RecyclerView rvUserActivity;

    @BindView(R.id.ll_homeEmpty)
    LinearLayout llHomeEmpty;

    @BindView(R.id.srl_refreshTrips)
    SwipeRefreshLayout srlRefreshTrips;

    TripDetailsAdapter adapter;

    @Inject
    Realm realm;

    @Inject
    API api;
    private RealmResults<TripByCustomerId> bookedTripRealmResults;

    public static FragmentTrips newInstance(int index)
    {
        FragmentTrips fragmentTrips = new FragmentTrips();
        Bundle b = new Bundle();
        b.putInt("index", index);
        fragmentTrips.setArguments(b);
        return fragmentTrips;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_trips, container, false);

        ButterKnife.bind(this, view);

        ((App) getActivity().getApplication()).getUser().inject(this);

        adapter = new TripDetailsAdapter(getActivity());

        rvUserActivity.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvUserActivity.setHasFixedSize(true);
        rvUserActivity.setAdapter(adapter);

        srlRefreshTrips.setOnRefreshListener(this);

        getTripsFromRealm();
        getTrips();

        return view;
    }


    private void getTripsFromRealm()
    {
        adapter.clear();

        bookedTripRealmResults =
                realm.where(TripByCustomerId.class).findAll();

        bookedTripRealmResults.addChangeListener(new RealmChangeListener<RealmResults<TripByCustomerId>>()
        {
            @Override
            public void onChange(RealmResults<TripByCustomerId> element)
            {
                adapter.notifyDataSetChanged();
            }
        });

        for (TripByCustomerId bookedTrip : bookedTripRealmResults)
        {
            adapter.addBookedTrip(bookedTrip);
        }

        if (adapter.getItemCount() != 0)
        {
            llHomeEmpty.setVisibility(View.GONE);
        } else
        {
            llHomeEmpty.setVisibility(View.VISIBLE);
        }
    }


    private void getTrips()
    {
        final String customerId = PreferenceManager.getDefaultSharedPreferences(getActivity())
                .getString(Constants.CUSTOMER_ID, null);
        api.getTripsByCustomerId(customerId, new RetrofitCallbacks<List<TripByCustomerId>>()
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
                    getTripsFromRealm();
                } else
                {
                    Toast.makeText(getActivity(), R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                }
                if (srlRefreshTrips.isRefreshing())
                {
                    srlRefreshTrips.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<List<TripByCustomerId>> call, Throwable t)
            {
                super.onFailure(call, t);
                Toast.makeText(getActivity(), R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                if (srlRefreshTrips.isRefreshing())
                {
                    srlRefreshTrips.setRefreshing(false);
                }
            }
        });
    }

    @Override
    public void onRefresh()
    {
        getTrips();
    }
}
package io.fusionbit.vcarrycustomer;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.multidex.BuildConfig;
import android.support.v7.widget.AppCompatSpinner;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import adapters.CustomListAdapter;
import api.API;
import api.RetrofitCallbacks;
import apimodels.FromLocation;
import apimodels.SpinnerModel;
import apimodels.Vehicle;
import butterknife.BindView;
import butterknife.ButterKnife;
import components.DaggerFirebaseOperations;
import components.FirebaseOperations;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import module.FirebaseRemoteConfigSettingsModule;
import retrofit2.Call;
import retrofit2.Response;

public class ActivityBookTrip extends VCarryActivity implements Validator.ValidationListener
{

    @BindView(R.id.btn_requestTrip)
    Button btnRequestTrip;

    @BindView(R.id.iv_selectFrom)
    ImageView ivSelectFrom;

    @BindView(R.id.iv_selectTo)
    ImageView ivSelectTo;

    @BindView(R.id.spin_vehicle)
    AppCompatSpinner spinVehicle;

    @BindView(R.id.tv_tripFare)
    TextView tvTripFare;

    @NotEmpty
    @BindView(R.id.act_from)
    AutoCompleteTextView actFrom;

    @NotEmpty
    @BindView(R.id.act_to)
    AutoCompleteTextView actTo;

    @BindView(R.id.pb_loadingTripCost)
    ProgressBar pbLoadingTripCost;

    String fromPlace, fromLat, fromLng;

    String toPlace, toLat, toLng;

    FirebaseOperations firebaseOperations;

    RealmResults<Vehicle> realmVehicles;

    RealmResults<FromLocation> realmFromLocations;
    Realm realm;
    @Inject
    API api;
    Validator validator;
    Call<Integer> getFare;
    private String fromShippingLocationId = null;
    private String toShippingLocationId = null;
    private int vehicleTypeId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_trip);

        ButterKnife.bind(this);

        validator = new Validator(this);
        validator.setValidationListener(this);

        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getResources().getString(R.string.book_a_trip));
        }

        api = ((App) getApplication()).getVcarryApi().api();

        final FirebaseRemoteConfigSettings configSettings =
                new FirebaseRemoteConfigSettings.Builder()
                        .setDeveloperModeEnabled(BuildConfig.DEBUG)
                        .build();

        firebaseOperations = DaggerFirebaseOperations.builder()
                .firebaseRemoteConfigSettingsModule(new FirebaseRemoteConfigSettingsModule(configSettings))
                .build();

        spinVehicle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                SpinnerModel model = (SpinnerModel) adapterView.getAdapter().getItem(i);
                vehicleTypeId = model.getId();
                getFair();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });


        ivSelectFrom.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                Intent i = new Intent(ActivityBookTrip.this, ActivityPickLocation.class);
                i.putExtra("ACTIVITY_INTENT", Constants.SELECT_START_LOCATION_ACTIVITY);

                startActivityForResult(i, Constants.SELECT_START_LOCATION_ACTIVITY);

            }
        });

        ivSelectTo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(ActivityBookTrip.this, ActivityPickLocation.class);
                i.putExtra("ACTIVITY_INTENT", Constants.SELECT_DESTINATION_LOCATION_ACTIVITY);

                startActivityForResult(i, Constants.SELECT_DESTINATION_LOCATION_ACTIVITY);
            }
        });

        btnRequestTrip.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                validator.validate();

                /*if (!actFrom.getText().toString().isEmpty() && !actTo.getText().toString().isEmpty())
                {
                    btnRequestTrip.setEnabled(false);
                    firebaseOperations.tripOperations().bookTrip(actFrom.getText().toString(), actTo.getText().toString(), new TripOperations.TripOperationListener()
                    {
                        @Override
                        public void tripBookedSuccessfully()
                        {
                            Toast.makeText(ActivityBookTrip.this, "Trip Booked Successfully", Toast.LENGTH_SHORT).show();
                            btnRequestTrip.setEnabled(true);
                        }

                        @Override
                        public void failedToBookTrip(DatabaseError databaseError)
                        {
                            btnRequestTrip.setEnabled(true);
                        }
                    });
                } else
                {
                    Toast.makeText(ActivityBookTrip.this, "Please enter from and to", Toast.LENGTH_SHORT).show();
                }*/
            }
        });

        actFrom.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                actFrom.showDropDown();
            }
        });

        actTo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                actTo.showDropDown();
            }
        });

        actFrom.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View view, boolean inFocus)
            {
                if (inFocus)
                {
                    actFrom.showDropDown();
                }
            }
        });

        actTo.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View view, boolean inFocus)
            {
                if (inFocus)
                {
                    actTo.showDropDown();
                }
            }
        });

        final String customerId = PreferenceManager.getDefaultSharedPreferences(this)
                .getString(Constants.CUSTOMER_ID, null);

        realm = Realm.getDefaultInstance();

        getShippingLocationsFromRealm();
        setShippingLocationListAdapter();
        getShippingLocations(customerId);

        getVehiclesFromRealm();
        setVehicleListAdapter();
        getVehiclesFromApi();

    }

    private void getFair()
    {
        pbLoadingTripCost.setVisibility(View.VISIBLE);

        if (getFare != null)
        {
            getFare.cancel();
        }

        final RetrofitCallbacks<Integer> onGetFairCallback =
                new RetrofitCallbacks<Integer>()
                {

                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response)
                    {
                        super.onResponse(call, response);
                        if (response.isSuccessful())
                        {
                            if (response.body() > 0)
                            {
                                tvTripFare.setText(getResources().getString(R.string.rs) + " " + response.body());
                            } else
                            {
                                tvTripFare.setText("N/A");
                            }
                        } else
                        {
                            tvTripFare.setText("N/A");
                        }
                        pbLoadingTripCost.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<Integer> call, Throwable t)
                    {
                        super.onFailure(call, t);
                        tvTripFare.setText("N/A");
                        pbLoadingTripCost.setVisibility(View.GONE);
                    }
                };

        getFare = api.getFareForVehicleTypeLocations(fromShippingLocationId + "",
                toShippingLocationId + "", vehicleTypeId + "", onGetFairCallback);

    }

    private void getShippingLocationsFromRealm()
    {

        realmFromLocations = realm.where(FromLocation.class).findAll();

        realmFromLocations.addChangeListener(new RealmChangeListener<RealmResults<FromLocation>>()
        {
            @Override
            public void onChange(RealmResults<FromLocation> element)
            {
                setShippingLocationListAdapter();
            }
        });

    }

    private void setShippingLocationListAdapter()
    {
        final List<FromLocation> shippingLocationList = new ArrayList<>();
        shippingLocationList.addAll(realm.copyFromRealm(realmFromLocations));

        CustomListAdapter<FromLocation> fromAdaoter = new CustomListAdapter<FromLocation>(this,
                android.R.layout.simple_list_item_1, shippingLocationList);

        CustomListAdapter<FromLocation> toAdapter = new CustomListAdapter<FromLocation>(this,
                android.R.layout.simple_list_item_1, shippingLocationList);

        actFrom.setAdapter(fromAdaoter);
        actTo.setAdapter(toAdapter);

        final AdapterView.OnItemClickListener actFromListener = new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                final SpinnerModel model = (SpinnerModel) adapterView.getAdapter().getItem(i);
                actFrom.setText(model.getLabel());
                fromShippingLocationId = model.getId() + "";
                getFair();
                fromPlace = null;
                Utils.hideSoftKeyboard(ActivityBookTrip.this);
            }
        };

        final AdapterView.OnItemClickListener actToListener = new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                final SpinnerModel model = (SpinnerModel) adapterView.getAdapter().getItem(i);
                actTo.setText(model.getLabel());
                toShippingLocationId = model.getId() + "";
                getFair();
                toPlace = null;
                Utils.hideSoftKeyboard(ActivityBookTrip.this);
            }
        };

        actFrom.setOnItemClickListener(actFromListener);
        actTo.setOnItemClickListener(actToListener);
    }

    private void getShippingLocations(String customerId)
    {

        final RetrofitCallbacks<List<FromLocation>> onGetShippingLocationCallback =
                new RetrofitCallbacks<List<FromLocation>>()
                {

                    @Override
                    public void onResponse(Call<List<FromLocation>> call, Response<List<FromLocation>> response)
                    {
                        super.onResponse(call, response);
                        if (response.isSuccessful())
                        {
                            realm.beginTransaction();
                            for (FromLocation location : response.body())
                            {
                                realm.copyToRealmOrUpdate(location);
                            }
                            realm.commitTransaction();
                        }
                    }
                };

        api.getShippingLocationsForCustomer(customerId, onGetShippingLocationCallback);

    }

    private void getVehiclesFromApi()
    {

        final RetrofitCallbacks<List<Vehicle>> onGetVehiclesCallback =
                new RetrofitCallbacks<List<Vehicle>>()
                {

                    @Override
                    public void onResponse(Call<List<Vehicle>> call, Response<List<Vehicle>> response)
                    {
                        super.onResponse(call, response);
                        if (response.isSuccessful())
                        {
                            realm.beginTransaction();
                            for (Vehicle vehicle : response.body())
                            {
                                realm.copyToRealmOrUpdate(vehicle);
                            }
                            realm.commitTransaction();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Vehicle>> call, Throwable t)
                    {
                        super.onFailure(call, t);
                        Toast.makeText(ActivityBookTrip.this, "cannot get vehicle list", Toast.LENGTH_SHORT).show();
                    }
                };

        api.getVehicleTypes(onGetVehiclesCallback);

    }

    private void getVehiclesFromRealm()
    {
        realmVehicles = realm.where(Vehicle.class).findAll();

        realmVehicles.addChangeListener(new RealmChangeListener<RealmResults<Vehicle>>()
        {
            @Override
            public void onChange(RealmResults<Vehicle> element)
            {
                setVehicleListAdapter();
            }
        });
    }

    private void setVehicleListAdapter()
    {
        final List<Vehicle> vehicleList = new ArrayList<>();
        vehicleList.addAll(realmVehicles);

        spinVehicle
                .setAdapter(new CustomListAdapter<>(this,
                        android.R.layout.simple_list_item_1, vehicleList));

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == Activity.RESULT_OK)
        {

            switch (requestCode)
            {
                case Constants.SELECT_START_LOCATION_ACTIVITY:

                    fromPlace = data.getExtras().getString("PLACE");
                    fromLat = data.getExtras().getString("LAT");
                    fromLng = data.getExtras().getString("LNG");
                    fromShippingLocationId = null;
                    actFrom.setText(fromPlace);

                    break;

                case Constants.SELECT_DESTINATION_LOCATION_ACTIVITY:

                    toPlace = data.getExtras().getString("PLACE");
                    toLat = data.getExtras().getString("LAT");
                    toLng = data.getExtras().getString("LNG");
                    toShippingLocationId = null;
                    actTo.setText(toPlace);

                    break;
            }

        }
    }

    @Override
    public void onValidationSucceeded()
    {
        tryInsertingNewTrip();
    }

    private void tryInsertingNewTrip()
    {
        btnRequestTrip.setVisibility(View.GONE);
        final String customerId = PreferenceManager.getDefaultSharedPreferences(ActivityBookTrip.this)
                .getString(Constants.CUSTOMER_ID, null);

        final RetrofitCallbacks<Integer> onInsertCustomerTrip =
                new RetrofitCallbacks<Integer>()
                {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response)
                    {
                        super.onResponse(call, response);
                        if (response.isSuccessful())
                        {
                            if (response.body() > 0)
                            {
                                Utils.showSimpleAlertBox(ActivityBookTrip.this,
                                        "Your trip request was submitted successfully, Please " +
                                                "wait for confirmation from V-Carry!",
                                        new DialogInterface.OnClickListener()
                                        {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i)
                                            {
                                                finish();
                                            }
                                        });
                            } else
                            {
                                Toast.makeText(ActivityBookTrip.this, "Something went wrong, Please try again later", Toast.LENGTH_SHORT).show();
                            }
                        }
                        btnRequestTrip.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<Integer> call, Throwable t)
                    {
                        super.onFailure(call, t);
                        if (!call.isCanceled())
                        {
                            Toast.makeText(ActivityBookTrip.this, "Please check internet connection", Toast.LENGTH_SHORT).show();
                        }
                        btnRequestTrip.setVisibility(View.GONE);
                    }
                };

        if (customerId != null)
        {
            final String fromLatLng = fromLat + "," + fromLng;
            final String toLatLng = toLat + "," + toLng;

            api.insertCustomerTrip(fromShippingLocationId + "", toShippingLocationId + "",
                    vehicleTypeId + "", customerId, fromPlace
                    , toPlace, fromLatLng, toLatLng, onInsertCustomerTrip);
        }

    }

    @Override
    public void onValidationFailed(List<ValidationError> errors)
    {
        for (ValidationError error : errors)
        {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;)
            if (view instanceof AutoCompleteTextView)
            {
                ((AutoCompleteTextView) view).setError(message);
            } else
            {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
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

package io.fusionbit.vcarrycustomer;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSpinner;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import extra.LocaleHelper;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import models.BookedTrip;
import models.UserActivities;
import module.FirebaseRemoteConfigSettingsModule;
import retrofit2.Call;
import retrofit2.Response;

public class ActivityBookTrip extends VCarryActivity implements Validator.ValidationListener
{

    final Handler mHandler = new Handler();
    final List<FromLocation> shippingLocationList = new ArrayList<>();
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
    CustomAutoCompleteTextView actFrom;
    @NotEmpty
    @BindView(R.id.act_to)
    CustomAutoCompleteTextView actTo;
    @BindView(R.id.pb_loadingTripCost)
    ProgressBar pbLoadingTripCost;
    @BindView(R.id.rg_tripType)
    RadioGroup rgTripType;
    @BindView(R.id.rb_oneWay)
    RadioButton rbOneWay;
    @BindView(R.id.rb_return)
    RadioButton rbReturn;
    String fromPlace, fromLat, fromLng;
    String toPlace, toLat, toLng;
    FirebaseOperations firebaseOperations;
    RealmResults<Vehicle> realmVehicles;
    RealmResults<FromLocation> realmFromLocations;
    Validator validator;
    Call<Integer> getFare;
    @Inject
    API api;
    @Inject
    Realm realm;
    @Inject
    UserActivities userActivities;
    @BindView(R.id.tv_tripSchedule)
    TextView tvTripSchedule;
    @BindView(R.id.ll_tripScheduleDetails)
    LinearLayout llTripScheduleDetails;
    int selectedFromLocation, selectedToLocation;
    private String fromShippingLocationId = null;
    private String toShippingLocationId = null;
    private int vehicleTypeId = 0;
    private String vehicleName = "N/A";
    private String tripFare = "N/A";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_trip);

        ButterKnife.bind(this);

        ((App) getApplication()).getUser().inject(this);

        validator = new Validator(this);
        validator.setValidationListener(this);

        final boolean isSchedulingTrip = getIntent().getBooleanExtra(Constants.IS_SCHEDULING_TRIP, false);

        if (isSchedulingTrip)
        {
            getScheduleDetails(getIntent().getBundleExtra(Constants.SCHEDULE_DETAILS));
        }

        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (isSchedulingTrip)
            {
                getSupportActionBar().setTitle(getResources().getString(R.string.schedule_trip));
            } else
            {
                getSupportActionBar().setTitle(getResources().getString(R.string.book_a_trip));
            }
        }

        rgTripType.check(rbOneWay.getId());

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
                vehicleName = model.getLabel();
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
            }
        });

        setListenersForAutoCompleteTextView();

        final String customerId = PreferenceManager.getDefaultSharedPreferences(this)
                .getString(Constants.CUSTOMER_ID, null);

        getShippingLocationsFromRealm();
        setShippingLocationListAdapter();
        getShippingLocations(customerId);

        getVehiclesFromRealm();
        setVehicleListAdapter();
        getVehiclesFromApi();

    }

    private void setListenersForAutoCompleteTextView()
    {
        actTo.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View view, boolean inFocus)
            {
                Utils.hideSoftKeyboard(ActivityBookTrip.this);
            }
        });

        actFrom.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View view, boolean inFocus)
            {
                Utils.hideSoftKeyboard(ActivityBookTrip.this);
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

        actTo.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                if (charSequence.length() == 0)
                {
                    mHandler.post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            actTo.showDropDown();
                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable editable)
            {
            }
        });

        actFrom.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                if (charSequence.length() == 0)
                {
                    Toast.makeText(ActivityBookTrip.this, "Empty", Toast.LENGTH_SHORT).show();
                    mHandler.post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            actFrom.showDropDown();
                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable editable)
            {
            }
        });
    }

    private void getScheduleDetails(Bundle scheduleDetails)
    {
        llTripScheduleDetails.setVisibility(View.VISIBLE);
        final int day = scheduleDetails.getInt(Constants.DAY);
        final int month = scheduleDetails.getInt(Constants.MONTH);
        final int year = scheduleDetails.getInt(Constants.YEAR);
        final int hour = scheduleDetails.getInt(Constants.HOUR);
        final int minute = scheduleDetails.getInt(Constants.MINUTE);

        String sMinute;

        if (minute == 0)
        {
            sMinute = "00";
        } else if (minute < 10)
        {
            sMinute = "0" + minute;
        } else
        {
            sMinute = minute + "";
        }

        final boolean isPm = scheduleDetails.getBoolean(Constants.IS_PM);

        if (isPm)
        {
            tvTripSchedule.setText(day + "/" + (month + 1) + "/" + year +
                    " " + ((hour % 12) == 0 ? 12 : (hour % 12)) + ":" + sMinute + " PM");
        } else
        {
            tvTripSchedule.setText(day + "/" + (month + 1) + "/" + year +
                    " " + ((hour % 12) == 0 ? 12 : (hour % 12)) + ":" + sMinute + " AM");
        }

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
                                tripFare = response.body() + "";
                                tvTripFare.setText(getResources().getString(R.string.rs) + " " + response.body());
                            } else
                            {
                                tripFare = "N/A";
                                tvTripFare.setText("N/A");
                            }
                        } else
                        {
                            tripFare = "N/A";
                            tvTripFare.setText("N/A");
                        }
                        pbLoadingTripCost.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<Integer> call, Throwable t)
                    {
                        super.onFailure(call, t);
                        tripFare = "N/A";
                        tvTripFare.setText("N/A");
                        pbLoadingTripCost.setVisibility(View.GONE);
                    }
                };

        final String customerId = PreferenceManager.getDefaultSharedPreferences(this)
                .getString(Constants.CUSTOMER_ID, null);

        getFare = api.getFareForVehicleTypeLocations(fromShippingLocationId + "",
                toShippingLocationId + "", vehicleTypeId + "", customerId, onGetFairCallback);

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
        shippingLocationList.addAll(realm.copyFromRealm(realmFromLocations));

        if (LocaleHelper.getLanguage(this).equalsIgnoreCase("gu"))
        {
            for (FromLocation location : shippingLocationList)
            {
                location.setReturnGujaratiAddress(true);
            }
        }

        CustomListAdapter<FromLocation> fromAdapter = new CustomListAdapter<FromLocation>(this,
                android.R.layout.simple_list_item_1, shippingLocationList)
        {

            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
                if (convertView == null)
                {
                    convertView = LayoutInflater.from(ActivityBookTrip.this)
                            .inflate(R.layout.single_address_with_company_name, parent, false);
                }

                TextView companyName = (TextView) convertView.findViewById(R.id.tv_spinnerItemCompanyName);
                companyName.setText(getString(R.string.company_name) + " " +
                        shippingLocationList.get(position).getShippingName());
                companyName.setTextColor(Color.DKGRAY);
                companyName.setTextSize(13f);

                TextView label = (TextView) convertView.findViewById(R.id.tv_spinnerItem);
                label.setText(shippingLocationList.get(position).getLabel());
                label.setTextColor(Color.BLACK);
                label.setTextSize(16f);


                // And finally return your dynamic (or custom) view for each spinner item
                return convertView;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent)
            {
                // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
                if (convertView == null)
                {
                    convertView = LayoutInflater.from(ActivityBookTrip.this)
                            .inflate(R.layout.single_address_with_company_name, parent, false);
                }

                TextView companyName = (TextView) convertView.findViewById(R.id.tv_spinnerItemCompanyName);
                companyName.setText(getString(R.string.company_name) + " " +
                        shippingLocationList.get(position).getShippingName());
                companyName.setTextColor(Color.DKGRAY);
                companyName.setTextSize(13f);

                TextView label = (TextView) convertView.findViewById(R.id.tv_spinnerItem);
                label.setText(shippingLocationList.get(position).getLabel());
                label.setTextColor(Color.BLACK);
                label.setTextSize(16f);


                // And finally return your dynamic (or custom) view for each spinner item
                return convertView;
            }
        };

        CustomListAdapter<FromLocation> toAdapter = new CustomListAdapter<FromLocation>(this,
                android.R.layout.simple_list_item_1, shippingLocationList)
        {

            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
                if (convertView == null)
                {
                    convertView = LayoutInflater.from(ActivityBookTrip.this)
                            .inflate(R.layout.single_address_with_company_name, parent, false);
                }

                TextView companyName = (TextView) convertView.findViewById(R.id.tv_spinnerItemCompanyName);
                companyName.setText(getString(R.string.company_name) + " " +
                        shippingLocationList.get(position).getShippingName());
                companyName.setTextColor(Color.DKGRAY);
                companyName.setTextSize(13f);

                TextView label = (TextView) convertView.findViewById(R.id.tv_spinnerItem);
                label.setText(shippingLocationList.get(position).getLabel());
                label.setTextColor(Color.BLACK);
                label.setTextSize(16f);


                // And finally return your dynamic (or custom) view for each spinner item
                return convertView;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent)
            {
                // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
                if (convertView == null)
                {
                    convertView = LayoutInflater.from(ActivityBookTrip.this)
                            .inflate(R.layout.single_address_with_company_name, parent, false);
                }

                TextView companyName = (TextView) convertView.findViewById(R.id.tv_spinnerItemCompanyName);
                companyName.setText(getString(R.string.company_name) + " " +
                        shippingLocationList.get(position).getShippingName());
                companyName.setTextColor(Color.DKGRAY);
                companyName.setTextSize(13f);

                TextView label = (TextView) convertView.findViewById(R.id.tv_spinnerItem);
                label.setText(shippingLocationList.get(position).getLabel());
                label.setTextColor(Color.BLACK);
                label.setTextSize(16f);


                // And finally return your dynamic (or custom) view for each spinner item
                return convertView;
            }

        };

        actFrom.setAdapter(fromAdapter);
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
                selectedFromLocation = i;
                fromPlace = null;
                Utils.hideSoftKeyboard(ActivityBookTrip.this);
                actTo.requestFocus();
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
                selectedToLocation = i;
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
        promptUserForBookingTrip();
    }

    private void promptUserForBookingTrip()
    {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.book_trip))
                .setMessage(R.string.book_trip_prompt_msg)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton(getString(R.string.book_trip), new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        tryInsertingNewTrip();
                    }
                }).setNegativeButton("CANCEL", null)
                .show();
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
                                realm.beginTransaction();
                                final BookedTrip bt = new BookedTrip(response.body().toString(),
                                        shippingLocationList.get(selectedFromLocation)
                                                .getCompanyName(), shippingLocationList.get(selectedToLocation)
                                        .getCompanyName(),
                                        tripFare, vehicleName);
                                bt.setCountDownTime(System.currentTimeMillis() + (1000 * 60 * 30));
                                realm.copyToRealmOrUpdate(bt);
                                realm.commitTransaction();

                                Utils.showSimpleAlertBox(ActivityBookTrip.this,
                                        getString(R.string.booking_request_success_message),
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
                                Toast.makeText(ActivityBookTrip.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(ActivityBookTrip.this, R.string.check_internet, Toast.LENGTH_SHORT).show();
                        }
                        btnRequestTrip.setVisibility(View.GONE);
                    }
                };

        if (customerId != null)
        {
            final String fromLatLng = fromLat + "," + fromLng;
            final String toLatLng = toLat + "," + toLng;

            if (fromPlace == null)
            {
                fromPlace = actFrom.getText().toString();
            }
            if (toPlace == null)
            {
                toPlace = actTo.getText().toString();
            }

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

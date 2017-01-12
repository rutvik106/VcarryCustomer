package io.fusionbit.vcarrycustomer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.multidex.BuildConfig;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.ArrayList;
import java.util.List;

import adapters.CustomListAdapter;
import api.API;
import api.RetrofitCallbacks;
import apimodels.Vehicle;
import butterknife.BindView;
import butterknife.ButterKnife;
import components.DaggerFirebaseOperations;
import components.FirebaseOperations;
import firebase.TripOperations;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import module.FirebaseRemoteConfigSettingsModule;
import retrofit2.Call;
import retrofit2.Response;

public class ActivityBookTrip extends AppCompatActivity
{

    @BindView(R.id.btn_requestTrip)
    Button btnRequestTrip;

    @BindView(R.id.iv_selectFrom)
    ImageView ivSelectFrom;

    @BindView(R.id.iv_selectTo)
    ImageView ivSelectTo;

    @BindView(R.id.spin_vehicle)
    AppCompatSpinner spinVehicle;

    @BindView(R.id.act_from)
    AutoCompleteTextView actFrom;

    @BindView(R.id.act_to)
    AutoCompleteTextView actTo;

    String fromPlace = "", fromLat, fromLng;

    String toPlace = "", toLat, toLng;

    FirebaseOperations firebaseOperations;

    RealmResults<Vehicle> realmVehicles;

    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_trip);

        ButterKnife.bind(this);

        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setTitle(getResources().getString(R.string.book_a_trip));
        }

        final FirebaseRemoteConfigSettings configSettings =
                new FirebaseRemoteConfigSettings.Builder()
                        .setDeveloperModeEnabled(BuildConfig.DEBUG)
                        .build();

        firebaseOperations = DaggerFirebaseOperations.builder()
                .firebaseRemoteConfigSettingsModule(new FirebaseRemoteConfigSettingsModule(configSettings))
                .build();

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
                if (!actFrom.getText().toString().isEmpty() && !actTo.getText().toString().isEmpty())
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
                }
            }
        });

        getVehiclesFromRealm();

        setVehicleListAdapter();

        getVehiclesFromApi();

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

        API.getInstance().getVehicleTypes(onGetVehiclesCallback);

    }

    private void getVehiclesFromRealm()
    {

        realm = Realm.getDefaultInstance();

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

                    actFrom.setText(fromPlace);

                    break;

                case Constants.SELECT_DESTINATION_LOCATION_ACTIVITY:

                    toPlace = data.getExtras().getString("PLACE");
                    toLat = data.getExtras().getString("LAT");
                    toLng = data.getExtras().getString("LNG");

                    actTo.setText(toPlace);

                    break;
            }

        }
    }

}

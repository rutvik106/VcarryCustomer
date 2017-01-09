package io.fusionbit.vcarrycustomer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.multidex.BuildConfig;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import butterknife.BindView;
import butterknife.ButterKnife;
import components.DaggerFirebaseOperations;
import components.FirebaseOperations;
import firebase.TripOperations;
import module.FirebaseRemoteConfigSettingsModule;

public class ActivityBookTrip extends AppCompatActivity
{

    @BindView(R.id.btn_requestTrip)
    Button btnRequestTrip;

    @BindView(R.id.iv_selectFrom)
    ImageView ivSelectFrom;

    @BindView(R.id.iv_selectTo)
    ImageView ivSelectTo;

    @BindView(R.id.act_from)
    AutoCompleteTextView actFrom;

    @BindView(R.id.act_to)
    AutoCompleteTextView actTo;

    String fromPlace = "", fromLat, fromLng;

    String toPlace = "", toLat, toLng;

    FirebaseOperations firebaseOperations;

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

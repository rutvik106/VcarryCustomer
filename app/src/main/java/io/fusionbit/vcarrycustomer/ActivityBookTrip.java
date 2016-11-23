package io.fusionbit.vcarrycustomer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;

import firebase.TripOperations;

public class ActivityBookTrip extends AppCompatActivity
{

    Button btnRequestTrip;

    ImageView ivSelectFrom, ivSelectTo;

    AutoCompleteTextView actFrom, actTo;

    String fromPlace, fromLat, fromLng;

    String toPlace, toLat, toLng;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_trip);

        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setTitle(getResources().getString(R.string.book_a_trip));
        }

        actFrom = (AutoCompleteTextView) findViewById(R.id.act_from);
        actTo = (AutoCompleteTextView) findViewById(R.id.act_to);

        btnRequestTrip = (Button) findViewById(R.id.btn_requestTrip);

        ivSelectFrom = (ImageView) findViewById(R.id.iv_selectFrom);
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

        ivSelectTo = (ImageView) findViewById(R.id.iv_selectTo);
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
                TripOperations.bookTrip(new TripOperations.TripOperationListener()
                {
                    @Override
                    public void tripBookedSuccessfully()
                    {
                        Toast.makeText(ActivityBookTrip.this, "Trip Booked Successfully", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void failedToBookTrip(DatabaseError databaseError)
                    {

                    }
                });
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

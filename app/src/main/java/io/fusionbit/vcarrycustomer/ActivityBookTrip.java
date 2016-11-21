package io.fusionbit.vcarrycustomer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;

import firebase.TripOperations;

public class ActivityBookTrip extends AppCompatActivity
{

    Button btnRequestTrip;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_trip);

        btnRequestTrip = (Button) findViewById(R.id.btn_requestTrip);

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
}

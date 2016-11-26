package io.fusionbit.vcarrycustomer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import fragments.FragmentMap;

public class ActivityPickLocation extends VCarryActivity
{

    FragmentMap map;

    FrameLayout flSelectLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_location);

        if (getSupportActionBar() != null)
        {
            if (getIntent().getIntExtra("ACTIVITY_INTENT", 0) == Constants.SELECT_START_LOCATION_ACTIVITY)
            {
                getSupportActionBar().setTitle(getResources().getString(R.string.select_start_location));
            } else if (getIntent().getIntExtra("ACTIVITY_INTENT", 0) == (Constants.SELECT_DESTINATION_LOCATION_ACTIVITY))
            {
                getSupportActionBar().setTitle(getResources().getString(R.string.select_destination));
            }
        }

        map = FragmentMap.newInstance(0, this);

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .add(R.id.frag_map, map)
                .commit();

        flSelectLocation = (FrameLayout) findViewById(R.id.fl_selectLocation);

        flSelectLocation.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(!map.getCurrentPlace().equals("Fetching location..."))
                {
                    setResultIntent();
                }
                else {
                    Toast.makeText(ActivityPickLocation.this, "Fetching location...", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void setResultIntent()
    {
        Intent i = new Intent();
        i.putExtra("PLACE", map.getCurrentPlace());
        i.putExtra("LAT", map.getCurrentLat());
        i.putExtra("LNG", map.getCurrentLng());
        setResult(Activity.RESULT_OK, i);
        finish();
    }

}

package io.fusionbit.vcarrycustomer;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fragments.FragmentMap;

public class ActivityPickLocation extends BaseActivity
{

    FragmentMap map;

    FrameLayout flSelectLocation;

    @BindView(R.id.et_searchLocation)
    EditText etSearchLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);

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
                if (!map.getCurrentPlace().equals(getString(R.string.fetching_location)))
                {
                    setResultIntent();
                } else
                {
                    Toast.makeText(ActivityPickLocation.this, getString(R.string.fetching_location),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        etSearchLocation.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if (actionId == EditorInfo.IME_ACTION_SEARCH)
                {
                    onMapSearch(etSearchLocation.getText().toString());
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    protected int getLayoutResourceId()
    {
        return R.layout.activity_pick_location;
    }

    @Override
    protected void internetNotAvailable()
    {

    }

    @Override
    protected void internetAvailable()
    {

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

    public void onMapSearch(String location)
    {
        List<Address> addressList = null;

        if (location != null)
        {
            Geocoder geocoder = new Geocoder(this);
            try
            {
                addressList = geocoder.getFromLocationName(location, 1, 22.832233, 72.350482,
                        23.329658, 72.969541);

            } catch (IOException e)
            {
                e.printStackTrace();
            }
            if (addressList != null)
            {
                Address address = addressList.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                map.mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        }
    }

}

package fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import io.fusionbit.vcarrycustomer.App;
import io.fusionbit.vcarrycustomer.Constants;
import io.fusionbit.vcarrycustomer.GeocodeAddressIntentService;
import io.fusionbit.vcarrycustomer.MapWrapperLayout;
import io.fusionbit.vcarrycustomer.R;

import static java.lang.Math.asin;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.toRadians;
import static java.lang.StrictMath.sin;
import static java.lang.StrictMath.sqrt;
import static java.lang.StrictMath.toDegrees;

/**
 * Created by rutvik on 9/15/2016 at 1:48 PM.
 */

public class FragmentMap extends Fragment implements OnMapReadyCallback, GoogleMap.OnCameraChangeListener, GoogleMap.OnCameraIdleListener
{
    private static final String TAG = App.APP_TAG + FragmentMap.class.getSimpleName();

    private SyncedMapFragment mapFragment;

    private GoogleMap mMap;

    private LatLngInterpolator mLatLngInterpolator;

    private MapWrapperLayout mapWrapperLayout;

    public boolean isReady = false;

    private Context context;

    private LatLng currentLatLng;

    private AppCompatEditText etCurrentLocation;

    private AddressResultReceiver mResultReceiver;

    final Handler mHandler = new Handler();

    private LatLng onCameraChangedLatLng;

    public static FragmentMap newInstance(int index, Context context)
    {
        FragmentMap fragmentMap = new FragmentMap();
        fragmentMap.context = context;
        Bundle b = new Bundle();
        b.putInt("index", index);
        fragmentMap.setArguments(b);
        return fragmentMap;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.map_fragment, container, false);

        mapWrapperLayout = (MapWrapperLayout) view.findViewById(R.id.map_wrapper_layout);

        mLatLngInterpolator = new LatLngInterpolator.Linear();


        etCurrentLocation = (AppCompatEditText) view.findViewById(R.id.et_currentLocation);

        mapFragment = (SyncedMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.frag_map);


        loadMapNow();

        return view;
    }

    public void placeCurrentLocationMarker(final LatLng latLng)
    {
        if (isReady)
        {

            currentLatLng = latLng;

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

            Intent intent = new Intent(getActivity(), GeocodeAddressIntentService.class);
            intent.putExtra(Constants.RECEIVER, mResultReceiver);
            intent.putExtra(Constants.FETCH_TYPE_EXTRA, Constants.USE_ADDRESS_LOCATION);

            intent.putExtra(Constants.LOCATION_LATITUDE_DATA_EXTRA,
                    latLng.latitude);
            intent.putExtra(Constants.LOCATION_LONGITUDE_DATA_EXTRA,
                    latLng.longitude);

            getActivity().startService(intent);

        }
    }


    private void loadMapNow()
    {
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        isReady = true;

        mMap = googleMap;

        mapWrapperLayout.init(mMap, getPixelsFromDp(context, 39 + 20));

        if (ActivityCompat
                .checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                &&
                ActivityCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            mMap.setMyLocationEnabled(true);
        }

        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setTiltGesturesEnabled(false);
        mMap.getUiSettings().setRotateGesturesEnabled(false);

        LatLng gujarat = new LatLng(23.012068, 72.5789153);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(gujarat, 11));

        mResultReceiver = new AddressResultReceiver(null);

        mMap.setOnCameraChangeListener(this);

        mMap.setOnCameraIdleListener(this);

/*        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener()
        {
            @Override
            public void onMyLocationChange(Location location)
            {
                mMap.animateCamera(CameraUpdateFactory
                        .newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15));
            }
        });*/

    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition)
    {
        etCurrentLocation.setText("Fetching location...");
        onCameraChangedLatLng = mMap.getCameraPosition().target;
        currentLatLng = mMap.getCameraPosition().target;
    }

    @Override
    public void onCameraIdle()
    {
        mHandler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                Intent intent = new Intent(getActivity(), GeocodeAddressIntentService.class);
                intent.putExtra(Constants.RECEIVER, mResultReceiver);
                intent.putExtra(Constants.FETCH_TYPE_EXTRA, Constants.USE_ADDRESS_LOCATION);

                intent.putExtra(Constants.LOCATION_LATITUDE_DATA_EXTRA,
                        onCameraChangedLatLng.latitude);

                intent.putExtra(Constants.LOCATION_LONGITUDE_DATA_EXTRA,
                        onCameraChangedLatLng.longitude);

                getActivity().startService(intent);
            }
        }, 1200);

    }


    public interface LatLngInterpolator
    {
        public LatLng interpolate(float fraction, LatLng a, LatLng b);

        public class Linear implements LatLngInterpolator
        {
            @Override
            public LatLng interpolate(float fraction, LatLng a, LatLng b)
            {
                double lat = (b.latitude - a.latitude) * fraction + a.latitude;
                double lng = (b.longitude - a.longitude) * fraction + a.longitude;
                return new LatLng(lat, lng);
            }
        }

        public class LinearFixed implements LatLngInterpolator
        {
            @Override
            public LatLng interpolate(float fraction, LatLng a, LatLng b)
            {
                double lat = (b.latitude - a.latitude) * fraction + a.latitude;
                double lngDelta = b.longitude - a.longitude;

                // Take the shortest path across the 180th meridian.
                if (Math.abs(lngDelta) > 180)
                {
                    lngDelta -= Math.signum(lngDelta) * 360;
                }
                double lng = lngDelta * fraction + a.longitude;
                return new LatLng(lat, lng);
            }
        }

        public class Spherical implements LatLngInterpolator
        {

            /* From github.com/googlemaps/android-maps-utils */
            @Override
            public LatLng interpolate(float fraction, LatLng from, LatLng to)
            {
                // http://en.wikipedia.org/wiki/Slerp
                double fromLat = toRadians(from.latitude);
                double fromLng = toRadians(from.longitude);
                double toLat = toRadians(to.latitude);
                double toLng = toRadians(to.longitude);
                double cosFromLat = cos(fromLat);
                double cosToLat = cos(toLat);

                // Computes Spherical interpolation coefficients.
                double angle = computeAngleBetween(fromLat, fromLng, toLat, toLng);
                double sinAngle = sin(angle);
                if (sinAngle < 1E-6)
                {
                    return from;
                }
                double a = sin((1 - fraction) * angle) / sinAngle;
                double b = sin(fraction * angle) / sinAngle;

                // Converts from polar to vector and interpolate.
                double x = a * cosFromLat * cos(fromLng) + b * cosToLat * cos(toLng);
                double y = a * cosFromLat * sin(fromLng) + b * cosToLat * sin(toLng);
                double z = a * sin(fromLat) + b * sin(toLat);

                // Converts interpolated vector back to polar.
                double lat = atan2(z, sqrt(x * x + y * y));
                double lng = atan2(y, x);
                return new LatLng(toDegrees(lat), toDegrees(lng));
            }

            private double computeAngleBetween(double fromLat, double fromLng, double toLat, double toLng)
            {
                // Haversine's formula
                double dLat = fromLat - toLat;
                double dLng = fromLng - toLng;
                return 2 * asin(sqrt(pow(sin(dLat / 2), 2) +
                        cos(fromLat) * cos(toLat) * pow(sin(dLng / 2), 2)));
            }
        }

    }

    public static int getPixelsFromDp(Context context, float dp)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }


    class AddressResultReceiver extends ResultReceiver
    {
        public AddressResultReceiver(Handler handler)
        {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, final Bundle resultData)
        {
            if (resultCode == Constants.SUCCESS_RESULT)
            {
                //final Address address = resultData.getParcelable(Constants.RESULT_ADDRESS);
                getActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        String address = resultData.getString(Constants.RESULT_DATA_KEY);
                        address = address.substring(0, address.length() - 2);
                        etCurrentLocation.setText(address);
                    }
                });
            } else
            {
                getActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {

                    }
                });
            }
        }
    }

    public String getCurrentPlace()
    {
        return etCurrentLocation.getText().toString();
    }

    public String getCurrentLat()
    {
        return currentLatLng.latitude + "";
    }

    public String getCurrentLng()
    {
        return currentLatLng.longitude + "";
    }

}

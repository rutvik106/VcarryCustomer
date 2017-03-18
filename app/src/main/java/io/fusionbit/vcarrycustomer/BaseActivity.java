package io.fusionbit.vcarrycustomer;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import butterknife.ButterKnife;
import extra.NetworkConnectionDetector;

/**
 * Created by rutvik on 3/17/2017 at 12:09 PM.
 */

public abstract class BaseActivity extends AppCompatActivity
        implements NetworkConnectionDetector.ConnectivityReceiverListener
{

    private static final String TAG = App.APP_TAG + BaseActivity.class.getSimpleName();
    private NetworkConnectionDetector networkConnectionDetector;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());

        ButterKnife.bind(this);

    }

    @Override
    protected void onStart()
    {
        super.onStart();
        IntentFilter i = new IntentFilter();
        i.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        networkConnectionDetector = new NetworkConnectionDetector();
        registerReceiver(networkConnectionDetector, i);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        NetworkConnectionDetector.connectivityReceiverListener = this;
        checkInternet();
    }

    @Override
    protected void onStop()
    {
        if (networkConnectionDetector != null)
        {
            unregisterReceiver(networkConnectionDetector);
        }
        super.onStop();
    }

    public void checkInternet()
    {
        final ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected())
        {
            internetAvailable();
        } else
        {
            internetNotAvailable();
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected)
    {
        if (isConnected)
        {
            Log.i(TAG, "CONNECTED>>>");
            internetAvailable();
        } else
        {
            Log.i(TAG, "<<<DISCONNECTED");
            internetNotAvailable();
        }
    }

    protected abstract int getLayoutResourceId();

    protected abstract void internetNotAvailable();

    protected abstract void internetAvailable();

}

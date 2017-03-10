package extra;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Build;
import android.util.Log;

import io.fusionbit.vcarrycustomer.App;


/**
 * Created by rutvik on 1/7/2017 at 1:23 PM.
 */

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class ConnectionStateMonitor extends ConnectivityManager.NetworkCallback
{

    final NetworkRequest networkRequest;

    Context context;

    public ConnectionStateMonitor()
    {
        networkRequest = new NetworkRequest.Builder().addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR).addTransportType(NetworkCapabilities.TRANSPORT_WIFI).build();
    }

    public void enable(Context context)
    {
        this.context = context;

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        connectivityManager.registerNetworkCallback(networkRequest, this);
    }

    // Likewise, you can have a disable method that simply calls ConnectivityManager#unregisterCallback(networkRequest) too.

    @Override
    public void onAvailable(Network network)
    {
        Log.i(App.APP_TAG, "NETWORK IS AVAILABLE MSG FROM ConnectionStateMonitor.java file");
    }
}

package extra;

/**
 * Created by rutvik on 9/22/2016 at 3:08 PM.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by rutvik on 06-07-2016 at 12:11 AM.
 */

public class NetworkConnectionDetector extends BroadcastReceiver
{

    public static final String CONNECTIVITY_CHANGED = "io.fusionbit.khalaiyo.CONNECTIVITY_CHANGED";

    @Override
    public void onReceive(Context context, Intent intent)
    {


        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected())
        {
            context.sendBroadcast(new Intent(CONNECTIVITY_CHANGED).putExtra("is_active", true));
        } else
        {
            context.sendBroadcast(new Intent(CONNECTIVITY_CHANGED).putExtra("is_active", false));
        }

    }

    public static boolean isInternetOn(final Context context)
    {
        final ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected())
        {
            return true;
        } else
        {
            return false;
        }
    }

}

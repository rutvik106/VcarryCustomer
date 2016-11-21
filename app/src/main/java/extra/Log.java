package extra;

/**
 * Created by rutvik on 10/26/2016 at 9:49 AM.
 */

public class Log
{

    private static boolean doLog = true;

    public static void i(String TAG, String message)
    {
        if (doLog)
        {
            android.util.Log.i(TAG, message);
        }
    }

}

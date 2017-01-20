package fcm;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import extra.Log;
import io.fusionbit.vcarrycustomer.App;

/**
 * Created by rutvik on 1/17/2017 at 9:59 AM.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService
{
    private static final String TAG = App.APP_TAG + MyFirebaseMessagingService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {
        Log.i(TAG, remoteMessage.toString());
    }
}

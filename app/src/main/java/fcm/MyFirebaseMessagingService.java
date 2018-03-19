package fcm;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import api.API;
import extra.Log;
import io.fusionbit.vcarrycustomer.App;
import io.fusionbit.vcarrycustomer.NotificationHandler;
import io.realm.Realm;

/**
 * Created by rutvik on 1/17/2017 at 9:59 AM.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = App.APP_TAG + MyFirebaseMessagingService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage message) {
        String from = message.getFrom();
        Map data = message.getData();
        Log.i(TAG, "DATA: " + data.toString() + " FROM: " + from);

        new NotificationHandler(this, Realm.getDefaultInstance().getConfiguration(),
                API.getInstance(),
                message)
                .handleNotification();
    }

}

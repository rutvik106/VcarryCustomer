package fcm;

import android.preference.PreferenceManager;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import extra.Log;
import io.fusionbit.vcarrycustomer.App;
import io.fusionbit.vcarrycustomer.Constants;

/**
 * Created by rutvik on 1/17/2017 at 10:02 AM.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = App.APP_TAG + MyFirebaseInstanceIDService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        PreferenceManager.getDefaultSharedPreferences(this)
                .edit()
                .putString(Constants.FCM_INSTANCE_ID, refreshedToken)
                .apply();

        Log.i(TAG, "REFRESHED DEVICE TOKEN");
        Log.i(TAG, refreshedToken);

    }
}

package io.fusionbit.vcarrycustomer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import api.API;
import api.RetrofitCallbacks;
import butterknife.ButterKnife;
import extra.NetworkConnectionDetector;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import static io.fusionbit.vcarrycustomer.Constants.IS_EMULATOR;
import static io.fusionbit.vcarrycustomer.Constants.TEST_PHONE_NUMBER;

/**
 * Created by rutvik on 3/17/2017 at 12:09 PM.
 */

public abstract class BaseActivity extends AppCompatActivity
        implements NetworkConnectionDetector.ConnectivityReceiverListener {

    private static final String TAG = App.APP_TAG + BaseActivity.class.getSimpleName();
    private NetworkConnectionDetector networkConnectionDetector;

    private AlertDialog notRegisterDialog;

    private Call<List<Integer>> getCustomerIdFromEmail;

    private String customerId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());

        ButterKnife.bind(this);



    }

    public String getCustomerId() {
        return customerId;
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter i = new IntentFilter();
        i.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        networkConnectionDetector = new NetworkConnectionDetector();
        registerReceiver(networkConnectionDetector, i);

        if (FirebaseAuth.getInstance().getCurrentUser() != null || IS_EMULATOR == true) {
            tryToGetCustomerIdFromCustomerPhone(IS_EMULATOR ? Constants.TEST_PHONE_NUMBER : FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        NetworkConnectionDetector.connectivityReceiverListener = this;
        checkInternet();
    }

    @Override
    protected void onStop() {
        if (networkConnectionDetector != null) {
            unregisterReceiver(networkConnectionDetector);
        }
        super.onStop();
    }

    public void checkInternet() {
        final ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            internetAvailable();
        } else {
            internetNotAvailable();
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected) {
            Log.i(TAG, "CONNECTED>>>");
            internetAvailable();
        } else {
            Log.i(TAG, "<<<DISCONNECTED");
            internetNotAvailable();
        }
    }

    protected abstract int getLayoutResourceId();

    protected abstract void internetNotAvailable();

    protected abstract void internetAvailable();

    private void tryToGetCustomerIdFromCustomerPhone(String phone) {

        if (getCustomerIdFromEmail != null) {
            getCustomerIdFromEmail.cancel();
        }

        final RetrofitCallbacks<List<Integer>> onGetCustomerIdCallback =
                new RetrofitCallbacks<List<Integer>>() {
                    @Override
                    public void onResponse(Call<List<Integer>> call, Response<List<Integer>> response) {
                        super.onResponse(call, response);
                        if (response.isSuccessful()) {
                            if (response.body() == null) {
                                Toast.makeText(BaseActivity.this, "customer ID not found: " +
                                        "Response NULL", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            if (response.body().get(0) > 0) {
                                PreferenceManager.getDefaultSharedPreferences(BaseActivity.this)
                                        .edit()
                                        .putString(Constants.CUSTOMER_ID, String.valueOf(response.body().get(0)))
                                        .apply();

                                customerId = String.valueOf(response.body().get(0));

                                updateFcmDeviceToken(String.valueOf(response.body().get(0)));

                                Toast.makeText(BaseActivity.this, R.string.registered_customer, Toast.LENGTH_SHORT).show();

                                if (notRegisterDialog != null) {
                                    if (notRegisterDialog.isShowing()) {
                                        notRegisterDialog.dismiss();
                                    }
                                }

                                /*if (showIt)
                                {
                                    showIt = false;
                                    promptForRegistration();
                                }*/

                            } else {
                                PreferenceManager.getDefaultSharedPreferences(BaseActivity.this)
                                        .edit()
                                        .putString(Constants.CUSTOMER_ID, null)
                                        .apply();

                                Toast.makeText(BaseActivity.this,
                                        "Something went wrong, Please try again later",
                                        Toast.LENGTH_SHORT).show();

                                Toast.makeText(BaseActivity.this, "customer ID not found: "
                                        + response.body(), Toast.LENGTH_SHORT).show();

                                promptForRegistration(BaseActivity.this);
                            }
                        } else {
                            Toast.makeText(BaseActivity.this, "cannot get customer ID API RESPONSE" +
                                    " not OK: " + response.code(), Toast.LENGTH_SHORT).show();

                            promptForRegistration(BaseActivity.this);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Integer>> call, Throwable t) {
                        super.onFailure(call, t);

                        if (!call.isCanceled()) {
                            promptForRegistration(BaseActivity.this);
                            /*Toast.makeText(ActivityHome.this, "cannot get customer ID RETROFIT ON FAILURE: " +
                                    t.getMessage(), Toast.LENGTH_SHORT).show();*/
                        }
                    }
                };

        getCustomerIdFromEmail = API.getInstance().getCustomerIdFromPhone(phone, onGetCustomerIdCallback);

    }


    private void promptForRegistration(final Context context) {
        final String customerId = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(Constants.CUSTOMER_ID, null);
        if (customerId == null) {
            notRegisterDialog = new AlertDialog.Builder(context)
                    .setTitle("Not Registered")
                    .setMessage("Dear Customer, you're not yet registered with V-Carry. \n" +
                            "\n" +
                            "Please call 7575-8888-48 to register your account, and let us take load off your business.")
                    .setCancelable(false)
                    .setNegativeButton("Try Again", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            tryToGetCustomerIdFromCustomerPhone(IS_EMULATOR ? TEST_PHONE_NUMBER : FirebaseAuth.getInstance()
                                    .getCurrentUser().getPhoneNumber());
                        }
                    })
                    .setPositiveButton("CALL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Utils.dialNumber(context,"7575888848");
                        }
                    })
                    .setNeutralButton("LOGOUT", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int j) {
                            FirebaseAuth.getInstance().signOut();
                            final Intent i = new Intent(context, ActivityPhoneAuth.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                        }
                    })
                    .show();
        }
        /*final String customerId = PreferenceManager.getDefaultSharedPreferences(ActivityHome.this)
                .getString(Constants.CUSTOMER_ID, null);
        ActivityHome.this.customerId = customerId;
        if (customerId == null)
        {
            final Intent i = new Intent(this, ActivityRegistrationForm.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }*/
    }

    private void updateFcmDeviceToken(String customerId) {
        final String fcmDeviceToken = PreferenceManager.getDefaultSharedPreferences(this)
                .getString(Constants.FCM_INSTANCE_ID, null);

        final RetrofitCallbacks<ResponseBody> onUpdateDeviceTokenCallback =
                new RetrofitCallbacks<ResponseBody>() {

                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        super.onResponse(call, response);
                    }
                };

        if (fcmDeviceToken != null) {
            API.getInstance().updateDeviceTokenCustomer(customerId, fcmDeviceToken, onUpdateDeviceTokenCallback);
        } else {
            Toast.makeText(this, "FCM Instance ID not found!", Toast.LENGTH_SHORT).show();
        }


    }

}

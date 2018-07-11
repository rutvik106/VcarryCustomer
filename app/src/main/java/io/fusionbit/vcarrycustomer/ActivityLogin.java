package io.fusionbit.vcarrycustomer;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.MainThread;
import android.support.annotation.StyleRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import extra.Log;

public class ActivityLogin extends BaseActivity {

    private static final String TAG = App.APP_TAG + ActivityLogin.class.getSimpleName();
    private static final String GOOGLE_TOS_URL =
            "https://www.google.com/policies/terms/";
    private static final int RC_SIGN_IN = 100;
    BroadcastReceiver networkChangeReceiver;
    private boolean isConnected = false;
    private FirebaseAuth mFirebaseAuth;
    private Intent intent;
    private int PERMISSIONS_REQUEST = 324;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(TAG, "OnCreate");

        checkForPermissions();

    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_login;
    }

    @Override
    protected void internetNotAvailable() {

    }

    @Override
    protected void internetAvailable() {

    }

    private void checkForPermissions() {
        onPermissionGranted();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case RC_SIGN_IN:
                handleSignInResponse(resultCode, data);
                break;

            default:

                break;
        }

    }


    private void tryLogin() {
        if (!isConnected) {
            return;
        }

        //user not logged in
        //show firebase login methods using firebase auth UI
        /*startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                        .setTheme(getSelectedTheme())
                        .setTosUrl(getSelectedTosUrl())
                        .setLogo(getSelectedLogo())
                        .setProviders(getSelectedProviders())
                        .build(), RC_SIGN_IN);*/


    }

    //user signed in response
    @MainThread
    private void handleSignInResponse(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            startActivity(intent);
        }

        if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
        }

    }


    @MainThread
    private String getSelectedTosUrl() {

        return GOOGLE_TOS_URL;

    }

    @MainThread
    @StyleRes
    private int getSelectedTheme() {

        //return R.style.LoginTheme; // AuthUI.getDefaultTheme();
        return 0; // AuthUI.getDefaultTheme();

    }

    @MainThread
    @DrawableRes
    private int getSelectedLogo() {
        return R.drawable.logo;
    }

    //set login methods/providers (GOOGLE/FACEBOOK)
    @MainThread
    private String[] getSelectedProviders() {
        ArrayList<String> selectedProviders = new ArrayList<>();

        //selectedProviders.add(AuthUI.GOOGLE_PROVIDER);

        return selectedProviders.toArray(new String[selectedProviders.size()]);
    }

    public void onPermissionGranted() {

        intent = new Intent(ActivityLogin.this, ActivityHome.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        findViewById(R.id.btn_googleSignIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isConnected) {
                    tryLogin();
                } else {
                    Toast.makeText(ActivityLogin.this, "check internet connection", Toast.LENGTH_SHORT).show();
                }

            }
        });


        //check internet connectivity
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            isConnected = true;
        } else {
            isConnected = false;
        }

        //listen to network changes
        networkChangeReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getBooleanExtra("is_active", false)) {
                    isConnected = true;
                } else {
                    isConnected = false;
                }
            }
        };

        mFirebaseAuth = FirebaseAuth.getInstance();

        if (mFirebaseAuth.getCurrentUser() != null) {
            startActivity(intent);
        }


    }

    public void onPermissionDenied(ArrayList<String> deniedPermissions) {
        finish();
    }
}

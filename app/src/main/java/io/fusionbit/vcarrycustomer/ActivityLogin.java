package io.fusionbit.vcarrycustomer;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.MainThread;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

import extra.Log;

public class ActivityLogin extends BaseActivity implements PermissionListener
{

    private static final String TAG = App.APP_TAG + ActivityLogin.class.getSimpleName();
    private static final String GOOGLE_TOS_URL =
            "https://www.google.com/policies/terms/";
    private static final int RC_SIGN_IN = 100;
    BroadcastReceiver networkChangeReceiver;
    private boolean isConnected = false;
    private FirebaseAuth mFirebaseAuth;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Log.i(TAG, "OnCreate");

        checkForPermissions();

    }

    @Override
    protected int getLayoutResourceId()
    {
        return R.layout.activity_login;
    }

    @Override
    protected void internetNotAvailable()
    {

    }

    @Override
    protected void internetAvailable()
    {

    }

    private void checkForPermissions()
    {
        TedPermission.with(ActivityLogin.this)
                .setPermissionListener(this)
                .setDeniedMessage("If you reject any permission, you can not use this App\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET,
                        Manifest.permission.SYSTEM_ALERT_WINDOW,
                        Manifest.permission.VIBRATE,
                        Manifest.permission.WAKE_LOCK,
                        Manifest.permission.DISABLE_KEYGUARD,
                        Manifest.permission.RECEIVE_BOOT_COMPLETED)
                .check();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode)
        {

            case RC_SIGN_IN:
                handleSignInResponse(resultCode, data);
                break;

            default:

                break;
        }

    }


    private void tryLogin()
    {
        if (!isConnected)
        {
            return;
        }

        //user not logged in
        //show firebase login methods using firebase auth UI
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                        .setTheme(getSelectedTheme())
                        .setTosUrl(getSelectedTosUrl())
                        .setLogo(getSelectedLogo())
                        .setProviders(getSelectedProviders())
                        .build(), RC_SIGN_IN);


    }

    //user signed in response
    @MainThread
    private void handleSignInResponse(int resultCode, Intent data)
    {
        if (resultCode == RESULT_OK)
        {
            startActivity(intent);
        }

        if (resultCode == RESULT_CANCELED)
        {
            Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
        }

    }


    @MainThread
    private String getSelectedTosUrl()
    {

        return GOOGLE_TOS_URL;

    }

    @MainThread
    @StyleRes
    private int getSelectedTheme()
    {

        return R.style.LoginTheme; // AuthUI.getDefaultTheme();

    }

    @MainThread
    @DrawableRes
    private int getSelectedLogo()
    {
        return R.drawable.logo;
    }

    //set login methods/providers (GOOGLE/FACEBOOK)
    @MainThread
    private String[] getSelectedProviders()
    {
        ArrayList<String> selectedProviders = new ArrayList<>();

        selectedProviders.add(AuthUI.GOOGLE_PROVIDER);

        return selectedProviders.toArray(new String[selectedProviders.size()]);
    }

    @Override
    public void onPermissionGranted()
    {

        intent = new Intent(ActivityLogin.this, ActivityHome.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        findViewById(R.id.btn_googleSignIn).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                if (isConnected)
                {
                    tryLogin();
                } else
                {
                    Toast.makeText(ActivityLogin.this, "check internet connection", Toast.LENGTH_SHORT).show();
                }

            }
        });


        //check internet connectivity
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected())
        {
            isConnected = true;
        } else
        {
            isConnected = false;
        }

        //listen to network changes
        networkChangeReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                if (intent.getBooleanExtra("is_active", false))
                {
                    isConnected = true;
                } else
                {
                    isConnected = false;
                }
            }
        };

        mFirebaseAuth = FirebaseAuth.getInstance();

        if (mFirebaseAuth.getCurrentUser() != null)
        {
            startActivity(intent);
        }


    }

    @Override
    public void onPermissionDenied(ArrayList<String> deniedPermissions)
    {
        finish();
    }
}

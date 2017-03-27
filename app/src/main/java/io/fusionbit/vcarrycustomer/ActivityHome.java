package io.fusionbit.vcarrycustomer;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;

import api.API;
import api.RetrofitCallbacks;
import butterknife.BindView;
import butterknife.ButterKnife;
import extra.ConnectionStateMonitor;
import extra.LocaleHelper;
import fragments.FragmentAccBalance;
import fragments.FragmentHome;
import fragments.FragmentTrips;
import io.realm.Realm;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import static io.fusionbit.vcarrycustomer.Constants.WAS_LANGUAGE_CHANGED;

public class ActivityHome extends VCarryActivity
        implements NavigationView.OnNavigationItemSelectedListener
{
    private static final String TAG = App.APP_TAG + ActivityHome.class.getSimpleName();

    FragmentTransaction ft;

    @Inject
    API api;

    @Inject
    Realm realm;

    @BindView(R.id.cl_navActivityLayout)
    CoordinatorLayout clNavActivityLayout;

    Snackbar snackbarNoInternet;
    boolean exitApp = false;
    Fragment currentFragment = null;
    private BroadcastReceiver mNetworkDetectReceiver;

    private void checkInternetConnection()
    {
        if (!Utils.isDeviceOnline(this))
        {
            showNoInternetView();
        } else
        {
            if (snackbarNoInternet != null)
            {
                if (snackbarNoInternet.isShown())
                {
                    snackbarNoInternet.dismiss();
                }
            }
        }
    }

    private void showNoInternetView()
    {
        snackbarNoInternet = Snackbar
                .make(clNavActivityLayout, "No Internet", Snackbar.LENGTH_INDEFINITE);

        snackbarNoInternet.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setActionBarTitle("V-Carry");

        ((App) getApplication()).getUser().inject(this);

        setupConnectionMonitor();

        LocaleHelper.onCreate(this, LocaleHelper.getLanguage(this));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (FirebaseAuth.getInstance().getCurrentUser() != null)
        {
            tryToGetCustomerIdFromCustomerEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail());

            Glide.with(this).load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl())
                    .bitmapTransform(new CropCircleTransformation(this))
                    .into((ImageView) navigationView.getHeaderView(0).findViewById(R.id.iv_userPic));

            ((TextView) navigationView.getHeaderView(0).findViewById(R.id.tv_titleUserName))
                    .setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());

            ((TextView) navigationView.getHeaderView(0).findViewById(R.id.tv_subTitle))
                    .setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, FragmentHome.newInstance(0));
            ft.commitAllowingStateLoss();

            navigationView.getMenu().getItem(0).setChecked(true);

        } else
        {
            finish();
        }
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        mNetworkDetectReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                checkInternetConnection();
            }
        };
        registerReceiver(mNetworkDetectReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onStop()
    {
        if (mNetworkDetectReceiver != null)
        {
            unregisterReceiver(mNetworkDetectReceiver);
        }
        super.onStop();
    }

    private void tryToGetCustomerIdFromCustomerEmail(String email)
    {

        final RetrofitCallbacks<Integer> onGetCustomerIdCallback =
                new RetrofitCallbacks<Integer>()
                {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response)
                    {
                        super.onResponse(call, response);
                        if (response.isSuccessful())
                        {
                            System.out.println(response.body());

                            if (response.body() > 0)
                            {
                                PreferenceManager.getDefaultSharedPreferences(ActivityHome.this)
                                        .edit()
                                        .putString(Constants.CUSTOMER_ID, String.valueOf(response.body()))
                                        .apply();

                                updateFcmDeviceToken(String.valueOf(response.body()));

                            } else
                            {
                                PreferenceManager.getDefaultSharedPreferences(ActivityHome.this)
                                        .edit()
                                        .putString(Constants.CUSTOMER_ID, null)
                                        .apply();

                                Toast.makeText(ActivityHome.this,
                                        "Something went wrong, Please try again later",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                        promptForRegistration();
                    }

                    @Override
                    public void onFailure(Call<Integer> call, Throwable t)
                    {
                        super.onFailure(call, t);
                        final String customerId = PreferenceManager.getDefaultSharedPreferences(ActivityHome.this)
                                .getString(Constants.CUSTOMER_ID, null);
                        if (customerId == null)
                        {
                            promptForRegistration();
                        } else
                        {
                            Toast.makeText(ActivityHome.this, "please check network connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                };

        api.getCustomerIdFromEmail(email, onGetCustomerIdCallback);

    }

    private void updateFcmDeviceToken(String customerId)
    {
        final String fcmDeviceToken = PreferenceManager.getDefaultSharedPreferences(this)
                .getString(Constants.FCM_INSTANCE_ID, null);

        final RetrofitCallbacks<ResponseBody> onUpdateDeviceTokenCallback =
                new RetrofitCallbacks<ResponseBody>()
                {

                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
                    {
                        super.onResponse(call, response);
                    }
                };

        if (fcmDeviceToken != null)
        {
            api.updateDeviceTokenCustomer(customerId, fcmDeviceToken, onUpdateDeviceTokenCallback);
        } else
        {
            Toast.makeText(this, "FCM Instance ID not found!", Toast.LENGTH_SHORT).show();
        }


    }

    private void setActionBarTitle(String title)
    {

        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setTitle(title);
        }

    }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        } else
        {
            if (exitApp)
            {
                super.onBackPressed();
            }
            exitApp = true;
            Toast.makeText(this, "Press BACK again to exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    exitApp = false;
                }
            }, 2000);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            startActivityForResult(new Intent(this, ActivitySettings.class), Constants.CHANGE_LANGUAGE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home)
        {
            currentFragment = FragmentHome.newInstance(0);
            setActionBarTitle("V-Carry");
        } else if (id == R.id.nav_trips)
        {
            currentFragment = FragmentTrips.newInstance(1);
            setActionBarTitle(getResources().getString(R.string.actionbar_title_trips));
        } else if (id == R.id.nav_accountBalance)
        {
            currentFragment = FragmentAccBalance.newInstance(2);
            setActionBarTitle(getResources().getString(R.string.actionbar_title_trips));
        } else if (id == R.id.nav_tripsOnOffer)
        {

        } else if (id == R.id.nav_share)
        {

        } else if (id == R.id.nav_sendFeedback)
        {

        }

        if (currentFragment != null)
        {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, currentFragment);
            ft.commitAllowingStateLoss();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == Activity.RESULT_OK)
        {
            switch (requestCode)
            {
                case Constants.CHANGE_LANGUAGE:
                    boolean wasChanged = data.getExtras().getBoolean(WAS_LANGUAGE_CHANGED, false);
                    if (wasChanged)
                    {
                        this.recreate();
                    }
                    break;
            }
        }
    }

    private void promptForRegistration()
    {
        final String customerId = PreferenceManager.getDefaultSharedPreferences(this)
                .getString(Constants.CUSTOMER_ID, null);

        if (customerId == null)
        {
            final Intent i = new Intent(this, ActivityRegistrationForm.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setupConnectionMonitor()
    {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            new ConnectionStateMonitor().enable(this);
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if (currentFragment != null)
        {
            getSupportFragmentManager().beginTransaction().detach(currentFragment).attach(currentFragment).commit();
        }
    }
}

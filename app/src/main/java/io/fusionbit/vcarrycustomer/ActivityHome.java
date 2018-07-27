package io.fusionbit.vcarrycustomer;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.BaseColumns;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

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
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import static io.fusionbit.vcarrycustomer.Constants.IS_EMULATOR;
import static io.fusionbit.vcarrycustomer.Constants.WAS_LANGUAGE_CHANGED;

public class ActivityHome extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = App.APP_TAG + ActivityHome.class.getSimpleName();

    FragmentTransaction ft;

    private Realm realm;

    @BindView(R.id.cl_navActivityLayout)
    CoordinatorLayout clNavActivityLayout;

    Snackbar snackbarNoInternet;
    boolean exitApp = false;
    Fragment currentFragment = null;
    MenuItem searchMenu;
    SearchView searchView;
    Call<List<String>> tripSearchCall;
    private BroadcastReceiver mNetworkDetectReceiver;
    private SimpleCursorAdapter tripSearchResultCursorAdapter;
    private String[] strArrData = {"No Suggestions"};
    private boolean showIt = true;

    private void checkInternetConnection() {
        if (!Utils.isDeviceOnline(this)) {
            showNoInternetView();
        } else {
            if (snackbarNoInternet != null) {
                if (snackbarNoInternet.isShown()) {
                    snackbarNoInternet.dismiss();
                }
            }
        }
    }

    private void showNoInternetView() {
        snackbarNoInternet = Snackbar
                .make(clNavActivityLayout, "No Internet", Snackbar.LENGTH_INDEFINITE);

        snackbarNoInternet.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);

        realm = Realm.getDefaultInstance();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setActionBarTitle(getString(R.string.app_name));

        setupConnectionMonitor();

        LocaleHelper.onCreate(this, LocaleHelper.getLanguage(this));

        final String[] from = new String[]{"tripNumber"};
        final int[] to = new int[]{android.R.id.text1};

        tripSearchResultCursorAdapter =
                new SimpleCursorAdapter(this, android.R.layout.simple_spinner_dropdown_item,
                        null, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (FirebaseAuth.getInstance().getCurrentUser() != null || IS_EMULATOR == true) {

            /*Glide.with(this).load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl())
                    .bitmapTransform(new CropCircleTransformation(this))
                    .into((ImageView) navigationView.getHeaderView(0).findViewById(R.id.iv_userPic));*/

            /*((TextView) navigationView.getHeaderView(0).findViewById(R.id.tv_titleUserName))
                    .setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());*/

            ((TextView) navigationView.getHeaderView(0).findViewById(R.id.tv_subTitle))
                    .setText(IS_EMULATOR ? Constants.TEST_PHONE_NUMBER :
                            FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());

            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, FragmentHome.newInstance(0));
            ft.commitAllowingStateLoss();

            navigationView.getMenu().getItem(0).setChecked(true);

        } else {
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mNetworkDetectReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                checkInternetConnection();
            }
        };
        registerReceiver(mNetworkDetectReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onStop() {
        if (mNetworkDetectReceiver != null) {
            unregisterReceiver(mNetworkDetectReceiver);
        }
        super.onStop();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_home;
    }

    @Override
    protected void internetNotAvailable() {
        showNoInternetView();
    }

    @Override
    protected void internetAvailable() {
        checkInternetConnection();
    }

    private void setActionBarTitle(String title) {

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (exitApp) {
                super.onBackPressed();
            }
            exitApp = true;
            Toast.makeText(this, "Press BACK again to exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exitApp = false;
                }
            }, 2000);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_home, menu);

        // Retrieve the SearchView and plug it into SearchManager
        searchMenu = menu.findItem(R.id.action_search);
        searchMenu.setVisible(false);
        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSuggestionsAdapter(tripSearchResultCursorAdapter);

        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionClick(int position) {
                // Add clicked text to search box
                CursorAdapter ca = searchView.getSuggestionsAdapter();
                Cursor cursor = ca.getCursor();
                cursor.moveToPosition(position);
                searchView.setQuery(cursor.getString(cursor.getColumnIndex("tripNumber")), false);

                ActivityTripDetails.start(cursor.getString(cursor.getColumnIndex("tripNumber")),
                        ActivityHome.this);

                return true;
            }

            @Override
            public boolean onSuggestionSelect(int position) {
                return true;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String s) {
                if (tripSearchCall != null) {
                    tripSearchCall.cancel();
                }

                tripSearchCall = API.getInstance().getTripNumberLike(s, getCustomerId(), new RetrofitCallbacks<List<String>>() {

                    @Override
                    public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                        super.onResponse(call, response);
                        if (response.isSuccessful()) {
                            if (response.body() == null) {
                                return;
                            }
                            strArrData = response.body().toArray(new String[0]);
                            // Filter data
                            final MatrixCursor mc = new MatrixCursor(new String[]{BaseColumns._ID, "tripNumber"});
                            for (int i = 0; i < strArrData.length; i++) {
                                if (strArrData[i].toLowerCase().contains(s.toLowerCase())) {
                                    mc.addRow(new Object[]{i, strArrData[i]});
                                }
                            }
                            tripSearchResultCursorAdapter.changeCursor(mc);
                            tripSearchResultCursorAdapter.notifyDataSetChanged();
                        }
                    }
                });
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivityForResult(new Intent(this, ActivitySettings.class), Constants.CHANGE_LANGUAGE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            currentFragment = FragmentHome.newInstance(0);
            setActionBarTitle(getString(R.string.app_name));
            searchMenu.setVisible(false);
        } else if (id == R.id.nav_trips) {
            currentFragment = FragmentTrips.newInstance(1);
            setActionBarTitle(getResources().getString(R.string.actionbar_title_trips));
            searchMenu.setVisible(true);
        } else if (id == R.id.nav_accountBalance) {
            currentFragment = FragmentAccBalance.newInstance(2);
            setActionBarTitle(getResources().getString(R.string.nav_accountBalance));
            searchMenu.setVisible(false);
        } else if (id == R.id.nav_tripsOnOffer) {
            Toast.makeText(this, R.string.feature_coming_soon, Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_share) {
            Utils.ShareApp(this);
        } else if (id == R.id.nav_sendFeedback) {
            Utils.sendFeedback(this);
        } else if (id == R.id.nav_signOut) {
            confirmLogout();
        }

        if (currentFragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, currentFragment);
            ft.commitAllowingStateLoss();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void confirmLogout() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.nav_signOut))
                .setMessage(getString(R.string.logout_message))
                .setPositiveButton(getString(R.string.nav_signOut), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int j) {
                        realm.beginTransaction();
                        realm.deleteAll();
                        realm.commitTransaction();
                        PreferenceManager.getDefaultSharedPreferences(ActivityHome.this)
                                .edit()
                                .putString(Constants.CUSTOMER_ID, null)
                                .apply();
                        FirebaseAuth.getInstance().signOut();
                        final Intent i = new Intent(ActivityHome.this, ActivityPhoneAuth.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    }
                })
                .setNegativeButton(getString(R.string.cancel), null)
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case Constants.CHANGE_LANGUAGE:
                    boolean wasChanged = data.getExtras().getBoolean(WAS_LANGUAGE_CHANGED, false);
                    if (wasChanged) {
                        this.recreate();
                    }
                    break;
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setupConnectionMonitor() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            new ConnectionStateMonitor().enable(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (currentFragment != null) {
            getSupportFragmentManager().beginTransaction().detach(currentFragment).attach(currentFragment).commit();
        }
    }

    @Override
    protected void onDestroy() {
        realm.close();
        super.onDestroy();
    }
}

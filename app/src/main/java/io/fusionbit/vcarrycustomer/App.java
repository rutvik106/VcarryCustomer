package io.fusionbit.vcarrycustomer;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import components.DaggerVcarryApi;
import components.VcarryApi;
import extra.LocaleHelper;
import extra.Log;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import module.ApiModule;
import module.AppModule;

/**
 * Created by rutvik on 11/18/2016 at 10:29 PM.
 */

public class App extends MultiDexApplication
{

    public static final String APP_TAG = "VCRY ";

    private static final String TAG = APP_TAG + App.class.getSimpleName();

    private VcarryApi vcarryApi;

    @Override
    public void onCreate()
    {
        super.onCreate();

        LocaleHelper.onCreate(this, LocaleHelper.getLanguage(this));

        Realm.init(this);

        final RealmConfiguration realmConfig = new RealmConfiguration
                .Builder()
                .name(Constants.REALM_DATABASE_NAME)
                .deleteRealmIfMigrationNeeded()
                .build();

        vcarryApi = DaggerVcarryApi.builder()
                .appModule(new AppModule(this))
                .apiModule(new ApiModule(Constants.API_BASE_URL))
                .build();


        Realm.setDefaultConfiguration(realmConfig);

        Log.i(TAG, "Application Created!!!");
    }

    public VcarryApi getVcarryApi()
    {
        return vcarryApi;
    }

    @Override
    protected void attachBaseContext(Context base)
    {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}

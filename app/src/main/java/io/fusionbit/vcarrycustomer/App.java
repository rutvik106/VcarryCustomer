package io.fusionbit.vcarrycustomer;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import extra.LocaleHelper;
import extra.Log;
import extra.TypefaceUtil;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by rutvik on 11/18/2016 at 10:29 PM.
 */

public class App extends MultiDexApplication
{

    public static final String APP_TAG = "VCRY ";

    private static final String TAG = APP_TAG + App.class.getSimpleName();

    @Override
    public void onCreate()
    {
        super.onCreate();

        LocaleHelper.onCreate(this,LocaleHelper.getLanguage(this));

        Realm.init(this);

        final RealmConfiguration realmConfig = new RealmConfiguration
                .Builder()
                .name(Constants.REALM_DATABASE_NAME)
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(realmConfig);

        Log.i(TAG, "Application Created!!!");
    }


    @Override
    protected void attachBaseContext(Context base)
    {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}

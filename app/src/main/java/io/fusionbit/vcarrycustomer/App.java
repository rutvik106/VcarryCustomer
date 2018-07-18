package io.fusionbit.vcarrycustomer;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

import extra.LocaleHelper;
import extra.Log;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by rutvik on 11/18/2016 at 10:29 PM.
 */

public class App extends Application {

    public static final String APP_TAG = "VCRY ";

    private static final String TAG = APP_TAG + App.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();

        Log.i(TAG, "Application Created!!!");

        Realm.init(this);

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(realmConfiguration);

        LocaleHelper.onCreate(this, LocaleHelper.getLanguage(this));

    }

    /*@Override
    protected void attachBaseContext(Context base)
    {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }*/
}

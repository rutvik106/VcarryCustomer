package io.fusionbit.vcarrycustomer;

import android.app.Application;

import components.DaggerUser;
import components.User;
import extra.LocaleHelper;
import extra.Log;
import io.realm.Realm;
import module.AppModule;
import module.RealmConfigModule;

/**
 * Created by rutvik on 11/18/2016 at 10:29 PM.
 */

public class App extends Application
{

    public static final String APP_TAG = "VCRY ";

    private static final String TAG = APP_TAG + App.class.getSimpleName();

    private User user;

    @Override
    public void onCreate()
    {
        super.onCreate();

        Log.i(TAG, "Application Created!!!");

        Realm.init(this);

        LocaleHelper.onCreate(this, LocaleHelper.getLanguage(this));

        user = DaggerUser.builder()
                .appModule(new AppModule(this))
                .realmConfigModule(new RealmConfigModule(Constants.REALM_DATABASE_NAME))
                .build();
    }

    public User getUser()
    {
        return user;
    }

    /*@Override
    protected void attachBaseContext(Context base)
    {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }*/
}

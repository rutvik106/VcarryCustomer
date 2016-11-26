package io.fusionbit.vcarrycustomer;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import extra.LocaleHelper;
import extra.Log;
import extra.TypefaceUtil;

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

        Log.i(TAG, "Application Created!!!");
    }


    @Override
    protected void attachBaseContext(Context base)
    {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}

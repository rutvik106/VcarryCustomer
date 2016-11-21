package io.fusionbit.vcarrycustomer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class ActivitySplash extends VCarryActivity
{

    private static final String TAG = App.APP_TAG + ActivitySplash.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                Intent i = new Intent(ActivitySplash.this, ActivityLogin.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        }, 1200);

    }
}

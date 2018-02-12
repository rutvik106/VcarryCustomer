package io.fusionbit.vcarrycustomer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class ActivitySplash extends AppCompatActivity {

    private static final String TAG = App.APP_TAG + ActivitySplash.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i;
                if (!Constants.IS_EMULATOR) {
                    i = new Intent(ActivitySplash.this, ActivityPhoneAuth.class);
                } else {
                    i = new Intent(ActivitySplash.this, ActivityHome.class);
                }
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        }, 1200);

    }

    @Override
    public void onBackPressed() {
    }

}

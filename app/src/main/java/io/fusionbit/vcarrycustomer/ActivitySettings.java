package io.fusionbit.vcarrycustomer;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.AppCompatSpinner;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import extra.LocaleHelper;
import extra.Log;

public class ActivitySettings extends BaseActivity {
    private static final String TAG = App.APP_TAG + ActivitySettings.class.getSimpleName();

    AppCompatSpinner spinSelectLanguage;

    List<String> languageList;

    boolean isChanged = false;

    int currentSelected;
    @BindView(R.id.tv_deviceToken)
    TextView tvDeviceToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.action_settings));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        LocaleHelper.onCreate(this, LocaleHelper.getLanguage(this));

        spinSelectLanguage = (AppCompatSpinner) findViewById(R.id.spin_selectLanguage);

        languageList = new ArrayList<>();
        languageList.add("English");
        languageList.add(getResources().getString(R.string.gujarati));


        spinSelectLanguage
                .setAdapter(new ArrayAdapter<String>(this,
                        android.R.layout.simple_list_item_1, languageList));

        final String language = LocaleHelper.getLanguage(this);

        Log.i(TAG, "LANGUAGE: " + language);

        if (language.equals("en")) {
            spinSelectLanguage.setSelection(0);
            currentSelected = 0;
        } else if (language.equals("gu")) {
            spinSelectLanguage.setSelection(1);
            currentSelected = 1;
        }

        spinSelectLanguage.post(new Runnable() {
            @Override
            public void run() {
                spinSelectLanguage.setOnItemSelectedListener(new ChangeLanguage());
            }
        });


        tvDeviceToken.setText(PreferenceManager.getDefaultSharedPreferences(this)
                .getString(Constants.FCM_INSTANCE_ID, "Not Found"));
        tvDeviceToken.setTextIsSelectable(true);


    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_settings;
    }

    @Override
    protected void internetNotAvailable() {

    }

    @Override
    protected void internetAvailable() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        Intent output = new Intent();
        output.putExtra(Constants.WAS_LANGUAGE_CHANGED, isChanged);
        setResult(RESULT_OK, output);
        super.finish();
    }

    public void selected(int selection) {
        if ((currentSelected - selection) != 0) {
            isChanged = true;
        }
    }

    class ChangeLanguage implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            switch (adapterView.getSelectedItemPosition()) {

                //English
                case 0:
                    LocaleHelper.setLocale(ActivitySettings.this, "en");
                    //ActivitySettings.this.recreate();
                    selected(0);
                    break;


                //Gujarati
                case 1:
                    LocaleHelper.setLocale(ActivitySettings.this, "gu");
                    //ActivitySettings.this.recreate();
                    selected(1);
                    break;

            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

}

package io.fusionbit.vcarrycustomer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import extra.LocaleHelper;

public class ActivitySettings extends VCarryActivity
{

    AppCompatSpinner spinSelectLanguage;

    List<String> languageList;

    boolean isChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setTitle(getResources().getString(R.string.action_settings));
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


        if (language.equals("en"))
        {
            spinSelectLanguage.setSelection(0);
        } else if (language.equals("gu"))
        {
            spinSelectLanguage.setSelection(1);
        }

        spinSelectLanguage.post(new Runnable()
        {
            @Override
            public void run()
            {
                spinSelectLanguage.setOnItemSelectedListener(new ChangeLanguage());
            }
        });


    }


    class ChangeLanguage implements AdapterView.OnItemSelectedListener
    {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
        {
            isChanged = true;
            switch (adapterView.getSelectedItemPosition())
            {

                //English
                case 0:
                    LocaleHelper.setLocale(ActivitySettings.this, "en");
                    //ActivitySettings.this.recreate();
                    break;


                //Gujarati
                case 1:
                    LocaleHelper.setLocale(ActivitySettings.this, "gu");
                    //ActivitySettings.this.recreate();
                    break;

            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView)
        {

        }
    }


    @Override
    public void finish()
    {
        Intent output = new Intent();
        output.putExtra(Constants.WAS_LANGUAGE_CHANGED, isChanged);
        setResult(RESULT_OK, output);
        super.finish();
    }
}

package fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

import apimodels.CountryCodes;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.fusionbit.vcarrycustomer.App;
import io.fusionbit.vcarrycustomer.R;

/**
 * Created by rutvik on 6/15/2017 at 12:04 PM.
 */

public class PhoneAuthScreenOne extends Fragment {
    private static final String TAG = App.APP_TAG + PhoneAuthScreenOne.class.getSimpleName();
    @BindView(R.id.fabConfirmMobile)
    FloatingActionButton fabConfirmMobile;
    @BindView(R.id.et_phoneNumber)
    EditText etPhoneNumber;
    Unbinder unbinder;

    PhoneAuthScreenOneCallbacks phoneAuthScreenOneCallbacks;
    @BindView(R.id.spinner)
    Spinner spinner;

    private String selectedCountryCode="+91";

    public static PhoneAuthScreenOne newInstance(PhoneAuthScreenOneCallbacks phoneAuthScreenOneCallbacks) {
        PhoneAuthScreenOne fragment = new PhoneAuthScreenOne();
        fragment.phoneAuthScreenOneCallbacks = phoneAuthScreenOneCallbacks;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_phone_auth_screen_one, container, false);

        unbinder = ButterKnife.bind(this, rootView);

        parseCountryCodes();

        spinner.setVisibility(View.GONE);

        return rootView;
    }

    private void parseCountryCodes() {
        InputStream ims = getActivity().getResources().openRawResource(R.raw.country_codes);

        Gson gson = new Gson();
        Reader reader = new InputStreamReader(ims);
        CountryCodes[] gsonObj = gson.fromJson(reader, CountryCodes[].class);

        setSpinner(gsonObj);

    }

    private void setSpinner(final CountryCodes[] countryCodes) {
        final ArrayList<String> countryCodesString = new ArrayList<>();
        for (CountryCodes c : countryCodes) {
            countryCodesString.add(c.getName() + " (" + c.getDialCode() + ")");
        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, countryCodesString);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCountryCode = countryCodes[i].getDialCode();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner.setSelection(87);

    }

    public boolean validatePhoneNumber() {
        String phoneNumber = etPhoneNumber.getText().toString();
        if (TextUtils.isEmpty(phoneNumber) || phoneNumber.length() > 10) {
            etPhoneNumber.setError("Invalid phone number.");
            return false;
        }

        return true;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.fabConfirmMobile)
    public void onViewClicked() {
        if (!validatePhoneNumber()) {
            return;
        }
        phoneAuthScreenOneCallbacks.verifyPhoneNumber(getMobileNo());
    }

    public String getMobileNo() {
        return getSelectedCountryCode() + etPhoneNumber.getText().toString();
    }

    public String getSelectedCountryCode() {
        return selectedCountryCode;
    }

    public interface PhoneAuthScreenOneCallbacks {
        void verifyPhoneNumber(String phoneNo);
    }
}

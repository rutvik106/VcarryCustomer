package fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.fusionbit.vcarrycustomer.R;

/**
 * Created by rutvik on 6/15/2017 at 12:04 PM.
 */

public class PhoneAuthScreenOne extends Fragment
{
    @BindView(R.id.fabConfirmMobile)
    FloatingActionButton fabConfirmMobile;
    @BindView(R.id.et_phoneNumber)
    EditText etPhoneNumber;
    Unbinder unbinder;

    PhoneAuthScreenOneCallbacks phoneAuthScreenOneCallbacks;

    public static PhoneAuthScreenOne newInstance(PhoneAuthScreenOneCallbacks phoneAuthScreenOneCallbacks)
    {
        PhoneAuthScreenOne fragment = new PhoneAuthScreenOne();
        fragment.phoneAuthScreenOneCallbacks = phoneAuthScreenOneCallbacks;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_phone_auth_screen_one, container, false);

        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    public boolean validatePhoneNumber()
    {
        String phoneNumber = etPhoneNumber.getText().toString();
        if (TextUtils.isEmpty(phoneNumber) || phoneNumber.length() > 10)
        {
            etPhoneNumber.setError("Invalid phone number.");
            return false;
        }

        return true;
    }


    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.fabConfirmMobile)
    public void onViewClicked()
    {
        if (!validatePhoneNumber())
        {
            return;
        }
        phoneAuthScreenOneCallbacks.verifyPhoneNumber(etPhoneNumber.getText().toString());
    }

    public String getMobileNo()
    {
        return etPhoneNumber.getText().toString();
    }

    public interface PhoneAuthScreenOneCallbacks
    {
        void verifyPhoneNumber(String phoneNo);
    }
}

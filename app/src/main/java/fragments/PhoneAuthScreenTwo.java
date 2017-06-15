package fragments;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.fusionbit.vcarrycustomer.R;

/**
 * Created by rutvik on 6/15/2017 at 12:15 PM.
 */

public class PhoneAuthScreenTwo extends Fragment
{
    @BindView(R.id.fabConfirmOtp)
    FloatingActionButton fabConfirmOtp;
    @BindView(R.id.et_otp)
    EditText etOtp;
    @BindView(R.id.tv_otpWaitingText)
    TextView tvOtpWaitingText;
    @BindView(R.id.btn_resendOtp)
    Button btnResendOtp;
    Unbinder unbinder;

    PhoneAuthScreenTwoCallbacks phoneAuthScreenTwoCallbacks;

    CountDownTimer timer;

    public static PhoneAuthScreenTwo newInstance(PhoneAuthScreenTwoCallbacks phoneAuthScreenTwoCallbacks)
    {
        PhoneAuthScreenTwo fragment = new PhoneAuthScreenTwo();
        fragment = new PhoneAuthScreenTwo();
        fragment.phoneAuthScreenTwoCallbacks = phoneAuthScreenTwoCallbacks;
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_phone_auth_screen_two, container, false);

        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }


    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.fabConfirmOtp)
    public void onFabConfirmOtpClicked()
    {
        String code = etOtp.getText().toString();
        if (TextUtils.isEmpty(code))
        {
            etOtp.setError("Cannot be empty.");
            return;
        }
        phoneAuthScreenTwoCallbacks.verifyNumberWithOtp(etOtp.getText().toString());
    }

    public void startTimer()
    {
        btnResendOtp.setVisibility(View.GONE);

        timer = new CountDownTimer(60000, 1000)
        {

            public void onTick(long millisUntilFinished)
            {
                tvOtpWaitingText.setText("Please wait, You will receive a OTP in " + millisUntilFinished / 1000 + " Seconds.");
            }

            public void onFinish()
            {
                tvOtpWaitingText.setText("If you have not received any OTP. Please try resending.");
                btnResendOtp.setVisibility(View.VISIBLE);
            }
        }.start();
    }

    public void stopTimer()
    {
        if (timer != null)
        {
            timer.cancel();
            timer = null;
        }
    }

    @OnClick(R.id.btn_resendOtp)
    public void onBtnResendOtpClicked()
    {
        phoneAuthScreenTwoCallbacks.resendOtp();
    }

    public interface PhoneAuthScreenTwoCallbacks
    {
        void verifyNumberWithOtp(String otp);

        void resendOtp();
    }

}

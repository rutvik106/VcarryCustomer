package io.fusionbit.vcarrycustomer;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import fragments.PhoneAuthScreenOne;
import fragments.PhoneAuthScreenTwo;

public class ActivityPhoneAuth extends FragmentActivity implements PermissionListener, PhoneAuthScreenTwo.PhoneAuthScreenTwoCallbacks,
        PhoneAuthScreenOne.PhoneAuthScreenOneCallbacks
{

    private static final String TAG = App.APP_TAG + ActivityPhoneAuth.class.getSimpleName();
    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";
    Intent intent;
    @BindView(R.id.pager)
    ViewPager mPager;
    List<Fragment> phoneAuthFragments = new ArrayList<>();
    PhoneAuthScreenOne phoneAuthScreenOne;
    PhoneAuthScreenTwo phoneAuthScreenTwo;
    ProgressDialog pd;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    // [END declare_auth]
    // [START declare_auth]
    private FirebaseAuth mAuth;
    private PagerAdapter mPagerAdapter;
    private boolean mVerificationInProgress;
    private String mVerificationId;
    final PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks()
            {
                @Override
                public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential)
                {
                    Log.d(TAG, "onVerificationCompleted:" + phoneAuthCredential);
                    mVerificationInProgress = false;
                    Toast.makeText(ActivityPhoneAuth.this, "Verification Completed", Toast.LENGTH_SHORT).show();
                    signInWithPhoneAuthCredential(phoneAuthCredential);
                }

                @Override
                public void onVerificationFailed(FirebaseException e)
                {
                    Log.w(TAG, "onVerificationFailed", e);
                    mVerificationInProgress = false;
                    if (pd != null)
                    {
                        if (pd.isShowing())
                        {
                            pd.dismiss();
                        }
                    }
                    if (e instanceof FirebaseAuthInvalidCredentialsException)
                    {
                        Toast.makeText(ActivityPhoneAuth.this, "INVALID REQUEST", Toast.LENGTH_SHORT).show();
                        // Invalid request
                        // ...
                    } else if (e instanceof FirebaseTooManyRequestsException)
                    {
                        Toast.makeText(ActivityPhoneAuth.this,
                                "The SMS quota for the project has been exceeded", Toast.LENGTH_SHORT).show();
                        // The SMS quota for the project has been exceeded
                        // ...
                    }

                    // Show a message and update the UI
                    // ...
                }

                @Override
                public void onCodeSent(String verificationId,
                                       PhoneAuthProvider.ForceResendingToken token)
                {
                    if (pd != null)
                    {
                        if (pd.isShowing())
                        {
                            pd.dismiss();
                        }
                    }
                    mPager.setCurrentItem(1);
                    phoneAuthScreenTwo.startTimer();
                    // The SMS verification code has been sent to the provided phone number, we
                    // now need to ask the user to enter the code and then construct a credential
                    // by combining the code with a verification ID.
                    Log.d(TAG, "onCodeSent:" + verificationId);

                    // Save verification ID and resending token so we can use them later
                    mVerificationId = verificationId;
                    mResendToken = token;

                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_phone_auth);

        ButterKnife.bind(this);

        setTitle("V Carry");

        intent = new Intent(this, ActivityHome.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        initializeFragments();

        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());

        mPager.setAdapter(mPagerAdapter);

        mPager.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                return true;
            }
        });


        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        checkForPermissions();

        //etOtp.setVisibility(View.GONE);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_VERIFY_IN_PROGRESS, mVerificationInProgress);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        mVerificationInProgress = savedInstanceState.getBoolean(KEY_VERIFY_IN_PROGRESS);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        // [START_EXCLUDE]
        if (mVerificationInProgress && phoneAuthScreenOne.validatePhoneNumber())
        {
            phoneAuthScreenOne.onViewClicked();
        }
        // [END_EXCLUDE]
    }

    private void initializeFragments()
    {
        phoneAuthScreenOne = PhoneAuthScreenOne.newInstance(this);
        phoneAuthScreenTwo = PhoneAuthScreenTwo.newInstance(this);

        phoneAuthFragments.add(phoneAuthScreenOne);
        phoneAuthFragments.add(phoneAuthScreenTwo);

    }

    private void checkForPermissions()
    {
        TedPermission.with(this)
                .setPermissionListener(this)
                .setDeniedMessage("If you reject any permission, you can not use this App\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET,
                        Manifest.permission.SYSTEM_ALERT_WINDOW,
                        Manifest.permission.VIBRATE,
                        Manifest.permission.WAKE_LOCK,
                        Manifest.permission.DISABLE_KEYGUARD,
                        Manifest.permission.RECEIVE_BOOT_COMPLETED)
                .check();
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential)
    {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            phoneAuthScreenTwo.stopTimer();
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();

                            startActivity(intent);

                            // ...
                        } else
                        {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException)
                            {
                                // The verification code entered was invalid
                                if (pd != null)
                                {
                                    if (pd.isShowing())
                                    {
                                        pd.dismiss();
                                    }
                                }
                                Toast.makeText(ActivityPhoneAuth.this, "Cannot verify. Try again after sometime.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }


    @Override
    public void onPermissionGranted()
    {
        if (mAuth.getCurrentUser() != null)
        {
            startActivity(intent);
        }
    }

    @Override
    public void onPermissionDenied(ArrayList<String> deniedPermissions)
    {
        finish();
    }

    @Override
    public void verifyNumberWithOtp(String otp)
    {
        if (mVerificationId == null)
        {
            Toast.makeText(this, R.string.code_not_sent, Toast.LENGTH_SHORT).show();
            return;
        }
        verifyPhoneNumberWithCode(mVerificationId, otp);
    }

    @Override
    public void resendOtp()
    {
        phoneAuthScreenTwo.startTimer();
        resendVerificationCode(phoneAuthScreenOne.getMobileNo(), mResendToken);
    }

    @Override
    public void verifyPhoneNumber(String phoneNo)
    {
        startPhoneNumberVerification(phoneNo);
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code)
    {
        pd = ProgressDialog.show(this, getString(R.string.please_wait), getString(R.string.verifying_otp), true, false);
        pd.setCanceledOnTouchOutside(false);
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);
    }

    // [START resend_verification]
    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token)
    {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }

    public void startPhoneNumberVerification(String phoneNumber)
    {
        pd = ProgressDialog.show(this, getString(R.string.please_wait), getString(R.string.verify_phone), true, false);
        pd.setCanceledOnTouchOutside(false);
        // [START start_phone_auth]
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        // [END start_phone_auth]

        mVerificationInProgress = true;
    }
    // [END resend_verification]

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter
    {
        public ScreenSlidePagerAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        {
            return phoneAuthFragments.get(position);
        }

        @Override
        public int getCount()
        {
            return phoneAuthFragments.size();
        }
    }

}

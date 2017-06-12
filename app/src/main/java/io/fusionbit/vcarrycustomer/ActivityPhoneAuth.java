package io.fusionbit.vcarrycustomer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;

public class ActivityPhoneAuth extends BaseActivity implements PermissionListener
{

    private static final String TAG = App.APP_TAG + ActivityPhoneAuth.class.getSimpleName();
    @BindView(R.id.et_phoneNumber)
    EditText etPhoneNumber;
    @BindView(R.id.fabVerify)
    FloatingActionButton fabVerify;
    @BindView(R.id.et_otp)
    EditText etOtp;
    Intent intent;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    // [END declare_auth]
    // [START declare_auth]
    private FirebaseAuth mAuth;
    final PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks()
            {
                @Override
                public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential)
                {
                    Log.d(TAG, "onVerificationCompleted:" + phoneAuthCredential);
                    Toast.makeText(ActivityPhoneAuth.this, "Verification Completed", Toast.LENGTH_SHORT).show();
                    signInWithPhoneAuthCredential(phoneAuthCredential);
                }

                @Override
                public void onVerificationFailed(FirebaseException e)
                {
                    Log.w(TAG, "onVerificationFailed", e);

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
                    // The SMS verification code has been sent to the provided phone number, we
                    // now need to ask the user to enter the code and then construct a credential
                    // by combining the code with a verification ID.
                    Log.d(TAG, "onCodeSent:" + verificationId);

                    // Save verification ID and resending token so we can use them later
                    mVerificationId = verificationId;
                    mResendToken = token;

                }
            };
    private boolean mVerificationInProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        intent = new Intent(this, ActivityHome.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);


        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setTitle("Phone Verification");
        }


        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        checkForPermissions();

        etOtp.setVisibility(View.GONE);

    }

    private void checkForPermissions()
    {
        new TedPermission(this)
                .setPermissionListener(this)
                .setDeniedMessage("If you reject any permission, you can not use this App\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.INTERNET,
                        android.Manifest.permission.SYSTEM_ALERT_WINDOW,
                        android.Manifest.permission.VIBRATE,
                        android.Manifest.permission.WAKE_LOCK,
                        android.Manifest.permission.DISABLE_KEYGUARD,
                        android.Manifest.permission.RECEIVE_BOOT_COMPLETED)
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
                            }
                        }
                    }
                });
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code)
    {
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
    // [END resend_verification]

    private void startPhoneNumberVerification(String phoneNumber)
    {
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

    private boolean validatePhoneNumber()
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
    protected int getLayoutResourceId()
    {
        return R.layout.activity_phone_auth;
    }

    @Override
    protected void internetNotAvailable()
    {

    }

    @Override
    protected void internetAvailable()
    {

    }


    @OnClick(R.id.fabVerify)
    public void onViewClicked()
    {
        if (!validatePhoneNumber())
        {
            return;
        }

        if (etOtp.getText().length() > 0)
        {
            String code = etOtp.getText().toString();
            if (TextUtils.isEmpty(code))
            {
                etOtp.setError("Cannot be empty.");
                return;
            }
            verifyPhoneNumberWithCode(mVerificationId, etOtp.getText().toString());
        } else
        {
            etPhoneNumber.setEnabled(false);

            etOtp.setVisibility(View.VISIBLE);

            startPhoneNumberVerification(etPhoneNumber.getText().toString());
        }

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
}

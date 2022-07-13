package lv.ieatinc.ieat.fragments;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import lv.ieatinc.ieat.Constants;
import lv.ieatinc.ieat.R;
import lv.ieatinc.ieat.utilities.Timer;

public class ForgotPasswordFragment extends Fragment {
    public final String TAG = "FORGOT PASSWORD FRAGMENT";
    private Timer timer;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    public ForgotPasswordFragment() {
        super(R.layout.forgot_password_fragment);

        mAuth = FirebaseAuth.getInstance();

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                Log.d(TAG, "onVerificationCompleted:" + phoneAuthCredential);
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.w(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Log.e(TAG, "Invalid request");
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Log.e(TAG, "SMS quota has been exceeded");
                }
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                Log.d(TAG, "onCodeSent:" + verificationId);

                mVerificationId = verificationId;
                mResendToken = token;
            }
        };
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        ImageView back_arrow = view.findViewById(R.id.fp_back_arrow);
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Does the same as Back button, goes back to previous fragment, destroying current
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        Button change_password = view.findViewById(R.id.fp_change_password_button);
        change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView phone_number = getView().findViewById(R.id.fp_phone_number_input);
                TextView phone_code = getView().findViewById(R.id.fp_phone_code_input);
                TextView new_password = getView().findViewById(R.id.fp_new_password_input);

                String phone_number_input = phone_number.getText().toString().trim();
                String phone_code_input = phone_code.getText().toString().trim();
                String new_password_input = new_password.getText().toString();

                if (!phone_number_input.isEmpty()) {
                    if (!Patterns.PHONE.matcher(phone_code_input).matches()) {
                        phone_number.setError("Please enter a valid phone number");
                        phone_number.requestFocus();
                        return;
                    }
                } else {
                    phone_number.setError("Please enter the phone code!");
                    phone_number.requestFocus();
                    return;
                }

                if (phone_code_input.isEmpty()) {
                    phone_code.setError("Please enter the phone code!");
                    phone_code.requestFocus();
                    return;
                }

                if(!new_password_input.isEmpty()) {
                    if(new_password_input.length() < Constants.MIN_PASSWORD_LENGTH) {
                        new_password.setError("The password is too short!");
                        new_password.requestFocus();
                        return;
                    }
                } else {
                    new_password.setError("Please enter a new password!");
                    new_password.requestFocus();
                    return;
                }

                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, phone_code_input);

            }
        });

        TextView send_email = view.findViewById(R.id.fp_send_email_text);
        send_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (timer == null) {
                    timer = new Timer(getView().findViewById(R.id.fp_timer_time_text));
                    sendPhoneCode(((TextView) getView().findViewById(R.id.fp_phone_number_input)).getText().toString().trim());
                } else {
                    if (!timer.isCounting()) {
                        timer = new Timer(getView().findViewById(R.id.fp_timer_time_text));
                        sendPhoneCode(((TextView) getView().findViewById(R.id.fp_phone_number_input)).getText().toString().trim());
                    } else {
                        Toast.makeText(getContext(), "Please wait for the timer to end", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void sendPhoneCode(String phoneNumber) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(getActivity())                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener((Executor) this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            mAuth.signInWithCredential(credential);
                            FirebaseUser user = task.getResult().getUser();
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(getContext(), "Invalid phone code", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}

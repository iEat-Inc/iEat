package lv.ieatinc.ieat.fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.Map;

import lv.ieatinc.ieat.BaseActivity;
import lv.ieatinc.ieat.utilities.FirebaseDB;
import lv.ieatinc.ieat.R;

public class LoginFragment extends Fragment {
    public final String TAG = "LOGIN FRAGMENT";
    private final String server_client_Id = "93658957684-6h9j4k9u0c5gvrp2te7nlhrconct7nn5.apps.googleusercontent.com";
    private SignInClient oneTapClient;
    private BeginSignInRequest signInRequest;
    FirebaseAuth mAuth;

    private static final int REQ_ONE_TAP = 2;  // Can be any integer unique to the Activity.
    private boolean showOneTapUI = true;

    public LoginFragment() {
        super(R.layout.login_fragment);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FragmentActivity activity = getActivity();

        // Uncomment this when you want the app to skip login, since logged in users save on device
        // This way you have to log in only once
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getActivity(), BaseActivity.class);
            startActivity(intent);
            getActivity().finish();
        }

        Button login = view.findViewById(R.id.login_login_button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView email_input = getView().findViewById(R.id.login_email_input);
                TextView password_input = getView().findViewById(R.id.login_password_input);

                String email = email_input.getText().toString().trim();
                String password = password_input.getText().toString().trim();

                if (!email.isEmpty()) {
                    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        email_input.setError("Not a valid email address");
                        email_input.requestFocus();
                        return;
                    }
                } else {
                    email_input.setError("Please input your email");
                    email_input.requestFocus();
                    return;
                }

                if (password.isEmpty()) {
                    password_input.setError("Please input your password");
                    password_input.requestFocus();
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "signInWithEmail:success");
                                    Intent intent = new Intent(getActivity(), BaseActivity.class);
                                    startActivity(intent);
                                    getActivity().finish();
                                } else {
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(getContext(), "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        Button login_with_google = view.findViewById(R.id.login_login_google_button);
        login_with_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oneTapClient = Identity.getSignInClient(getActivity());
                signInRequest = BeginSignInRequest.builder()
                        .setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder()
                                .setSupported(true)
                                .build())
                        .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                                .setSupported(true)
                                .setServerClientId(server_client_Id)
                                .setFilterByAuthorizedAccounts(true)
                                .build())
                        .setAutoSelectEnabled(true)
                        .build();

                oneTapClient.beginSignIn(signInRequest)
                        .addOnSuccessListener(new OnSuccessListener<BeginSignInResult>() {
                            @Override
                            public void onSuccess(BeginSignInResult result) {
                                try {
                                    startIntentSenderForResult(
                                            result.getPendingIntent().getIntentSender(), REQ_ONE_TAP,
                                            null, 0, 0, 0, null);
                                } catch (IntentSender.SendIntentException e) {
                                    Log.e(TAG, "Couldn't start One Tap UI: " + e.getLocalizedMessage());
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "No account has been found.", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, e.getLocalizedMessage());
                            }
                        });
            }
        });

        TextView forgot_password = activity.findViewById(R.id.login_forgot_password_text);
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.getSupportFragmentManager() // getParentFragmentManager/getChildFragmentManager doesn't seem to work
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, ForgotPasswordFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack("userControlBackStack") // Use this to go back to previous fragment
                        .commit();
            }
        });

        TextView forgot_password_2 = activity.findViewById(R.id.login_forgot_password_text_2);
        forgot_password_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView email_input = getView().findViewById(R.id.login_email_input);
                String email = email_input.getText().toString().trim();

                if (!email.isEmpty()) {
                    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        email_input.setError("Not a valid email address");
                        email_input.requestFocus();
                        return;
                    }
                } else {
                    email_input.setError("Please input your email");
                    email_input.requestFocus();
                    return;
                }

                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getContext(), "Email sent", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}

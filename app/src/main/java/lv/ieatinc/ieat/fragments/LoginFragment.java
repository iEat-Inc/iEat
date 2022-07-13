package lv.ieatinc.ieat.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

import lv.ieatinc.ieat.utilities.FirebaseDB;
import lv.ieatinc.ieat.R;

public class LoginFragment extends Fragment {
    public final String TAG = "LOGIN FRAGMENT";
    private String server_client_Id = "";
    private SignInClient oneTapClient;
    private BeginSignInRequest signInRequest;

    public LoginFragment() {
        super(R.layout.login_fragment);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FragmentActivity activity = getActivity();

        FirebaseDB.readData(new FirebaseDB.FirestoreCallback() {
            @Override
            public void onCallback(Map<String, Object> data) {
                Log.i(TAG, String.valueOf(data));
            }
        }, db);

        // Not yet finished, will continue on 13.07
        Button login_with_google = view.findViewById(R.id.login_login_google_button);
        login_with_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oneTapClient = Identity.getSignInClient(activity);
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
    }
}

package lv.ieatinc.ieat.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import lv.ieatinc.ieat.BaseActivity;
import lv.ieatinc.ieat.Constants;
import lv.ieatinc.ieat.R;
import lv.ieatinc.ieat.utilities.FirebaseDB;


public class SignupFragment extends Fragment {
    public final String TAG = "SIGNUP FRAGMENT";
    EditText usernameEditText;
    EditText emailEditText;
    EditText passEditText;
    EditText confPassEditText;
    EditText phoneEditText;
    Button signupButton;
    private FirebaseAuth mAuth;

    public SignupFragment() {
        super(R.layout.signup_fragment);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "START");

        mAuth = FirebaseAuth.getInstance();

        ImageView back_arrow = view.findViewById(R.id.signup_back_arrow);
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Does the same as Back button, goes back to previous fragment, destroying current
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        signupButton = view.findViewById(R.id.Signup_button);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usernameEditText = getView().findViewById(R.id.Signup_UserName);
                emailEditText = getView().findViewById(R.id.Signup_Email);
                passEditText = getView().findViewById(R.id.Signup_Password);
                confPassEditText = getView().findViewById(R.id.Signup_ConfirmPassword);
                signupButton = getView().findViewById(R.id.Signup_button);
                phoneEditText = getView().findViewById(R.id.Signup_phone);

                final String username = usernameEditText.getText().toString().trim();
                final String email = emailEditText.getText().toString().trim();
                final String pass = passEditText.getText().toString().trim();
                final String confPass = confPassEditText.getText().toString().trim();
                final String phone = phoneEditText.getText().toString().trim();

                //TODO: Firebase username exists check
                if (!username.isEmpty()) {
                    if (username.length() < Constants.MIN_USERNAME_LENGTH) {
                        usernameEditText.setError("Username is too short");
                        usernameEditText.requestFocus();
                        return;
                    }
                } else {
                    usernameEditText.setError("Username is empty");
                    usernameEditText.requestFocus();
                    return;
                }

                if (!email.isEmpty()) {
                    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        emailEditText.setError("Please enter a valid email address");
                        emailEditText.requestFocus();
                        return;
                    }
                } else {
                    emailEditText.setError("Email is empty");
                    emailEditText.requestFocus();
                    return;
                }

                if (!pass.isEmpty()) {
                    if (pass.length() < Constants.MIN_PASSWORD_LENGTH) {
                        passEditText.setError("The password is too short!");
                        passEditText.requestFocus();
                        return;
                    }
                } else {
                    passEditText.setError("Password is empty");
                    passEditText.requestFocus();
                    return;
                }

                if (!confPass.isEmpty()) {
                    if (confPass.length() < Constants.MIN_PASSWORD_LENGTH) {
                        confPassEditText.setError("The password is too short!");
                        confPassEditText.requestFocus();
                        return;
                    }
                } else {
                    confPassEditText.setError("Password is empty");
                    confPassEditText.requestFocus();
                    return;
                }

                if (!phone.isEmpty()) {
                    if (!Patterns.PHONE.matcher(phone).matches()) {
                        phoneEditText.setError("Please enter a valid phone number");
                        phoneEditText.requestFocus();
                        return;
                    }
                } else {
                    phoneEditText.setError("Phone number is empty");
                    phoneEditText.requestFocus();
                    return;
                }

                if (!pass.equals(confPass)) {
                    passEditText.setError("Passwords do not match");
                    passEditText.requestFocus();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            Map<String, String> data = new HashMap<String, String>();
                            data.put("access", "Manager");

                            Log.d(TAG, "createUserWithEmail:success");

                            FirebaseUser user = mAuth.getCurrentUser();

                            FirebaseDB.addUser(new FirebaseDB.AddUserCallback() {
                                @Override
                                public void onComplete(Boolean status) {
                                    if(status) {
                                        Toast.makeText(getContext(), "User created!",
                                                Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getActivity(), BaseActivity.class);
                                        startActivity(intent);
                                        getActivity().finish();
                                    } else {
                                        user.delete()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        Toast.makeText(getContext(), "Unable to create user",
                                                                Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                    }
                                }
                            }, db, user.getUid(), data);
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getContext(), "This email already exists.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
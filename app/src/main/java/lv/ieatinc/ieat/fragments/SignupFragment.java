package lv.ieatinc.ieat.fragments;

import android.annotation.SuppressLint;
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
import com.google.firebase.firestore.auth.User;

import lv.ieatinc.ieat.Constants;
import lv.ieatinc.ieat.R;


public class SignupFragment extends Fragment {
    public final String TAG = "SIGNUP FRAGMENT";
    EditText emailEditText;
    EditText passEditText;
    EditText confPassEditText;
    Button signupButton;
    private FirebaseAuth mAuth;

    public SignupFragment() {
        super(R.layout.signup_fragment);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }

    private void reload() { }

    private void updateUI(FirebaseUser user) {

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

        Button createUser = view.findViewById(R.id.Signup_button);
        createUser.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                emailEditText = view.findViewById(R.id.Signup_Email);
                passEditText = view.findViewById(R.id.Signup_Password);
                confPassEditText = view.findViewById(R.id.Signup_ConfirmPassword);
                signupButton = view.findViewById(R.id.Signup_button);

                signupButton.setOnClickListener(new View.OnClickListener() {

                   final String email = emailEditText.getText().toString().trim();
                   final String pass = passEditText.getText().toString().trim();
                   final String confPass = confPassEditText.getText().toString().trim();


                    @Override
                    public void onClick(View v) {
                        if (email.isEmpty()) {
                            emailEditText.setError("Email is empty");
                            emailEditText.requestFocus();
                            return;
                        }

                        if (!pass.isEmpty()) {
                            if (pass.length() < Constants.MIN_PASSWORD_LENGTH) {
                                emailEditText.setError("The password is too short!");
                                emailEditText.requestFocus();
                                return;
                            }
                        } else {
                            emailEditText.setError("Please enter a new password!");
                            emailEditText.requestFocus();
                            return;
                        }
                        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                        {
                            emailEditText.setError("Enter the valid email address");
                            emailEditText.requestFocus();
                            return;
                        }

                        if (!pass.equals(confPass)) {
                            passEditText.setError("Passwords should match");
                            passEditText.requestFocus();
                            return;
                        }


                            mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(getContext(), "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    updateUI(null);
                                }
                            }
                            });
                        }
                });
            }
                });
    }
}
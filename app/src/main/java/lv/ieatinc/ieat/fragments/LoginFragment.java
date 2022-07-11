package lv.ieatinc.ieat.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

import lv.ieatinc.ieat.utilities.FirebaseDB;
import lv.ieatinc.ieat.R;

public class LoginFragment extends Fragment {
    public final String TAG = "LOGIN FRAGMENT";

    public LoginFragment() {
        super(R.layout.login_fragment);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        FirebaseDB.readData(new FirebaseDB.FirestoreCallback() {
            @Override
            public void onCallback(Map<String, Object> data) {
                Log.i(TAG, String.valueOf(data));
            }
        }, db);

        TextView forgot_password = getActivity().findViewById(R.id.forgot_password_text);
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity()
                        .getSupportFragmentManager() // getParentFragmentManager/getChildFragmentManager doesn't seem to work
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, ForgotPasswordFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack("userControlBackStack") // Use this to go back to previous fragment
                        .commit();
            }
        });
    }
}

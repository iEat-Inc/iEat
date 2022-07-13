package lv.ieatinc.ieat.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.View;

import lv.ieatinc.ieat.R;


public class SignupFragment extends Fragment {
    public final String TAG = "SIGNUP FRAGMENT";

    public SignupFragment() {
        super(R.layout.signup_fragment);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "START");
    }
}
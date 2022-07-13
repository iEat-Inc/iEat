package lv.ieatinc.ieat.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import lv.ieatinc.ieat.R;


public class SignupFragment extends Fragment {
    public final String TAG = "SIGNUP FRAGMENT";

    public SignupFragment() {
        super(R.layout.signup_fragment);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "START");

        ImageView back_arrow = view.findViewById(R.id.signup_back_arrow);
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Does the same as Back button, goes back to previous fragment, destroying current
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }
}
package lv.ieatinc.ieat.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

import lv.ieatinc.ieat.R;


public class NewRestaurantFragment extends Fragment {
    public final String TAG = "NEW RESTAURANT FRAGMENT";

    EditText restName;
    EditText restRegNumber;
    EditText restAddress;
    Button newRestButton;
    private FirebaseAuth mAuth;


    public NewRestaurantFragment() {
        super(R.layout.fragment_new_restaurant);

    }

    //TODO finish with firebase
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "START");

        mAuth = FirebaseAuth.getInstance();

        ImageView back_arrow = view.findViewById(R.id.new_rest_back_arrow);
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Does the same as Back button, goes back to previous fragment, destroying current
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        newRestButton = view.findViewById(R.id.Signup_button);
        newRestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restName = getView().findViewById(R.id.NewRestaurant_rest_name);
                restRegNumber = getView().findViewById(R.id.NewRestaurant_reg_number);
                restAddress = getView().findViewById(R.id.NewRestaurant_address);
                newRestButton = getView().findViewById(R.id.NewRestaurant_button);

                final String name = restName.getText().toString().trim();
                final String regNumber = restRegNumber.getText().toString().trim();
                final String address = restAddress.getText().toString().trim();

                if (name.isEmpty()){
                    restName.setError("Restaurant name is empty");
                    restName.requestFocus();
                    return;
                }

                if (!regNumber.isEmpty()){
                    if(regNumber.length()  < 11){
                        restRegNumber.setError("Registration number is too short");
                        restRegNumber.requestFocus();
                        return;
                    }
                }
                else {
                    restRegNumber.setError("Registration number is empty");
                    restRegNumber.requestFocus();
                    return;
                }
                if (address.isEmpty()){
                    restAddress.setError("Restaurant name is empty");
                    restAddress.requestFocus();
                    return;
                }

            }
        });
    }
}
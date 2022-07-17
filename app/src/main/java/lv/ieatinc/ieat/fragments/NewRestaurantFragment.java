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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.invoke.ConstantCallSite;
import java.util.HashMap;
import java.util.Map;

import lv.ieatinc.ieat.Constants;
import lv.ieatinc.ieat.R;
import lv.ieatinc.ieat.utilities.FirebaseDB;


public class NewRestaurantFragment extends Fragment {
    public final String TAG = "NEW RESTAURANT FRAGMENT";

    EditText restName;
    EditText restRegNumber;
    EditText restAddress;
    Button newRestButton;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;


    public NewRestaurantFragment() {
        super(R.layout.fragment_new_restaurant);
    }

    //TODO: finish with firebase
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "START");
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        ImageView back_arrow = getActivity().findViewById(R.id.new_rest_back_arrow);
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Does the same as Back button, goes back to previous fragment, destroying current
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        newRestButton = getActivity().findViewById(R.id.NewRestaurant_button);
        newRestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restName = getView().findViewById(R.id.NewRestaurant_rest_name);
                restRegNumber = getView().findViewById(R.id.NewRestaurant_reg_number);
                restAddress = getView().findViewById(R.id.NewRestaurant_address);

                final String name = restName.getText().toString().trim();
                final String regNumber = restRegNumber.getText().toString().trim();
                final String address = restAddress.getText().toString().trim();

                if (name.isEmpty()){
                    restName.setError("Restaurant name is empty");
                    restName.requestFocus();
                    return;
                }

                if (!regNumber.isEmpty()){
                    if(regNumber.length()  < Constants.MIN_REG_NUMBER_LENGTH){
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

                // Creating a map with data to pass to db
                Map<String, String> data = new HashMap<String, String>();
                data.put("Name", name);
                data.put("RegNumber", regNumber);
                data.put("Address", address);

                FirebaseDB.addRestaurant(new FirebaseDB.AddRestaurantCallback() {
                    @Override
                    public void onComplete(Boolean status) {
                        if(status) {
                            getActivity().getSupportFragmentManager().popBackStack();
                        } else {
                            Toast.makeText(getContext(), "Failed to create a restaurant",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }, db, data);
            }
        });
    }
}
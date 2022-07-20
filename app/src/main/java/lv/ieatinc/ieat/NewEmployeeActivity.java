package lv.ieatinc.ieat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.HashMap;

import lv.ieatinc.ieat.fragments.HomeFragment;
import lv.ieatinc.ieat.utilities.FirebaseDB;

public class NewEmployeeActivity extends AppCompatActivity {

    private static final String TAG = "NEW EMPLOYEE ACTIVITY";
    ImageView backArrow;
    EditText username_input;
    EditText email_input;
    EditText phone_input;
    EditText pass_input;
    EditText conf_pass_input;
    Spinner employee_type_spinner;
    Button add_employee_button;

    FirebaseFirestore db;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String restaurantId = intent.getStringExtra(RestaurantActivity.EXTRA_EMPLOYEE_DATA);

        getSupportActionBar().hide();
        setContentView(R.layout.activity_new_employee);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        backArrow = findViewById(R.id.new_employee_back_arrow);
        username_input = findViewById(R.id.new_employee_username_input);
        email_input = findViewById(R.id.new_employee_email);
        phone_input = findViewById(R.id.new_employee_phone);
        pass_input = findViewById(R.id.new_employee_password);
        conf_pass_input = findViewById(R.id.new_employee_conf_password);
        employee_type_spinner = findViewById(R.id.new_employee_spinner_types);
        add_employee_button = findViewById(R.id.new_employee_add_button);

        add_employee_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = username_input.getText().toString();
                String email = email_input.getText().toString().trim();
                String phone_num = phone_input.getText().toString();
                String password = pass_input.getText().toString();
                String conf_password = conf_pass_input.getText().toString();
                String type = employee_type_spinner.getSelectedItem().toString();

                if(!username.isEmpty()) {
                    if(username.length() < Constants.MIN_USERNAME_LENGTH) {
                        username_input.setError("Username is too short");
                        username_input.requestFocus();
                        return;
                    }
                } else {
                    username_input.setError("Username is empty");
                    username_input.requestFocus();
                    return;
                }

                if(!email.isEmpty()) {
                    if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        email_input.setError("Please enter a valid email address");
                        email_input.requestFocus();
                        return;
                    }
                } else {
                    email_input.setError("Email is empty");
                    email_input.requestFocus();
                    return;
                }

                if(!phone_num.isEmpty()) {
                    if(!Patterns.PHONE.matcher(phone_num).matches()) {
                        phone_input.setError("Please enter a valid phone number");
                        phone_input.requestFocus();
                        return;
                    }
                } else {
                    phone_input.setError("Phone number is empty");
                    phone_input.requestFocus();
                    return;
                }

                if(!password.isEmpty()) {
                    if(password.length() < Constants.MIN_PASSWORD_LENGTH) {
                        pass_input.setError("Password is too short");
                        pass_input.requestFocus();
                        return;
                    }
                } else {
                    pass_input.setError("Password is empty");
                    pass_input.requestFocus();
                    return;
                }

                if(!conf_password.isEmpty()) {
                    if(!conf_password.equals(password)) {
                        conf_pass_input.setError("Passwords do not match");
                        conf_pass_input.requestFocus();
                        return;
                    }
                } else {
                    conf_pass_input.setError("Password is empty");
                    conf_pass_input.requestFocus();
                    return;
                }

                HashMap<String, String> new_data = new HashMap<String, String>();

                new_data.put("access", type);
                auth.getCurrentUser();
                // TODO: User gets signed out when creating new account
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseDB.addUser(new FirebaseDB.AddUserCallback() {
                                @Override
                                public void onComplete(Boolean status) {
                                    if(status) {
                                        Toast.makeText(getBaseContext(), "User created!",
                                                Toast.LENGTH_SHORT).show();
                                        // TODO: Add this user to a restaurant
                                    }
                                }
                            }, db, auth.getCurrentUser().getUid(), new_data);
                            finish();
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getBaseContext(), "This email already exists.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        backArrow.setOnClickListener(v -> finish());

        Spinner employeeTypes = findViewById(R.id.new_employee_spinner_types);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this, R.array.employee_types, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        employeeTypes.setAdapter(adapter);
    }
}
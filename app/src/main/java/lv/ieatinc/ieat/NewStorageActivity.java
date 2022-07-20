package lv.ieatinc.ieat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import lv.ieatinc.ieat.utilities.FirebaseDB;

public class NewStorageActivity extends AppCompatActivity {

    private static final String TAG = "NEW STORAGE ACTIVITY";
    ImageView backArrow;
    EditText storage_name_input;
    EditText storage_tag_input;
    EditText storage_size_input;
    Button add_storage_button;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String restaurantId = intent.getStringExtra(RestaurantActivity.EXTRA_STORAGE_DATA);

        getSupportActionBar().hide();
        setContentView(R.layout.new_storage_activity);

        db = FirebaseFirestore.getInstance();

        backArrow = findViewById(R.id.new_storage_back_arrow);
        storage_name_input = findViewById(R.id.new_storage_name_input);
        storage_tag_input = findViewById(R.id.new_storage_tag_input);
        storage_size_input = findViewById(R.id.new_storage_size_input);
        add_storage_button = findViewById(R.id.new_employee_add_button);

        add_storage_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String storage_name = storage_name_input.getText().toString();
                String storage_tag = storage_tag_input.getText().toString();
                String storage_size = storage_size_input.getText().toString();

                if(storage_name.isEmpty()) {
                    storage_name_input.setError("Name is empty");
                    storage_name_input.requestFocus();
                    return;
                }

                if(storage_tag.isEmpty()) {
                    storage_tag_input.setError("Tag is empty");
                    storage_tag_input.requestFocus();
                    return;
                }

                if(storage_size_input.getText().toString().isEmpty()) {
                    storage_size_input.setError("Size is empty");
                    storage_size_input.requestFocus();
                    return;
                } else {
                    if (Integer.parseInt(storage_size) <= 0) {
                        storage_size_input.setError("Size is too small");
                        storage_size_input.requestFocus();
                        return;
                    }
                }

                HashMap<String, String> new_data = new HashMap<String, String>();

                new_data.put("Name", storage_name);
                new_data.put("Tag", storage_tag);
                new_data.put("Size", storage_size);

                FirebaseDB.addStorage(new FirebaseDB.AddStorageCallback() {
                    @Override
                    public void onComplete(Boolean status) {
                        if(status) {
                            Toast.makeText(getBaseContext(), "Storage created", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(getBaseContext(), "Failed to create a new storage", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, db, restaurantId, new_data);
            }
        });

        backArrow.setOnClickListener(v -> finish());

    }


}
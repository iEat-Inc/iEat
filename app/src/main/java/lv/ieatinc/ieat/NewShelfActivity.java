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

import io.grpc.Context;
import lv.ieatinc.ieat.utilities.FirebaseDB;

public class NewShelfActivity extends AppCompatActivity {

    ImageView backArrow;
    EditText name_input;
    EditText tag_input;
    EditText size_input;
    Button add_shelf;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String restaurantId = intent.getStringExtra(StorageActivity.EXTRA_SHELF_DATA);
        String storageId = intent.getStringExtra(StorageActivity.EXTRA_SHELF_DATA2);

        getSupportActionBar().hide();
        setContentView(R.layout.activity_new_shelf);

        db = FirebaseFirestore.getInstance();

        name_input = findViewById(R.id.new_shelf_name_input);
        tag_input = findViewById(R.id.new_shelf_tag_input);
        size_input = findViewById(R.id.new_shelf_size_input);
        add_shelf = findViewById(R.id.new_shelf_add_button);

        add_shelf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = name_input.getText().toString();
                String tag = tag_input.getText().toString();
                String size = size_input.getText().toString();

                if(name.isEmpty()) {
                    name_input.setError("Name is empty");
                    name_input.requestFocus();
                    return;
                }

                if(tag.isEmpty()) {
                    tag_input.setError("Tag is empty");
                    tag_input.requestFocus();
                    return;
                }

                if(!size.isEmpty()) {
                    if(Integer.parseInt(size) <= 0) {
                        size_input.setError("Size is too small");
                        size_input.requestFocus();
                        return;
                    }
                } else {
                    size_input.setError("Size is empty");
                    size_input.requestFocus();
                    return;
                }

                HashMap<String, String> new_data = new HashMap<String, String>();

                new_data.put("Name", name);
                new_data.put("Tag", tag);
                new_data.put("Size", size);

                FirebaseDB.addShelf(new FirebaseDB.AddShelfCallback() {
                    @Override
                    public void onComplete(Boolean status) {
                        if(status) {
                            Toast.makeText(getBaseContext(), "Shelf created", Toast.LENGTH_SHORT).show();
                            finish();
                            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                        } else {
                            Toast.makeText(getBaseContext(), "Failed to create a new shelf", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, db, restaurantId, storageId, new_data);
            }
        });

        backArrow = findViewById(R.id.new_shelf_back_arrow);
        backArrow.setOnClickListener(v -> {
            finish();
            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        });
    }
}
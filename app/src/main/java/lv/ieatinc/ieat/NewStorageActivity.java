package lv.ieatinc.ieat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

public class NewStorageActivity extends AppCompatActivity {

    ImageView backArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_storage_activity);
        backArrow = findViewById(R.id.new_storage_back_arrow);
        backArrow.setOnClickListener(v -> finish());
    }


}
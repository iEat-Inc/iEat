package lv.ieatinc.ieat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

public class NewShelfActivity extends AppCompatActivity {

    ImageView backArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_shelf);
        getSupportActionBar().hide();
        backArrow = findViewById(R.id.new_shelf_back_arrow);
        backArrow.setOnClickListener(v -> finish());
    }
}
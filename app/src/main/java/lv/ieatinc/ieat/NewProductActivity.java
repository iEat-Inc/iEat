package lv.ieatinc.ieat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

public class NewProductActivity extends AppCompatActivity {
    ImageView backArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);
        backArrow = findViewById(R.id.add_product_back_arrow);
        backArrow.setOnClickListener(v -> finish());

        Spinner employeeTypes = findViewById(R.id.add_product_spinner_types);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this, R.array.units_of_measurements, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        employeeTypes.setAdapter(adapter);
    }
}
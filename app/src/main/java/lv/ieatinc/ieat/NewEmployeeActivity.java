package lv.ieatinc.ieat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

public class NewEmployeeActivity extends AppCompatActivity {

    ImageView backArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_employee);
        backArrow = findViewById(R.id.new_employee_back_arrow);
        backArrow.setOnClickListener(v -> finish());

        Spinner employeeTypes = findViewById(R.id.new_employee_spinner_types);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this, R.array.employee_types, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        employeeTypes.setAdapter(adapter);
    }
}
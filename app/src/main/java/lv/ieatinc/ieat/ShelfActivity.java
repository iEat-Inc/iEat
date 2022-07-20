package lv.ieatinc.ieat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class ShelfActivity extends AppCompatActivity {
    ImageView back_arrow;
    Button addProduct;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelf);
        back_arrow = findViewById(R.id.shelf_back_arrow);
        back_arrow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), StorageActivity.class).
                        setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

            }
        });
        addProduct = findViewById(R.id.shelf_new_product);
        addProduct.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), NewProductActivity.class).
                        setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });


    }
}
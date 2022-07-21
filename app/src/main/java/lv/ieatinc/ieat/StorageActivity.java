package lv.ieatinc.ieat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;

import lv.ieatinc.ieat.adapters.StorageListAdapter;
import lv.ieatinc.ieat.fragments.NewRestaurantFragment;

public class StorageActivity extends AppCompatActivity implements StorageListAdapter.OnClickListener {
    public final String TAG = "STORAGE ACTIVITY";
    public static final String EXTRA_SHELF_DATA = "lv.ieatinc.ieat.EXTRA_SHELF_DATA1";
    public static final String EXTRA_SHELF_DATA2 = "lv.ieatinc.ieat.EXTRA_SHELF_DATA2";
    private LottieAnimationView loading;
    private RecyclerView restaurantsList;
    ImageView back_arrow;
    Button newShelf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent input_intent = getIntent();
        input_intent.getStringExtra(RestaurantActivity.EXTRA_EXISTING_STORAGE_DATA);

        getSupportActionBar().hide();
        setContentView(R.layout.activity_storage);

        back_arrow = findViewById(R.id.storage_back_arrow);
        back_arrow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });

        newShelf = findViewById(R.id.storage_new_shelf);
        newShelf.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewShelfActivity.class);
//                intent.putExtra(EXTRA_SHELF_DATA, restaurantId);
//                intent.putExtra(EXTRA_SHELF_DATA2, storageId);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });


    }

    @Override
    public void onClick(int position) {


    }
}
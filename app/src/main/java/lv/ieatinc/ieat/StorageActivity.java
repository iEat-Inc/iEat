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
    private LottieAnimationView loading;
    private RecyclerView restaurantsList;
    ImageView back_arrow;
    Button newShelf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);

        back_arrow = findViewById(R.id.storage_back_arrow);
        back_arrow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RestaurantActivity.class).
                        setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

            }
});
        newShelf = findViewById(R.id.storage_new_shelf);
        newShelf.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), NewShelfActivity.class).
                        setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });


    }

    @Override
    public void onClick(int position) {


    }
}
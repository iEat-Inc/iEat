package lv.ieatinc.ieat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import lv.ieatinc.ieat.adapters.RestaurantEmployeeListAdapter;
import lv.ieatinc.ieat.adapters.RestaurantListAdapter;
import lv.ieatinc.ieat.adapters.RestaurantStorageListAdapter;
import lv.ieatinc.ieat.fragments.HomeFragment;
import lv.ieatinc.ieat.utilities.FirebaseDB;

public class RestaurantActivity extends AppCompatActivity implements RestaurantStorageListAdapter.OnRestaurantStorageClickListener {
    public static final String TAG = "RESTAURANT ACTIVITY";
    public static final String EXTRA_STORAGE_DATA = "lv.ieatinc.ieat.STORAGE_DATA";
    public static final String EXTRA_EMPLOYEE_DATA = "lv.ieatinc.ieat.EMPLOYEE_DATA";
    private FirebaseFirestore db;
    RecyclerView storage_list;
    RecyclerView employee_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_restaurant);

        db = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        Serializable serializableExtra = intent.getSerializableExtra(HomeFragment.EXTRA_DATA);
        HashMap<String, String> data = (HashMap<String, String>) serializableExtra;

        TextView header = findViewById(R.id.restaurant_header);
        ImageView back_arrow = findViewById(R.id.restaurants_back_arrow);
        Button add_storage = findViewById(R.id.restaurant_add_storage_button);
        Button add_employee = findViewById(R.id.restaurant_add_employee_button);
        storage_list = findViewById(R.id.restaurant_storages_list);
        employee_list = findViewById(R.id.restaurant_employees_list);

        header.setText(data.get("Name"));
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), BaseActivity.class);
                startActivity(intent);
            }
        });

        add_storage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), NewStorageActivity.class);
                intent.putExtra(EXTRA_STORAGE_DATA, data.get("Id"));
                startActivity(intent);
            }
        });

        add_employee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), NewEmployeeActivity.class);
                intent.putExtra(EXTRA_EMPLOYEE_DATA, data.get("Id"));
                startActivity(intent);
            }
        });

        FirebaseDB.getStorages(new FirebaseDB.GetStorageCallback() {
            @Override
            public void onCallback(HashMap<String, Object> data) {
                updateStoragesList(data);
            }
        }, db, data.get("Id"));

        FirebaseDB.getEmployees(new FirebaseDB.GetEmployeeCallback() {
            @Override
            public void onCallback(HashMap<String, Object> data) {
                updateEmployeesList(data);
            }
        }, db, data.get("Id"));
    }

    @Override
    public void onClick(int position) {
        Log.i(TAG, "Clicked! " + position);
        Intent intent = new Intent(getBaseContext(), StorageActivity.class);
        startActivity(intent);
    }

    private void updateStoragesList(HashMap<String, Object> data) {
//        loading.setVisibility(View.GONE);
//        restaurantsList.setVisibility(View.VISIBLE);
//
//        restaurantsList.setLayoutManager(new GridLayoutManager(this, 2));
//        RestaurantListAdapter restaurantListAdapter = new RestaurantListAdapter(data, this::onClick);
//        restaurantsList.setAdapter(restaurantListAdapter);
        RestaurantStorageListAdapter storageListAdapter = new RestaurantStorageListAdapter(data, this);
        storage_list.setLayoutManager(new GridLayoutManager(this, 2));
        storage_list.setAdapter(storageListAdapter);
    }

    private void updateEmployeesList(HashMap<String, Object> data) {
        RestaurantEmployeeListAdapter employeeListAdapter = new RestaurantEmployeeListAdapter(data, this::onClick);
        employee_list.setLayoutManager(new GridLayoutManager(this, 2));
        employee_list.setAdapter(employeeListAdapter);
    }
}
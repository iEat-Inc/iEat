package lv.ieatinc.ieat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import lv.ieatinc.ieat.adapters.EmployeeListAdapter;
import lv.ieatinc.ieat.adapters.RestaurantStorageListAdapter;
import lv.ieatinc.ieat.fragments.HomeFragment;
import lv.ieatinc.ieat.utilities.FirebaseDB;

public class RestaurantActivity extends AppCompatActivity implements RestaurantStorageListAdapter.OnRestaurantStorageClickListener {
    public static final String TAG = "RESTAURANT ACTIVITY";
    public static final String EXTRA_STORAGE_DATA = "lv.ieatinc.ieat.STORAGE_DATA";
    public static final String EXTRA_EMPLOYEE_DATA = "lv.ieatinc.ieat.EMPLOYEE_DATA";
    public static final String EXTRA_EXISTING_STORAGE_DATA = "lv.ieatinc.ieat.EXISTING_EMPLOYEE_DATA";
    public static final String EXTRA_EXISTING_EMPLOYEE_DATA = "lv.ieatinc.ieat.EXISTING_STORAGE_DATA";

    private FirebaseFirestore db;
    RecyclerView storage_list;
    RecyclerView employee_list;
    Map<String, Object> storages;
    HashMap<String, String> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_restaurant);

        db = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        Serializable serializableExtra = intent.getSerializableExtra(HomeFragment.EXTRA_DATA);
        data = (HashMap<String, String>) serializableExtra;

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
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });

        add_storage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), NewStorageActivity.class);
                intent.putExtra(EXTRA_STORAGE_DATA, data.get("Id"));
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });

        add_employee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), NewEmployeeActivity.class);
                intent.putExtra(EXTRA_EMPLOYEE_DATA, data.get("Id"));
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });
    }

    @Override
    public void onStart(){
        super.onStart();
        FirebaseDB.getStorages(new FirebaseDB.GetStorageCallback() {
            @Override
            public void onCallback(HashMap<String, Object> data) {
                storages = data;
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
        HashMap<String, Object> shelves = new HashMap<>();
//        shelves.put("restaurantId", data.get("Id"));
//        shelves.put("storages", storages);
        intent.putExtra(EXTRA_EXISTING_STORAGE_DATA, shelves);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_out_left,R.anim.slide_in_right);
    }

    private void updateStoragesList(HashMap<String, Object> data) {
        int spanCount = data.size() <= 4 ? 2 : 3;
        RestaurantStorageListAdapter storageListAdapter = new RestaurantStorageListAdapter(data, this);
        storage_list.setLayoutManager(new GridLayoutManager(this, spanCount));
        storage_list.setAdapter(storageListAdapter);
    }

    private void updateEmployeesList(HashMap<String, Object> data) {
        Log.i(TAG, String.valueOf(data));
        Log.i(TAG, String.valueOf(data.size()));
        int spanCount = data.size() <= 4 ? 2 : 3;
        EmployeeListAdapter employeeListAdapter = new EmployeeListAdapter(data, this::onClick);
        employee_list.setLayoutManager(new GridLayoutManager(this, spanCount));
        employee_list.setAdapter(employeeListAdapter);
    }
}
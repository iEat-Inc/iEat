package lv.ieatinc.ieat.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import lv.ieatinc.ieat.AuthenticationActivity;
import lv.ieatinc.ieat.R;
import lv.ieatinc.ieat.RestaurantActivity;
import lv.ieatinc.ieat.adapters.RestaurantListAdapter;
import lv.ieatinc.ieat.utilities.FirebaseDB;

public class HomeFragment extends Fragment implements RestaurantListAdapter.OnRestaurantClickListener {
    public static final String TAG = "HOME FRAGMENT";
    public static final String EXTRA_DATA = "lv.ieatinc.ieat.DATA";
    private LottieAnimationView loading;
    private RecyclerView restaurantsList;
    private TextView no_restaurants_found;
    private HashMap<String, Object> restaurant_data;

    public HomeFragment() {
        super(R.layout.fragment_home);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        loading = view.findViewById(R.id.home_loading_anim);
        restaurantsList = view.findViewById(R.id.home_restaurants);
        no_restaurants_found = view.findViewById(R.id.home_empty_list_text);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        FirebaseDB.getRestaurants(new FirebaseDB.GetRestaurantCallback() {
            @Override
            public void onCallback(HashMap<String, Object> data) {
                Log.i(TAG, "Restaurants called back");
                if (!data.isEmpty()) {
                    restaurant_data = data;
                    updateRestaurantsList(data);
                } else {
                    loading.setVisibility(View.GONE);
                    no_restaurants_found.setVisibility(View.VISIBLE);
                }
            }
        }, db);

        TextView signout = view.findViewById(R.id.home_signout_text);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AuthenticationActivity.class);
                FirebaseAuth.getInstance().signOut();
                startActivity(intent);
                getActivity().finish();
            }
        });

        Button new_restaurant = view.findViewById(R.id.home_new_rest);
        new_restaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.baseFragmentContainer, NewRestaurantFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack("RestaurantBackStack")
                        .commit();
            }
        });
    }

    @Override
    public void onClick(int position) {
        Intent intent = new Intent(getActivity(), RestaurantActivity.class);

        // Since we don't know the key, we get all the keys in order, create an array from them
        // and get the key at the position we need
        Object key = restaurant_data.keySet().toArray()[position];
        HashMap<String, Object> data = (HashMap<String, Object>) restaurant_data.get(key);
        intent.putExtra(EXTRA_DATA, data);
        startActivity(intent);
        getActivity().finish();
    }

    private void updateRestaurantsList(HashMap<String, Object> data) {
        loading.setVisibility(View.GONE);
        restaurantsList.setVisibility(View.VISIBLE);

        restaurantsList.setLayoutManager(new LinearLayoutManager(getView().getContext()));
        RestaurantListAdapter restaurantListAdapter = new RestaurantListAdapter(data, this::onClick);
        restaurantsList.setAdapter(restaurantListAdapter);
    }
}
package lv.ieatinc.ieat.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import lv.ieatinc.ieat.AuthenticationActivity;
import lv.ieatinc.ieat.R;
import lv.ieatinc.ieat.adapters.RestaurantListAdapter;
import lv.ieatinc.ieat.utilities.FirebaseDB;

public class HomeFragment extends Fragment {
    public final String TAG = "HOME FRAGMENT";
    private LottieAnimationView loading;
    private RecyclerView restaurantsList;

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
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        FirebaseDB.getRestaurants(new FirebaseDB.GetRestaurantCallback() {
            @Override
            public void onCallback(HashMap<Integer, Object> data) {
                Log.i(TAG, "Restaurants called back");
                loading.setVisibility(View.GONE);
                restaurantsList.setVisibility(View.VISIBLE);

                restaurantsList.setLayoutManager(new LinearLayoutManager(view.getContext()));
//                restaurantsList.setHasFixedSize(true);
                RestaurantListAdapter restaurantListAdapter = new RestaurantListAdapter(data);
                restaurantsList.setAdapter(restaurantListAdapter);
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
}
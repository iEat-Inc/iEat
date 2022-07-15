package lv.ieatinc.ieat.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import lv.ieatinc.ieat.R;

public class HomeFragment extends Fragment {
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
        restaurantsList = view.findViewById(R.id.rv_restaurants);

        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        restaurantsList.setLayoutManager(layoutManager);
        restaurantsList.setHasFixedSize(true);
    }
}
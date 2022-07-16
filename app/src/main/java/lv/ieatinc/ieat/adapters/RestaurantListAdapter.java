package lv.ieatinc.ieat.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lv.ieatinc.ieat.R;

public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.ViewHolder>{
    private HashMap<Integer, Object> localDataSet;
    private final String TAG = "RESTAURANT LIST ADAPTER";

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final Button btn;

        public ViewHolder(View view) {
            super(view);

            btn = (Button) view.findViewById(R.id.restaurant_list_item_button);
        }

        public Button getButton() {
            return btn;
        }
    }

    public RestaurantListAdapter(HashMap<Integer, Object> dataSet) {
        localDataSet = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.restaurant_list_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Map<String, String> data = (Map<String, String>) localDataSet.get(position + 1);
        viewHolder.getButton().setText(data.get("Name"));
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}

package lv.ieatinc.ieat.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lv.ieatinc.ieat.R;
import lv.ieatinc.ieat.fragments.ForgotPasswordFragment;

public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.ViewHolder>{
    private HashMap<String, Object> localDataSet;
    private static final String TAG = "RESTAURANT LIST ADAPTER";
    private OnRestaurantClickListener mOnRestaurantClickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final Button btn;
        private OnRestaurantClickListener onRestaurantClickListener;

        public ViewHolder(View view, OnRestaurantClickListener onClickListener) {
            super(view);

            btn = (Button) view.findViewById(R.id.restaurant_list_item_button);
            this.onRestaurantClickListener = onClickListener;

            btn.setOnClickListener(this);
        }

        public Button getButton() {
            return btn;
        }

        @Override
        public void onClick(View view) {
            onRestaurantClickListener.onClick(getAdapterPosition());
        }
    }

    public RestaurantListAdapter(HashMap<String, Object> dataSet, OnRestaurantClickListener onClickListener) {
        localDataSet = dataSet;
        this.mOnRestaurantClickListener = onClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.restaurant_list_item, viewGroup, false);

        return new ViewHolder(view, mOnRestaurantClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        // Since we don't know the key, we get all the keys in order, create an array from them
        // and get the key at the position we need
        Object key = localDataSet.keySet().toArray()[position];
        Map<String, String> data = (Map<String, String>) localDataSet.get(key);
        viewHolder.getButton().setText(data.get("Name"));
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

    public interface OnRestaurantClickListener {
        void onClick(int position);
    }
}

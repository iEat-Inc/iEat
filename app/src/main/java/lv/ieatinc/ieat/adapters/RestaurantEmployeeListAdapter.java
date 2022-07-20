package lv.ieatinc.ieat.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.Map;

import lv.ieatinc.ieat.R;

public class RestaurantEmployeeListAdapter extends RecyclerView.Adapter<RestaurantEmployeeListAdapter.ViewHolder> {
    private HashMap<String, Object> localDataSet;
    private static final String TAG = "RESTAURANT EMPLOYEE LIST ADAPTER";
    private RestaurantEmployeeListAdapter.OnRestaurantEmployeeClickListener mOnRestaurantEmployeeClickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final Button btn;
        private final TextView textView;
        private RestaurantEmployeeListAdapter.OnRestaurantEmployeeClickListener onRestaurantEmployeeClickListener;

        public ViewHolder(View view, RestaurantEmployeeListAdapter.OnRestaurantEmployeeClickListener onClickListener) {
            super(view);

            btn = (Button) view.findViewById(R.id.storage_list_btn_item);
            textView = (TextView) view.findViewById(R.id.storage_list_text_item);
            this.onRestaurantEmployeeClickListener = onClickListener;
            btn.setOnClickListener(this);
        }

        public Button getButton() {
            return btn;
        }

        public TextView getTextView() {
            return textView;
        }

        @Override
        public void onClick(View view) {
            onRestaurantEmployeeClickListener.onClick(getAdapterPosition());
        }
    }

    public RestaurantEmployeeListAdapter(HashMap<String, Object> dataSet, RestaurantEmployeeListAdapter.OnRestaurantEmployeeClickListener onClickListener) {
        localDataSet = dataSet;
        this.mOnRestaurantEmployeeClickListener = onClickListener;
    }

    @Override
    public RestaurantEmployeeListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.restaurant_storage_list_item, viewGroup, false);

        return new RestaurantEmployeeListAdapter.ViewHolder(view, mOnRestaurantEmployeeClickListener);
    }

    @Override
    public void onBindViewHolder(RestaurantEmployeeListAdapter.ViewHolder viewHolder, final int position) {
        // Since we don't know the key, we get all the keys in order, create an array from them
        // and get the key at the position we need
        Object key = localDataSet.keySet().toArray()[position];
        Map<String, String> data = (Map<String, String>) localDataSet.get(key);
        viewHolder.getTextView().setText(data.get("Name"));
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

    public interface OnRestaurantEmployeeClickListener {
        void onClick(int position);
    }
}

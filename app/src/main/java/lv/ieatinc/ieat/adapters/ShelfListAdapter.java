package lv.ieatinc.ieat.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.Map;

import lv.ieatinc.ieat.R;

public class ShelfListAdapter extends RecyclerView.Adapter<ShelfListAdapter.ViewHolder>{
    private HashMap<String, Object> localDataSet;
    private final String TAG = "STORAGE LIST ADAPTER";

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final Button btn;

        public ViewHolder(View view) {
            super(view);

            btn = (Button) view.findViewById(R.id.shelf_list_item_button);
        }

        public Button getButton() {
            return btn;
        }
    }
    public ShelfListAdapter(HashMap<String, Object> dataSet) {
        localDataSet = dataSet;
    }

    @Override
    public ShelfListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.shelf_list_item, viewGroup, false);

        return new ShelfListAdapter.ViewHolder(view);    }

    @Override
    public void onBindViewHolder(ShelfListAdapter.ViewHolder holder, int position) {
        Object key = localDataSet.keySet().toArray()[position];
        Map<String, String> data = (Map<String, String>) localDataSet.get(key);
        holder.getButton().setText(data.get("Name"));
        holder.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

    public interface OnClickListener {
        void onClick(int position);
    }
}


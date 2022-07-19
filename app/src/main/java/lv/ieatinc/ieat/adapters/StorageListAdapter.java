package lv.ieatinc.ieat.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.Map;

import lv.ieatinc.ieat.R;

public class StorageListAdapter extends RecyclerView.Adapter<StorageListAdapter.ViewHolder>{
    private HashMap<String, Object> localDataSet;
    private final String TAG = "STORAGE LIST ADAPTER";

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final Button btn;

        public ViewHolder(View view) {
            super(view);

            btn = (Button) view.findViewById(R.id.storage_list_item_button);
        }

        public Button getButton() {
            return btn;
        }
    }
    public StorageListAdapter(HashMap<String, Object> dataSet) {
        localDataSet = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.storage_list_item, viewGroup, false);

        return new StorageListAdapter.ViewHolder(view);    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
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

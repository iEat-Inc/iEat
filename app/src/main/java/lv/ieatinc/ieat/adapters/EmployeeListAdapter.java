package lv.ieatinc.ieat.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import lv.ieatinc.ieat.R;

public class EmployeeListAdapter extends RecyclerView.Adapter<EmployeeListAdapter.ViewHolder>{
    private HashMap<String, Object> localDataSet;
    private static final String TAG = "RESTAURANT LIST ADAPTER";
    private EmployeeListAdapter.OnEmployeeClickListener mOnEmployeeClickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final Button btn;
        private final TextView username;
        private EmployeeListAdapter.OnEmployeeClickListener onEmployeeClickListener;

        public ViewHolder(View view, EmployeeListAdapter.OnEmployeeClickListener onClickListener) {
            super(view);

            btn = (Button) view.findViewById(R.id.storage_list_btn_item);
            username = (TextView) view.findViewById(R.id.storage_list_text_item);

            this.onEmployeeClickListener = onClickListener;

            btn.setOnClickListener(this);
        }

        public Button getButton() {
            return btn;
        }

        public TextView getUsername() {
            return username;
        }

        @Override
        public void onClick(View view) {
            onEmployeeClickListener.onClick(getAdapterPosition());
        }
    }

    public EmployeeListAdapter(HashMap<String, Object> dataSet, EmployeeListAdapter.OnEmployeeClickListener onClickListener) {
        localDataSet = dataSet;
        this.mOnEmployeeClickListener = onClickListener;
    }

    @Override
    public EmployeeListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.restaurant_storage_list_item, viewGroup, false);

        return new EmployeeListAdapter.ViewHolder(view, mOnEmployeeClickListener);
    }

    @Override
    public void onBindViewHolder(EmployeeListAdapter.ViewHolder viewHolder, final int position) {
        // Since we don't know the key, we get all the keys in order, create an array from them
        // and get the key at the position we need
        Object key = localDataSet.keySet().toArray()[position];
        Map<String, String> data = (Map<String, String>) localDataSet.get(key);
        viewHolder.getUsername().setText(data.get("Username"));
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

    public interface OnEmployeeClickListener {
        void onClick(int position);
    }
}

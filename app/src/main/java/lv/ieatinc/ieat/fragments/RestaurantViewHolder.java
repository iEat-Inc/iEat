package lv.ieatinc.ieat.fragments;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import lv.ieatinc.ieat.R;

public class RestaurantViewHolder extends RecyclerView.ViewHolder {

    private Button view;
    public RestaurantViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView.findViewById(R.id.home_rest);
    }

    public TextView getView(){
        return view;
    }
}

package lv.ieatinc.ieat.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

import lv.ieatinc.ieat.FirebaseDB;
import lv.ieatinc.ieat.R;

public class LoginFragment extends Fragment {
    public final String TAG = "LOGIN FRAGMENT";

    public LoginFragment() {
        super(R.layout.login_fragment);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        FirebaseDB.readData(new FirebaseDB.FirestoreCallback() {
            @Override
            public void onCallback(Map<String, Object> data) {
                Log.i(TAG, String.valueOf(data));
            }
        }, db);
    }
}

package lv.ieatinc.ieat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import lv.ieatinc.ieat.fragments.HomeFragment;
import lv.ieatinc.ieat.fragments.LoginFragment;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity);
        getSupportActionBar().hide();
    }
}
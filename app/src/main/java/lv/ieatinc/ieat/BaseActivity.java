package lv.ieatinc.ieat;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import lv.ieatinc.ieat.fragments.LoginFragment;
import lv.ieatinc.ieat.fragments.SignupFragment;

public class BaseActivity extends AppCompatActivity {
    public BaseActivity() {
        super(R.layout.base_activity);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragmentContainer, LoginFragment.class, null)
                    .commit();
        }
    }

    public void buttonChangeActivity(View view) {
        getSupportFragmentManager() // getParentFragmentManager/getChildFragmentManager doesn't seem to work
                .beginTransaction()
                .replace(R.id.fragmentContainer, SignupFragment.class, null)
                .setReorderingAllowed(true)
                .addToBackStack("userControlBackStack")
                .commit();
    }
}

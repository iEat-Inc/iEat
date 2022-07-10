package lv.ieatinc.ieat;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import lv.ieatinc.ieat.fragments.LoginFragment;

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
                    .add(R.id.fragmentContainerView, LoginFragment.class, null)
                    .commit();
        }
    }
}

package lv.ieatinc.ieat.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import lv.ieatinc.ieat.Constants;
import lv.ieatinc.ieat.R;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class ForgotPasswordFragment extends Fragment {
    public final String TAG = "FORGOT PASSWORD FRAGMENT";
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    public ForgotPasswordFragment() {
        super(R.layout.forgot_password_fragment);

//        final OkHttpClient client = new OkHttpClient();
//
//        RequestBody formBody = new FormBody.Builder()
//                .add("email", "kristians.murmanis.km7@gmail.com")
//                .add("username", "Hiype")
//                .add("emailcode", "huh4532iuh4")
//                .build();
//        Request request = new Request.Builder()
//                .url(Constants.API_URL + Constants.FP_API_CALL)
//                .post(formBody)
//                .build();
//
//        try(Response response = client.newCall(request).execute()) {
//
//        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ImageView back_arrow = view.findViewById(R.id.fp_back_arrow);
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Does the same as Back button, goes back to previous fragment, destroying current
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        Button change_password = view.findViewById(R.id.fp_change_password_button);
        change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView email_code = (TextView) view.findViewById(R.id.fp_email_code_input);
                TextView new_password = view.findViewById(R.id.fp_new_password_input);

                email_code.getText().toString();
                new_password.getText().toString();
            }
        });
    }
}

package lv.ieatinc.ieat.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import lv.ieatinc.ieat.Constants;
import lv.ieatinc.ieat.R;
import lv.ieatinc.ieat.utilities.RandomString;
import lv.ieatinc.ieat.utilities.Timer;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ForgotPasswordFragment extends Fragment {
    public final String TAG = "FORGOT PASSWORD FRAGMENT";
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    final OkHttpClient client;
    private String random_str;
    private Timer timer;

    public ForgotPasswordFragment() {
        super(R.layout.forgot_password_fragment);
        client = new OkHttpClient();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        sendCodeEmail();
        timer = new Timer(view.findViewById(R.id.fp_timer_time_text));

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

                String email_code_input = email_code.getText().toString().trim();
                String new_password_input = new_password.getText().toString();

                if (!email_code_input.isEmpty()) {
                    if (!email_code_input.equals(random_str)) {
                        email_code.setError("Not the correct code");
                        email_code.requestFocus();
                        return;
                    }
                } else {
                    email_code.setError("Please enter the email code!");
                    email_code.requestFocus();
                    return;
                }

                if(!new_password_input.isEmpty()) {
                    if(new_password_input.length() < Constants.MIN_PASSWORD_LENGTH) {
                        email_code.setError("The password is too short!");
                        email_code.requestFocus();
                        return;
                    }
                } else {
                    email_code.setError("Please enter a new password!");
                    email_code.requestFocus();
                    return;
                }

                if(!timer.isCounting()) {
                    Toast.makeText(getContext(), "Code has become invalid, please request another email.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        TextView send_email = view.findViewById(R.id.fp_send_email_text);
        send_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCodeEmail();
            }
        });
    }

    private void sendCodeEmail() {
        RandomString rng_str = new RandomString(8);
        rng_str.nextString();
        random_str = rng_str.getCurrentString();

        RequestBody formBody = new FormBody.Builder()
                .add("email", "kristians.murmanis.km7@gmail.com")
                .add("username", "Hiype")
                .add("emailcode", random_str)
                .build();
        Request request = new Request.Builder()
                .url(Constants.API_URL + Constants.FP_API_CALL)
                .post(formBody)
                .build();

        Call call = (Call) client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.getStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    Log.e("OKHTTP UPDATE DATA RESP", "Code: " + String.valueOf(response.code()) + " String: " + response.body().string());
                } else {
                    Log.e("OKHTTP UPDATE DATA RESP", "Code: " + response.code());
                }
            }
        });
    }
}

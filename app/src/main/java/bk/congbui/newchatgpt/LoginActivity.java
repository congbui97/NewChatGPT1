package bk.congbui.newchatgpt;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {
    private EditText edtUsername,edtPassword,edtRePassword;
    private Button btnLoginOrSignup;
    private TextView tvSignUp;
    private AnimationDrawable rocketAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        unit();
        init();

        setOnClick();
    }

    private void setOnClick() {
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtRePassword.setVisibility(View.VISIBLE);
                tvSignUp.setVisibility(View.GONE);
            }
        });

    }

    private void unit() {
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        edtRePassword = findViewById(R.id.edtRepassword);
        btnLoginOrSignup = findViewById(R.id.btnLoginOrSignup);
        tvSignUp = findViewById(R.id.tvSignup);

        ImageView rocketImage =  findViewById(R.id.imgLogo);
        rocketImage.setBackgroundResource(R.drawable.cat_animation);
        rocketAnimation = (AnimationDrawable) rocketImage.getBackground();
//        rocketAnimation.start();
        rocketImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rocketImage.setBackgroundResource(R.drawable.cat_animation);
                rocketAnimation = (AnimationDrawable) rocketImage.getBackground();
                rocketAnimation.run();

            }
        });
    }

    private void init(){
        edtRePassword.setVisibility(View.GONE);

    }

    @Override
    public void onBackPressed() {
        edtRePassword.setVisibility(View.GONE);
        tvSignUp.setVisibility(View.VISIBLE);
    }
}
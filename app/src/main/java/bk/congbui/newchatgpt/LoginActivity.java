package bk.congbui.newchatgpt;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    //list image tu trai qua phai
    //trai  len tren 13  16 17 18 19 20 21 22 23 24 25 26
    // phai xuong duoi 26 27 30 31 32 34 35 36 37 toi giua
    //nham mat giua 37 38 39 40 mo mat 41 42 43
    // nham mat nhin ben phai 57 58 59 60 61
    // nhin duoi phai qua trai  6 7 8 9 10 11 12
    private int LOGIN_MODE = 0;
    private int SIGN_UP_MODE = 1;
    private EditText edtUsername,edtPassword,edtRePassword;
    private Button btnLoginOrSignup;
    private TextView tvSignUp;
    private AnimationDrawable rocketAnimation;

    private ImageView rocketImage;
    // mode login (0) or mode sign up (1)
    private int mode;

    private int[] anilation_list = new int[]{
            R.drawable.cat12,R.drawable.cat11,
            R.drawable.cat10, R.drawable.cat9,
            R.drawable.cat8, R.drawable.cat7};

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
                mode = SIGN_UP_MODE;
            }
        });

        edtUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAnimatiion(R.drawable.cat_nhinlentroi);
            }
        });

        edtPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtPassword.setFocusable(true);
                setAnimatiion(R.drawable.cat_nhinxuongpassword);
            }
        });

        edtUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                byte[] result = s.toString().getBytes();
                if(result.length == 1 ){
                    setAnimatiion(R.drawable.cat_nhinlentroi);
                }
            }
        });

        edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int pos = 3;
                byte[] result = s.toString().getBytes();
                if (result.length  < pos){
                    rocketImage.setBackgroundResource(anilation_list[0]);
                }else if(result.length < 2 * pos){
                    rocketImage.setBackgroundResource(anilation_list[1]);
                }else if(result.length < 3 * pos){
                    rocketImage.setBackgroundResource(anilation_list[2]);
                }else if(result.length < 4 * pos){
                    rocketImage.setBackgroundResource(anilation_list[3]);
                }else if(result.length < 5 * pos){
                    rocketImage.setBackgroundResource(anilation_list[4]);
                }else if(result.length < 6 * pos){
                    rocketImage.setBackgroundResource(anilation_list[5]);
                }
            }
        });

        btnLoginOrSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginOrSignUp();
            }
        });

    }

    private void loginOrSignUp() {
        String username = edtUsername.getText().toString().trim();
        String pass = edtPassword.getText().toString();
        String path = "user/" + username;

        if (mode == LOGIN_MODE){
            DatabaseReference mReference = FirebaseDatabase.getInstance().getReference(path);
            mReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String usernameServer = snapshot.child("userName").getValue(String.class);
//                    long keyMoney = snapshot.child("keyMoney").getValue(Long.class);
//                    long location = snapshot.child("location").getValue(Long.class);
//                    int pos = snapshot.child("pos").getValue(Integer.class);
                    String passServer = snapshot.child("passWord").getValue(String.class);
                    checkLogin(username,usernameServer,pass,passServer);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }else if (mode == SIGN_UP_MODE){
            String repass = edtRePassword.getText().toString();
            if (pass.equals(repass) == true){
                DatabaseReference database = FirebaseDatabase.getInstance().getReference(path);
                User user = new User(username, pass , System.currentTimeMillis() , 0,System.currentTimeMillis());
                database.setValue(user, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        Toast.makeText(getApplicationContext(), "push complete",Toast.LENGTH_SHORT).show();
                    }
                });

            }else {
                Toast.makeText(getApplicationContext(), "vui long nhap lai mat khau",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void checkLogin(String username, String usernameServer, String pass, String passServer) {
        if (username.equals(usernameServer) == false){
            Toast.makeText(getApplicationContext(), "Account does not exist", Toast.LENGTH_LONG).show();
        }else{
            if (pass.equals(passServer) == false){
                Toast.makeText(getApplicationContext(),"Incorrect password",Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(getApplicationContext(),"Login Success",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(LoginActivity.this , MainActivity.class);
                startActivity(intent);
            }
        }
    }

    private void unit() {
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        edtRePassword = findViewById(R.id.edtRepassword);
        btnLoginOrSignup = findViewById(R.id.btnLoginOrSignup);
        tvSignUp = findViewById(R.id.tvSignup);

        rocketImage =  findViewById(R.id.imgLogo);
        rocketImage.setBackgroundResource(R.drawable.cat_animation);
        rocketAnimation = (AnimationDrawable) rocketImage.getBackground();
        setAnimatiion(R.drawable.cat_first_animation);
        mode = LOGIN_MODE;
    }

    private void init(){
        edtRePassword.setVisibility(View.GONE);
    }

    private void setAnimatiion(int animation){
        rocketAnimation.stop();
        rocketImage.setBackgroundResource(animation);
        rocketAnimation = (AnimationDrawable) rocketImage.getBackground();
        rocketAnimation.start();
    }

    @Override
    public void onBackPressed() {
        edtRePassword.setVisibility(View.GONE);
        if (mode == SIGN_UP_MODE){
            tvSignUp.setVisibility(View.VISIBLE);
            mode = LOGIN_MODE;
        }else {
            finish();
        }
    }
}
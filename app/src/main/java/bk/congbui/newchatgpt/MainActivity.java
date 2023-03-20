package bk.congbui.newchatgpt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {
    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;

    private ImageView imgChat , imgAssistant , imgCreateImage , imgChatAI;
    public static TextToSpeech speech;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        unit();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            //If the draw over permission is not available open the settings screen
            //to grant the permission.
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
        } else {
            initializeView();
        }

        if (ContextCompat.checkSelfPermission(this , Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            checkPermissions(Manifest.permission.RECORD_AUDIO);
        }
    }

    private void unit() {
        imgChat = findViewById(R.id.imgChat);
        imgAssistant = findViewById(R.id.imgAssistant);
        imgCreateImage = findViewById(R.id.imgCreateImage);
        imgChatAI = findViewById(R.id.imgChatAI);
        speech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i != TextToSpeech.ERROR) {
                    speech.setLanguage(Locale.forLanguageTag("vi"));
                }
            }
        });

        checkLogin(this , MainActivity.this);
    }

    private void checkPermissions(String permission) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) == PackageManager.PERMISSION_DENIED) {
            // Requesting the permission
            ActivityCompat.requestPermissions(MainActivity.this, new String[] { permission }, 1001);
        }
        else {
            Toast.makeText(MainActivity.this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode,
                permissions,
                grantResults);

        if (requestCode == 1001) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "Record Permission Granted", Toast.LENGTH_SHORT) .show();
            }
            else {
                Toast.makeText(MainActivity.this, "Record Permission Denied", Toast.LENGTH_SHORT) .show();
            }
        }

    }


    private void initializeView() {
        imgChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this , ChatActivity.class);
                startActivity(intent);
            }
        });


        imgAssistant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startService(new Intent(MainActivity.this, ChatHeadService.class));
                finish();

            }
        });

        imgCreateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this , ImageActivity.class);
                startActivity(intent);
            }
        });

        imgChatAI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this , AiChatActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE_DRAW_OVER_OTHER_APP_PERMISSION) {

            //Check if the permission is granted or not.
            // Settings activity never returns proper value so instead check with following method
            if (Settings.canDrawOverlays(this)) {
                initializeView();
            } else { //Permission is not available
                Toast.makeText(MainActivity.this,
                        "Draw over other app permission not available. Closing the application",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


//    public static Call<ResponseBody> createApi(String textSend){
//
//        textSend = textSend.replace('"','\'');
//
//        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
//                .callTimeout(2, TimeUnit.MINUTES)
//                .connectTimeout(20, TimeUnit.SECONDS)
//                .readTimeout(60, TimeUnit.SECONDS)
//                .writeTimeout(60, TimeUnit.SECONDS);
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("https://api.openai.com")
//                .addConverterFactory(GsonConverterFactory.create())
//                .client(httpClient.build()).build();
//
//        ChatActivity.OpenAI api = retrofit.create(ChatActivity.OpenAI.class);
//
////        4097 tokens,
//        String requestJson =
//                "{" +
//                        "\"model\":\"text-davinci-003\"," +
//                        "\"prompt\":\"" +
//                        textSend +
//                        "\"," +
//                        "\"max_tokens\":3000," +
//                        "\"temperature\":0.2" +
//                        "}";
//        Log.d("aaa", requestJson);
//        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestJson);
//
//        Call<ResponseBody> call = api.generateText(requestBody);
//
//
//        return call;
//    }

    public static void checkLogin(Context context , Activity activity){
        SharedPreferences sharedPref = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        DatabaseReference mReference;
        String userName = sharedPref.getString("userName","");
        String passWord = sharedPref.getString("passWord", "");
        long location = sharedPref.getLong("location" , 0);
        if (userName.equals("") == false){
            String path = "user/" + userName;
            mReference = FirebaseDatabase.getInstance().getReference(path);
            mReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User user = snapshot.getValue(User.class);
                    if (user.getUserName().equals(userName) && user.getPassWord().equals(passWord) && location != user.getLocation()){
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.clear();
                        editor.apply();
                        Intent intent = new Intent(activity , LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        activity.startActivity(intent);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }

    }
}

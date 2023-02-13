package bk.congbui.newchatgpt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;


public class MainActivity extends AppCompatActivity {
    private String url = "https://api.openai.com/v1/completions";
    private String api_key = "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";
    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;

    private ImageView imgChat , imgAssistant;

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

//        updateText();


//        Thread thread = new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//                try {
//                    OpenAiService service = new OpenAiService("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");
//                    Engine davinci = service.getEngine("davinci");
//                    ArrayList<CompletionChoice> storyArray = new ArrayList<CompletionChoice>();
//
//                    CompletionRequest completionRequest = CompletionRequest.builder()
//                            .prompt("The following is a spooky story written for kids, just in time for Halloween. Everyone always talks about the old house at the end of the street, but I couldn't believe what happened when I went inside.")
//                            .temperature(0.7)
//                            .maxTokens(2000)
//                            .topP(1.0)
//                            .frequencyPenalty(0.0)
//                            .presencePenalty(0.3)
//                            .echo(true)
//                            .build();
//                    service.createCompletion("davinci", completionRequest).getChoices().forEach(line -> {storyArray.add(line);});
////                    tvHello.setText(storyArray.toString());
//                    if (storyArray.size() > 0) {
//                        for (int i = 0; i < storyArray.size(); i++) {
//                            Toast toast =  Toast.makeText(getApplicationContext(), storyArray.get(i).getText(), Toast.LENGTH_SHORT);
//                            toast.show();
//                        }
//                    } else {
//                        Toast toast =  Toast.makeText(getApplicationContext(), "storyArray is null", Toast.LENGTH_SHORT);
//                        toast.show();
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
//        });
//        thread.start();
    }

    private void unit() {
        imgChat = findViewById(R.id.imgChat);
        imgAssistant = findViewById(R.id.imgAssistant);
    }

    public void updateText() {
        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    List<String> stop  = new ArrayList<>();
                    stop.add(" Human:");
                    stop.add(("AI:"));
                    OpenAI openAI = new OpenAI(api_key);
                    Response<CompletionResponse> response = openAI.createCompletion("text-davinci-003"
                    ,"hello world" , 0.6 , 4000 , 1 , 0.0 , 0.6 ,stop );

                    if (response.isSuccessful()){
                        response.body().getChoices().forEach(choices -> {
                            choices.xuatLog();
                        });
                    }
                }catch (Exception e){
                    Log.d("aaa" , e.getMessage());
                }
            }
        };

        thread.start();
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

    // This function is called when the user accepts or decline the permission.
    // Request Code is used to check which permission called this function.
    // This request code is provided when the user is prompt for permission.

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

    private void callOpenAIApi(){

    }
}







//    private void getResponse(String query) throws JSONException {
//        // setting text on for question on below line.
////        questionTV.text = query
////        queryEdt.setText("")
//        // creating a queue for request queue.
//        RequestQueue queue  = Volley.newRequestQueue(getApplicationContext());
//        // creating a json object on below line.
//        JSONObject jsonObject = new  JSONObject();
//
//        // adding params to json object.
//        jsonObject.put("model", "text-davinci-003");
//        jsonObject.put("prompt", query);
//        jsonObject.put("temperature", 0);
//        jsonObject.put("max_tokens", 100);
//        jsonObject.put("top_p", 1);
//        jsonObject.put("frequency_penalty", 0.0);
//        jsonObject.put("presence_penalty", 0.0);
//
//        // on below line making json object request.
////        JsonObjectRequest postRequest1  =
////                // on below line making json object request.
////                object : JsonObjectRequest(Request.Method.POST, url, jsonObject, Response.Listener { response ->
////                // on below line getting response message and setting it to text view.
////                val responseMsg: String = response.getJSONArray("choices").getJSONObject(0).getString("text") responseTV.text = responseMsg},
////        // adding on error listener
////        Response.ErrorListener {error -> Log.e("TAGAPI", "Error is : " + error.message + "\n" + error)}) {
////
////        }
////            override fun getHeaders(): kotlin.collections.MutableMap<kotlin.String, kotlin.String> {
////                val params: MutableMap<String, String> = HashMap()
////                // adding headers on below line.
////                params["Content-Type"] = "application/json"
////                params["Authorization"] =
////                        "Bearer Enter your token here"
////                return params;
////            }
//
//
//        JsonObjectRequest postRequest = new JsonObjectRequest
//                (Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
//
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//                            String responseMsg  = response.getJSONArray("choices").getJSONObject(0).getString("text");
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.e("TAGAPI", "Error is : " + error.toString() + "\n" + error);}
//
//
//        // on below line adding retry policy for our request.
//
//        RetryPolicy object = new RetryPolicy() {
//            @Override
//            public int getCurrentTimeout() {
//                return 50000;
//            }
//
//            @Override
//            public int getCurrentRetryCount() {
//                return 50000;
//            }
//
//            @Override
//            public void retry(VolleyError error) throws VolleyError {
//
//            }
//        };
////        postRequest.setRetryPolicy(object);
////        // on below line adding our request to queue.
////        queue.add(postRequest);
//    }
//}
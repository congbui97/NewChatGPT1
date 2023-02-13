package bk.congbui.newchatgpt;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import bk.congbui.newchatgpt.message.Message;
import bk.congbui.newchatgpt.message.MessageAdapter;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public class ChatActivity extends AppCompatActivity {

    public interface OpenAI {
        @Headers({
                "Content-Type: application/json",
                "Authorization: Bearer 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"
        })
        @POST("/v1/completions")
        Call<ResponseBody> generateText(@Body RequestBody requestBody);
    }

    private ListView lvMessage ;
    private List<Message> data;
    private MessageAdapter adapter;

    private ImageView btnSend;
    private String textSend = "";
    private EditText edtMessage;
    private AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        unit();


    }

    @Override
    protected void onResume() {
        super.onResume();
        setOnclick();

    }

    private void unit() {
        lvMessage = findViewById(R.id.listview);
        btnSend = findViewById(R.id.btnSend);
        edtMessage = findViewById(R.id.edtGettext);
        lvMessage.setDivider(null);
        lvMessage.setDividerHeight(0);
        data = new ArrayList<>();
        adapter = new MessageAdapter(this,data);
        lvMessage.setAdapter(adapter);
    }

    private void setOnclick() {
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textSend = edtMessage.getText().toString();
                if (textSend != null && textSend.equals("")==false ){
                    data.add(new Message(textSend , 0));
                    adapter.notifyDataSetChanged();
                    lvMessage.setSelection(data.size() - 1);
                    callApi();
                    edtMessage.setText("");
                    startLoadingDialog();
                }


            }
        });
    }

    private  void  callApi(){

        //set time out for api

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .callTimeout(2, TimeUnit.MINUTES)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openai.com")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build()).build();

        OpenAI api = retrofit.create(OpenAI.class);

        String requestJson =
                "{" +
                        "\"model\":\"text-davinci-003\"," +
                        "\"prompt\":\"" +
                        textSend +
                        "\"," +
                        "\"max_tokens\":4000," +
                        "\"temperature\":0" +
                        "}";
        Log.d("aaa", requestJson);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestJson);

        Call<ResponseBody> call = api.generateText(requestBody);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String result = response.body().string();
                        Log.d("aaa",result);
                        try {
                            JSONObject resultJS = new JSONObject(result);
                            String text = resultJS.getString("choices");
                            text = text.replace("[","");
                            text = text.replace("]","");
                            JSONObject resultText = new JSONObject(text);
                            String lastValue = resultText.getString("text");
                            if (lastValue.startsWith("\n\n")){
                                lastValue = lastValue.substring(2);
                            }
                            data.add(new Message(lastValue,1));
                            adapter.notifyDataSetChanged();
                            lvMessage.setSelection(data.size() - 1);
                            dismissDialog();
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                        // Do something with the result
//                        tvResult.setText(result);
                    } catch (IOException e) {
//                        tvResult.setText(e.toString());
                        e.printStackTrace();
                    }
                }else {
                    int code = response.code();
                    // Handle error response
                    try {
                        String error = response.errorBody().string();
                        data.add(new Message("Server hiện đang quá tải, vui lòng thử lại...",2));
                        adapter.notifyDataSetChanged();
                        lvMessage.setSelection(data.size() - 1);
                        dismissDialog();


//                        tvResult.setText(error);
                        Log.d("aaa",error);
                        // Log or display the error message
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Handle failure
                Log.d("aaa",call.toString());
                data.add(new Message("Vui lòng kiểm tra mạng...",2));
                adapter.notifyDataSetChanged();
                lvMessage.setSelection(data.size() - 1);
                dismissDialog();
            }
        });
    }

    public void startLoadingDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
        LayoutInflater inflater =  getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading_layout,null));
        builder.setCancelable(true);
        dialog = builder.create();
        dialog.show();
    }
    public void dismissDialog(){
        dialog.dismiss();
    }

}
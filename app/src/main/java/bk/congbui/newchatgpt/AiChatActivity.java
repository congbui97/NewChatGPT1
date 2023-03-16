package bk.congbui.newchatgpt;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
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

import bk.congbui.newchatgpt.message.Message;
import bk.congbui.newchatgpt.message.MessageAdapter;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AiChatActivity extends AppCompatActivity {
    private OpenAICallApiClass openAI;
    private ListView lvMessage ;
    private List<Message> data;
    private MessageAdapter adapter;

    private ImageView btnSend;
    private String textSend = "";
    private EditText edtMessage;
    private AlertDialog dialog;
    public int PERSON = 0;
    public int OPEN_AI = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai_chat);
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
        openAI = new OpenAICallApiClass();
    }

    public   String callApiChat(String textSend){
        final String[] url = {""};

        //set time out for api
        Call<ResponseBody> call = openAI.createApiChat(textSend);

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
                            String lastValue = resultText.getString("url");
                            if (lastValue.startsWith("\n\n")){
                                lastValue = lastValue.substring(2);
                            }
                            url[0] = lastValue;
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
                        Log.d("aaa","Server hiện đang quá tải, vui lòng thử lại...");
                        Log.d("aaa",error);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Handle failure
                Log.d("aaa",call.toString());

            }
        });

        return url[0];
    }

    private void setOnclick() {
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textSend = edtMessage.getText().toString();
                if (textSend != null && textSend.equals("")==false ){

                    addAdapter(new Message(textSend , 0));
                    callApiChat(createDataForApi(data));
                    edtMessage.setText("");
                    startLoadingDialog();
                }
            }
        });
    }

    public void startLoadingDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(AiChatActivity.this);
        LayoutInflater inflater =  getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading_layout,null));
        builder.setCancelable(true);
        dialog = builder.create();
        dialog.show();
    }
    public void dismissDialog(){
        dialog.dismiss();
    }

    private void addAdapter(Message message){
        MainActivity.speech.speak(textSend, TextToSpeech.QUEUE_FLUSH,null);
        data.add(message);
        adapter.notifyDataSetChanged();
        lvMessage.setSelection(data.size() - 1);
    }

    public String createDataForApi(List<Message> questions){
        String result = "";
        String data = "";
        for (int i = 0; i < questions.size(); i++) {
            Message que = questions.get(i);
            if (que.getWhoSend() == PERSON){
                data = "{\"role\":\"user\",\"content\":\""+ que.getTextMessage() +"\"}";
            }else if (que.getWhoSend() == OPEN_AI){
                data = "{\"role\":\"assistant\",	\"content\":\""+ que.getTextMessage() + "\"}";
            }

            if (i < questions.size() - 2){
                result = result + data +",";
            }else {
                result = result + data;
            }
        }

        return result;
    }

}
package bk.congbui.newchatgpt;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageActivity extends AppCompatActivity {
    private EditText edtInput;
    private ImageView imgResult;
    private Button btnSendData;
    private OpenAICallApiClass openAi;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        unit();
        setClick();
    }

    private void setClick() {
        btnSendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = edtInput.getText().toString().trim();
                callApiImage(data);
                startLoadingDialog();
            }
        });
    }

    private void unit() {
        edtInput = findViewById(R.id.edtTextToImage);
        imgResult = findViewById(R.id.imgResult);
        btnSendData = findViewById(R.id.btnSendData);
        openAi = new OpenAICallApiClass();
    }

    public   void callApiImage(String textSend){
        //set time out for api
        Call<ResponseBody> call = openAi.createApiImage(textSend);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String result = response.body().string();
                        Log.d("aaa",result);
                        try {
                            JSONObject resultJS = new JSONObject(result);
                            String text = resultJS.getString("data");
                            text = text.replace("[","");
                            text = text.replace("]","");
                            JSONObject resultText = new JSONObject(text);
                            String lastValue = resultText.getString("url");
                            if (lastValue.startsWith("\n\n")){
                                lastValue = lastValue.substring(2);
                            }
                            Picasso.get().load(lastValue).into(imgResult);
                            dialog.dismiss();
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

    }

    public void startLoadingDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(ImageActivity.this);
        LayoutInflater inflater =  getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading_layout,null));
        builder.setCancelable(true);
        dialog = builder.create();
        dialog.show();
    }

}
package bk.congbui.newchatgpt;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import bk.congbui.newchatgpt.message.Message;
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

public class OpenAICallApiClass {
    public static String API_KEY = "";

    public OpenAICallApiClass() {}

    public interface OpenAI {
        @Headers({
                "Content-Type: application/json",
                "Authorization: Bearer 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",
                "OpenAI-Organization: org-CMEjxgQapEhnZ6EoxXzam3Hp"
        })
        @POST("/v1/completions")
        Call<ResponseBody> generateText(@Body RequestBody requestBody);
    }

    // Create an API service interface
    interface OpenAiApiService {
        @Headers({
                "Content-Type: application/json",
                "Authorization: Bearer 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",
                "OpenAI-Organization: org-CMEjxgQapEhnZ6EoxXzam3Hp"
        })
        @POST("/v1/images/generations")
        Call<ResponseBody> generateImage(@Body RequestBody request);
    }

    public interface OpenAIChatService {
        @Headers({
                "Content-Type: application/json",
                "Authorization: Bearer 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",
                "OpenAI-Organization: org-CMEjxgQapEhnZ6EoxXzam3Hp"
        })
        @POST("/v1/chat/completions")
        Call<ResponseBody> generateTextChat(@Body RequestBody requestBody);
    }

    public Call<ResponseBody> createApiText(String textSend){

        textSend = textSend.replace('"','\'');

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

//        4097 tokens,
        String requestJson =
                "{" +
                        "\"model\":\"text-davinci-003\"," +
                        "\"prompt\":\"" +
                        textSend +
                        "\"," +
                        "\"max_tokens\":3000," +
                        "\"temperature\":0.2" +
                        "}";
        Log.d("aaa", requestJson);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestJson);

        Call<ResponseBody> call = api.generateText(requestBody);


        return call;
    }

    public Call<ResponseBody> createApiImage(String textSend){

        textSend = textSend.replace('"','\'');

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .callTimeout(2, TimeUnit.MINUTES)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openai.com")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build()).build();

        OpenAiApiService api = retrofit.create(OpenAiApiService.class);

//        4097 tokens,
        String requestJson =
                "{" +
                        "\"model\":\"image-alpha-001\"," +
                        "\"size\":\"1024x1024\"," +
                        "\"prompt\":\"" +
                        textSend +
                        "\"," +
                        "\"n\":1" +

                        "}";
        Log.d("aaa", requestJson);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestJson);

        Call<ResponseBody> call = api.generateImage(requestBody);


        return call;
    }

    public Call<ResponseBody> createApiChat(String textSend){

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .callTimeout(2, TimeUnit.MINUTES)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openai.com")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build()).build();
        OpenAIChatService api = retrofit.create(OpenAIChatService.class);

//        4097 tokens,


        String requestJson = "{" + "\"model\":\"gpt-3.5-turbo\"," +
                        "\"messages\":[" +
                        textSend + "]}";
        Log.d("aaa", requestJson);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestJson);

        Call<ResponseBody> call = api.generateTextChat(requestBody);


        return call;
    }

    public   Message callApiText(String textSend){
        final Message message = new Message();

        //set time out for api
        Call<ResponseBody> call = createApiText(textSend);

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
                            message.setTextMessage(lastValue);
                            message.setWhoSend(1);
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
                        message.setTextMessage("Server hiện đang quá tải, vui lòng thử lại...");
                        message.setWhoSend(2);
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
                message.setTextMessage("Vui lòng kiểm tra mạng...");
                message.setWhoSend(2);
            }
        });

        return message;
    }

    public   String callApiImage(String textSend){
        final String[] url = {""};

        //set time out for api
        Call<ResponseBody> call = createApiImage(textSend);

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

    public   String callApiChat(String textSend){
        final String[] url = {""};

        //set time out for api
        Call<ResponseBody> call = createApiImage(textSend);

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


}

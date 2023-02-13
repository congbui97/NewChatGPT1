package bk.congbui.newchatgpt;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

interface OpenAIService {
//    contentType: String = "application/json"
    @POST("completions")
     Response<CompletionResponse> completions(
            @Header("Content-Type") String contentType ,
            @Header("Authorization")String  apiKey ,
            @Body CompletionRequest request
    );

    @Headers({"Content-Type: application/json", "Authorization: Bearer miCnt5cRzVo5x9rBvIGyT3BlbkFJR7yo7hB6SAxr8r0cUsUX"})
    @POST("https://api.openai.com/v1/engines/davinci/completions")
    Call<response> generateText(@Body Request request);
}

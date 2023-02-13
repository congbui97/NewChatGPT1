package bk.congbui.newchatgpt;

import java.util.List;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OpenAI {
    private String apiKey;
    private Retrofit retrofit;
    public OpenAI(String apiKey) {
        this.apiKey = apiKey;
        retrofit = new Retrofit.Builder().baseUrl("https://api.openai.com/v1/").addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public Response<CompletionResponse> createCompletion(String model , String prompt , double temperature,
                                                         int max_tokens , int top_p , double frequency_penalty ,
                                                         double presence_penalty , List<String> stop){
        CompletionRequest request = new CompletionRequest(model, prompt, temperature, max_tokens, top_p, frequency_penalty, presence_penalty, stop);

        OpenAIService openAIService = retrofit.create(OpenAIService.class);

        return openAIService.completions("application/json" , apiKey , request);
    }
}

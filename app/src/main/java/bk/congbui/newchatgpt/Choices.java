package bk.congbui.newchatgpt;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

public class Choices {
    @SerializedName("text")
    private String text;
    @SerializedName("index")
    private int index;
    @SerializedName("logprobs")
    private String logprobs;
    @SerializedName("finish_reason")
    String finish_version;

    public Choices(String text, int index, String logprobs, String finish_version) {
        this.text = text;
        this.index = index;
        this.logprobs = logprobs;
        this.finish_version = finish_version;
    }

    public String getFinish_version() {
        return finish_version;
    }

    public void setFinish_version(String finish_version) {
        this.finish_version = finish_version;
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getLogprobs() {
        return logprobs;
    }

    public void setLogprobs(String logprobs) {
        this.logprobs = logprobs;
    }

    public void xuatLog(){
        Log.d("aaa" , "text: "+ text);
        Log.d("aaa" , "index: "+ index);
        Log.d("aaa" , "logprobs: "+ logprobs);
        Log.d("aaa" , "finish_version: "+ finish_version);

    }
}

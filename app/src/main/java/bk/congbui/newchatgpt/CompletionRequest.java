package bk.congbui.newchatgpt;

import com.google.gson.annotations.SerializedName;

import java.util.List;

class CompletionRequest{
    String model;
    String promt;
    double temperature;
    int max_tokens;
    int top_p;
    double frequency_penalty;
    double presence_penalty;
    List<String> stop;

    public CompletionRequest(String model , String prompt , double temperature , int max_tokens , int top_p ,
                             double frequency_penalty , double presence_penalty ,
                             List<String> stop){
        this.model = model;
        this.promt = prompt;
        this.temperature = temperature;
        this.max_tokens = max_tokens;
        this.top_p = top_p;
        this.frequency_penalty = frequency_penalty;
        this.presence_penalty = presence_penalty;
        this.stop = stop;
    }


    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPromt() {
        return promt;
    }

    public void setPromt(String promt) {
        this.promt = promt;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public int getMax_tokens() {
        return max_tokens;
    }

    public void setMax_tokens(int max_tokens) {
        this.max_tokens = max_tokens;
    }

    public int getTop_p() {
        return top_p;
    }

    public void setTop_p(int top_p) {
        this.top_p = top_p;
    }

    public double getFrequency_penalty() {
        return frequency_penalty;
    }

    public void setFrequency_penalty(double frequency_penalty) {
        this.frequency_penalty = frequency_penalty;
    }

    public double getPresence_penalty() {
        return presence_penalty;
    }

    public void setPresence_penalty(double presence_penalty) {
        this.presence_penalty = presence_penalty;
    }

    public List<String> getStop() {
        return stop;
    }

    public void setStop(List<String> stop) {
        this.stop = stop;
    }
}
package bk.congbui.newchatgpt;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CompletionResponse {
    @SerializedName("id")
    private String id;
    @SerializedName("choices")
    private List<Choices> choices;

    public CompletionResponse(String id, List<Choices> choices) {
        this.id = id;
        this.choices = choices;
    }

    public List<Choices> getChoices() {
        return choices;
    }

    public void setChoices(List<Choices> choices) {
        this.choices = choices;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

package bk.congbui.newchatgpt.message;

import java.util.List;

public class Message {
    public int PERSON = 0;
    public int OPEN_AI = 1;

    private String textMessage;
    private int whoSend;

    public Message() {
    }

    public Message(String text, int whoSend) {
        this.textMessage = text;
        // 0 is person , 1 is AI
        this.whoSend = whoSend;
    }


    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    public int getWhoSend() {
        return whoSend;
    }

    public void setWhoSend(int whoSend) {
        this.whoSend = whoSend;
    }

    public String createDataForApi(List<Message> questions){
        String result = "";
        String data = "";
        for (int i = 0; i < questions.size(); i++) {
            Message que = questions.get(i);
            if (que.whoSend == PERSON){
                data = "{\"role\":\"user\",	\"content\":\""+ que.textMessage +"\"}";
            }else if (que.whoSend == OPEN_AI){
                data = "{\"role\":\"assistant\",	\"content\":\""+ que.textMessage + "\"}";
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

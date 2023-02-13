package bk.congbui.newchatgpt.message;

public class Message {

    private String textMessage;
    private int whoSend;

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
}

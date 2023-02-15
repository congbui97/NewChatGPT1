package bk.congbui.newchatgpt;

public class User {
    private String userName;
    private String passWord;
    private int location;
    private int pos;

    public User(String userName, String passWord, int location, int pos) {
        this.userName = userName;
        this.passWord = passWord;
        this.location = location;
        this.pos = pos;
    }

    public User() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }
}

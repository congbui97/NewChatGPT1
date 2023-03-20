package bk.congbui.newchatgpt;

public class User {
    private String userName;
    private String passWord;
    // thoi gian dang nhap
    private long location;
    // so lan tim kiem con lai
    private long pos;

    // ma nhap tien
    private long keyMoney;

    public User(String userName, String passWord, long location, long pos , long keyMoney) {
        this.userName = userName;
        this.passWord = passWord;
        this.location = location;
        this.pos = pos;
        this.keyMoney = keyMoney;
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

    public long getLocation() {
        return location;
    }

    public void setLocation(long location) {
        this.location = location;
    }

    public long getPos() {
        return pos;
    }

    public void setPos(long pos) {
        this.pos = pos;
    }

    public long getKeyMoney() {
        return keyMoney;
    }

    public void setKeyMoney(long keyMoney) {
        this.keyMoney = keyMoney;
    }
}

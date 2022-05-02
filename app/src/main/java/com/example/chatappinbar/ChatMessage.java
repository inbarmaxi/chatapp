package com.example.chatappinbar;

public class ChatMessage {

    private long date;
    private String senderId;
    private String content;
    private String senderName;
    private String senderImage;



    public ChatMessage(String senderName,String senderId,String senderImage,String content) {
        this.content = content;
        this.senderId = senderId;
        this.senderImage = senderImage;
        this.senderName = senderName;
        this.date = System.currentTimeMillis();
    }



    public ChatMessage(){}

    public long getDate() {
        return date;
    }


    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getContent() {
        return content;
    }

    public String getSenderImage() {
        return senderImage;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public void setSenderImage(String senderImage) {
        this.senderImage = senderImage;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
}

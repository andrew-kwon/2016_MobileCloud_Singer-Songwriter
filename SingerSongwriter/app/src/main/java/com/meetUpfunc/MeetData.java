package com.meetUpfunc;

/**
 * Created by Administrator on 2016-06-02.
 */
public class MeetData{

    private String meetupName;
    private String content;
    private String userID;

    public MeetData(String meetupName,String content, String userID)
    {
        this.meetupName=meetupName;
        this.content=content;
        this.userID=userID;
    }

    public String getUserID() {
        return userID;
    }
    public void setUserID(String userID) {
        this.userID=userID;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content=content;
    }

    public String getMeetupName() {
        return meetupName;
    }
    public void setMeetupName(String meetupName) {
        this.meetupName=meetupName;
    }


}
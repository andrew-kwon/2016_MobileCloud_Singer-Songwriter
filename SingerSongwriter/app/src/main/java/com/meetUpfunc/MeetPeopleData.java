package com.meetUpfunc;

/**
 * Created by Administrator on 2016-06-02.
 */
public class MeetPeopleData{

    private String meetupName;
    private String userID;
    private String userName;
    public MeetPeopleData(String meetupName, String userID, String userName)
    {
        this.meetupName=meetupName;
        this.userID=userID;
        this.userName=userName;
    }
    public String getUserID() {
        return userID;
    }
    public String getUserName() {
        return userName;
    }
    public String getMeetupName() {
        return meetupName;
    }
    public void setMeetupName(String meetupName) {
        this.meetupName=meetupName;
    }
}
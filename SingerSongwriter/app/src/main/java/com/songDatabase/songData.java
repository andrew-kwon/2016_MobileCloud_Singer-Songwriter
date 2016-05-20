package com.songDatabase;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2016-05-19.
 */
public class songData {

    private String songName;
    private String content;
    private String userName;
    private String likeCount;
    private String userID;
    private Bitmap profilePic;

    public songData(String songName, String content,
                    String userName, String likeCount,String userID, Bitmap profilePic)
    {
        this.songName=songName;
        this.content=content;
        this.userName=userName;
        this.likeCount=likeCount;
        this.userID=userID;
        this.profilePic=profilePic;

    }

    public String getSongName() {
        return songName;
    }
    public void setSongName(String songName) {
        this.songName=songName;
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

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName=userName;
    }

    public String getLikeCount() {
        return likeCount;
    }
    public void setLikeCount(String likeCount) {
        this.likeCount=likeCount;
    }
    public Bitmap getProfilePic() {
        return profilePic;
    }
    public void setProfilePic(Bitmap profilePic) {
        this.profilePic=profilePic;
    }


}
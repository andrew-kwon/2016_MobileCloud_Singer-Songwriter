package com.RecyclerUtil;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mysampleapp.R;

/**
 * Created by kundan on 10/26/2015.
 */
public class SongRecyclerViewHolder extends RecyclerView.ViewHolder {

    TextView songName,userName,content,likeCount;
    ImageView profilePic;

    public SongRecyclerViewHolder(View itemView) {
        super(itemView);

        songName= (TextView) itemView.findViewById(R.id.songName);
        userName= (TextView) itemView.findViewById(R.id.userName);
        content= (TextView) itemView.findViewById(R.id.content);
        likeCount= (TextView) itemView.findViewById(R.id.likeCount);
        profilePic= (ImageView) itemView.findViewById(R.id.profilePic);

    }
}

package com.RecyclerUtil;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mysampleapp.R;

/**
 * Created by Administrator on 2016-06-02.
 */
public class MeetupRecyclerViewHolder extends RecyclerView.ViewHolder {

    TextView meetUpName;
    TextView meetUpInfo;
    ImageView profilePic;

    public MeetupRecyclerViewHolder(View itemView) {
        super(itemView);

        meetUpName= (TextView) itemView.findViewById(R.id.MeetupName);
        meetUpInfo= (TextView) itemView.findViewById(R.id.MeetupInfo);
        profilePic= (ImageView) itemView.findViewById(R.id.profilePic);

    }
}


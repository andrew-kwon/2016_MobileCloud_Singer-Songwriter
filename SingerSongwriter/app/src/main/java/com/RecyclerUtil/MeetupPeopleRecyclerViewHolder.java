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
public class MeetupPeopleRecyclerViewHolder extends RecyclerView.ViewHolder {

    TextView peopleName;
//    TextView meetUpInfo;
    ImageView profilePic;

    public MeetupPeopleRecyclerViewHolder(View itemView) {
        super(itemView);

        peopleName= (TextView) itemView.findViewById(R.id.my_meetup_people_name);
//        meetUpInfo= (TextView) itemView.findViewById(R.id.MeetupInfo);
        profilePic= (ImageView) itemView.findViewById(R.id.my_meetup_people_propic);

    }
}


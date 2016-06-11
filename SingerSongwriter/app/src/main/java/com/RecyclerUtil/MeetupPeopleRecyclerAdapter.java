package com.RecyclerUtil;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.meetUpfunc.MeetData;
import com.meetUpfunc.MeetPeopleData;
import com.meetUpfunc.MeetUpListActivity;
import com.mysampleapp.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by Administrator on 2016-06-02.
 */
public class MeetupPeopleRecyclerAdapter extends  RecyclerView.Adapter<MeetupPeopleRecyclerViewHolder> {

    List<MeetPeopleData> myMeetPeopleData;
    Context context;
    LayoutInflater inflater;
    public MeetupPeopleRecyclerAdapter(Context context, List<MeetPeopleData> myMeetPeopleData) {
        this.context=context;
        inflater=LayoutInflater.from(context);
        this.myMeetPeopleData=myMeetPeopleData;


    }
    @Override
    public MeetupPeopleRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v=inflater.inflate(R.layout.meetup_people_item, parent, false);

        MeetupPeopleRecyclerViewHolder viewHolder=new MeetupPeopleRecyclerViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MeetupPeopleRecyclerViewHolder holder, int position) {


        holder.peopleName.setText(myMeetPeopleData.get(position).getUserName());

        Picasso.with(MeetUpListActivity.getContext())
                .load("http://52.207.214.66/singersong/data/"+myMeetPeopleData.get(position).getUserID() + ".jpg")
                .into(holder.profilePic);
//        holder.profilePic.setAlpha(90);
    }
    @Override
    public int getItemCount() {
        return myMeetPeopleData.size();
    }
}


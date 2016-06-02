package com.RecyclerUtil;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.meetUpfunc.MeetData;
import com.meetUpfunc.MeetUpListActivity;
import com.mysampleapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 2016-06-02.
 */
public class MeetupRecyclerAdapter extends  RecyclerView.Adapter<MeetupRecyclerViewHolder> {

    List<MeetData> myMeetData;
    Context context;
    LayoutInflater inflater;
    public MeetupRecyclerAdapter(Context context, List<MeetData> songDataList) {
        this.context=context;
        inflater=LayoutInflater.from(context);
        this.myMeetData=songDataList;


    }
    @Override
    public MeetupRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v=inflater.inflate(R.layout.meetup_list_item, parent, false);

        MeetupRecyclerViewHolder viewHolder=new MeetupRecyclerViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MeetupRecyclerViewHolder holder, int position) {

        holder.meetUpName.setText(myMeetData.get(position).getMeetupName());
        holder.content.setText(myMeetData.get(position).getContent());
        Picasso.with(MeetUpListActivity.getContext())
                    .load("http://52.207.214.66/singersong/data/" + myMeetData.get(position).getUserID() + ".jpg")
                    .into(holder.profilePic);
    }
    @Override
    public int getItemCount() {
        return myMeetData.size();
    }
}


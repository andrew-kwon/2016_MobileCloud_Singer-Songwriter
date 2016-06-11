package com.RecyclerUtil;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.meetUpfunc.MeetData;
import com.meetUpfunc.MeetUpListActivity;
import com.mysampleapp.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by Administrator on 2016-06-02.
 */
public class MeetupRecyclerAdapter extends  RecyclerView.Adapter<MeetupRecyclerViewHolder> {

    List<MeetData> myMeetData;
    Context context;
    LayoutInflater inflater;
    public MeetupRecyclerAdapter(Context context, List<MeetData> meetDataList) {
        this.context=context;
        inflater=LayoutInflater.from(context);
        this.myMeetData=meetDataList;


    }
    @Override
    public MeetupRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v=inflater.inflate(R.layout.meetup_list_item, parent, false);

        MeetupRecyclerViewHolder viewHolder=new MeetupRecyclerViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MeetupRecyclerViewHolder holder, int position) {

        String meetInfo = myMeetData.get(position).getMeetupName();
        holder.meetUpName.setText(meetInfo);
        holder.meetUpInfo.setText(""+myMeetData.get(position).getCountPeople()+" Members");
        String setMeetName="";
        try {
            setMeetName = URLEncoder.encode(myMeetData.get(position).getMeetupName(), "UTF-8");
        }catch (IOException e) {
            Log.v("AUDIOHTTPPLAYER", e.getMessage());
        }
        setMeetName=setMeetName.replaceAll("%","%25");
        Picasso.with(MeetUpListActivity.getContext())
                    .load("http://52.207.214.66/meetUp/meetUpData/"+ setMeetName + "_" + myMeetData.get(position).getUserID() + ".jpg")
                    .into(holder.profilePic);
//        holder.profilePic.setAlpha(90);
    }
    @Override
    public int getItemCount() {
        return myMeetData.size();
    }
}


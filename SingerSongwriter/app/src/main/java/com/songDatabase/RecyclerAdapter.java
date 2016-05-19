package com.songDatabase;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mysampleapp.R;

import java.util.List;

/**
 * Created by kundan on 10/26/2015.
 */
public class RecyclerAdapter extends  RecyclerView.Adapter<RecyclerViewHolder> {

    List<songData> mySongData;
    Context context;
    LayoutInflater inflater;
    public RecyclerAdapter(Context context, List<songData> songDataList) {
        this.context=context;
        inflater=LayoutInflater.from(context);
        this.mySongData=songDataList;


    }
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v=inflater.inflate(R.layout.list_item, parent, false);

        RecyclerViewHolder viewHolder=new RecyclerViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {

        holder.songName.setText(mySongData.get(position).getSongName());
        holder.userName.setText(mySongData.get(position).getUserName());
        holder.content.setText(mySongData.get(position).getContent());
        holder.likeCount.setText(mySongData.get(position).getLikeCount());

//        holder.profilePic.setOnClickListener(clickListener);
        holder.profilePic.setImageBitmap(mySongData.get(position).getProfilePic());
//        holder.profilePic.setTag(holder);

    }
//
//    View.OnClickListener clickListener=new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//
//            RecyclerViewHolder vholder = (RecyclerViewHolder) v.getTag();
//            int position = vholder.getPosition();
//
//            Toast.makeText(context,"This is position "+position,Toast.LENGTH_LONG ).show();
//
//        }
//    };
//


    @Override
    public int getItemCount() {
        return mySongData.size();
    }
}
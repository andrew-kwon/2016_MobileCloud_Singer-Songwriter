package com.RecyclerUtil;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mysampleapp.MainActivity;
import com.mysampleapp.R;
import com.songDatabase.LikeListViewActivity;
import com.songDatabase.SongListViewActivity;
import com.songDatabase.songData;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by kundan on 10/26/2015.
 */
public class SongRecyclerAdapter extends  RecyclerView.Adapter<SongRecyclerViewHolder> {

    List<songData> mySongData;
    Context context;
    LayoutInflater inflater;
    public SongRecyclerAdapter(Context context, List<songData> songDataList) {
        this.context=context;
        inflater=LayoutInflater.from(context);
        this.mySongData=songDataList;


    }
    @Override
    public SongRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v=inflater.inflate(R.layout.song_list_item, parent, false);

        SongRecyclerViewHolder viewHolder=new SongRecyclerViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SongRecyclerViewHolder holder, int position) {

        holder.songName.setText(mySongData.get(position).getSongName());
        holder.userName.setText(mySongData.get(position).getUserName());
        holder.content.setText(mySongData.get(position).getContent());
        holder.likeCount.setText(mySongData.get(position).getLikeCount());

//        holder.profilePic.setOnClickListener(clickListener);
//        holder.profilePic.setImageBitmap(mySongData.get(position).getProfilePic());
        if(MainActivity.UserIDClass.getRecyclerAdapterType().equals("SongList")) {
            Picasso.with(SongListViewActivity.getContext())
                    .load("http://52.207.214.66/singersong/data/" + mySongData.get(position).getUserID() + ".jpg")
                    .into(holder.profilePic);
        }
        else if(MainActivity.UserIDClass.getRecyclerAdapterType().equals("LikeList"))
        { Picasso.with(LikeListViewActivity.getLikeListContect())
                .load("http://52.207.214.66/singersong/data/" + mySongData.get(position).getUserID() + ".jpg")
                .into(holder.profilePic);

        }
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

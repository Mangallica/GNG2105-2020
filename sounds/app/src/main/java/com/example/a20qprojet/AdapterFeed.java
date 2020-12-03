package com.example.a20qprojet;


import android.content.Context;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.icu.util.GregorianCalendar;
import android.os.Build;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by karsk on 25/04/2018.
 */

public class AdapterFeed extends RecyclerView.Adapter<AdapterFeed.MyViewHolder> implements View.OnClickListener {

    Context context;
    ArrayList<EventPost> modelFeedArrayList = new ArrayList<>();
    RequestManager glide;
    private  OnEventListener mOnEventListener;

    public AdapterFeed(Context context, ArrayList<EventPost> modelFeedArrayList,OnEventListener onEventListener) {

        this.context = context;
        this.modelFeedArrayList = modelFeedArrayList;
        glide = Glide.with(context);
        this.mOnEventListener=onEventListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.eventfeed, parent, false);
        view.setOnClickListener(this);

        MyViewHolder viewHolder = new MyViewHolder(view,mOnEventListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final EventPost modelFeed = modelFeedArrayList.get(position);

        holder.tv_name.setText(modelFeed.getName());
        holder.tv_time.setText(modelFeed.getTime());
        holder.tv_likes.setText(String.valueOf(modelFeed.getLikes()));
        holder.tv_comments.setText(modelFeed.getComments() + " comments");
        holder.tv_status.setText(modelFeed.getStatus());

        if(modelFeed.getUrl()!=null){
            Picasso.get().load(modelFeed.getUrl()).into(holder.imgView_postPic);
        }

        //glide.load(modelFeed.getPropic()).into(holder.imgView_proPic);

        /*if (modelFeed.getPostpic() == 0) {
            holder.imgView_postPic.setVisibility(View.GONE);
        } else {
            holder.imgView_postPic.setVisibility(View.VISIBLE);
            glide.load(modelFeed.getPostpic()).into(holder.imgView_postPic);
        }*/
    }

    @Override
    public int getItemCount() {
        return modelFeedArrayList.size();
    }

    @Override
    public void onClick(View view) {

    }


    public long getEventId(int position){
        return modelFeedArrayList.get(position).id;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tv_name, tv_time, tv_likes, tv_comments, tv_status;
        ImageView imgView_proPic, imgView_postPic;
        RelativeLayout b;

        OnEventListener onEventListener;

        public MyViewHolder(View itemView,OnEventListener onEventListener) {
            super(itemView);

            this.onEventListener= onEventListener;
            imgView_proPic = (ImageView) itemView.findViewById(R.id.profilePic);

            imgView_postPic = (ImageView) itemView.findViewById(R.id.imgView_postPic);

            tv_name = (TextView) itemView.findViewById(R.id.user_name);
            tv_time = (TextView) itemView.findViewById(R.id.post_time);
            tv_likes = (TextView) itemView.findViewById(R.id.tv_like);
            tv_comments = (TextView) itemView.findViewById(R.id.tv_comment);
            tv_status = (TextView) itemView.findViewById(R.id.event_details);
            b=(RelativeLayout) itemView.findViewById(R.id.addtocalendarbutton);
            b.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }


        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onClick(View view) {

                 onEventListener.onEventClick(getAdapterPosition());
                 if(view.getId()==R.id.addtocalendarbutton){
                     addToCalendar();
                     Toast.makeText(view.getContext(),"added to Calendar "+getAdapterPosition(),Toast.LENGTH_LONG).show();
                 }

        }
        @RequiresApi(api = Build.VERSION_CODES.N)
        public void addToCalendar(){
            Intent calIntent = new Intent(Intent.ACTION_INSERT);
            calIntent.setType("vnd.android.cursor.item/event");
            calIntent.putExtra(CalendarContract.Events.TITLE, "My House Party");
            calIntent.putExtra(CalendarContract.Events.EVENT_LOCATION, "My Beach House");
            calIntent.putExtra(CalendarContract.Events.DESCRIPTION, "A Pig Roast on the Beach");

            GregorianCalendar calDate = new GregorianCalendar(2012, 7, 15);
            calIntent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);
            calIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                    calDate.getTimeInMillis());
            calIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
                    calDate.getTimeInMillis());

            context.startActivity(calIntent);
        }
    }

    public interface OnEventListener{

        void onEventClick(int position);

    }
}


package com.example.a20qprojet;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ConversationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected Activity activity;
    private int SELF = 100;
    private ArrayList<Message> messageArrayList;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == SELF) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_item_self, parent, false);
        } else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_item_other, parent, false);
        }
        return new ViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messageArrayList.get(position);
        if (message.getId() != null && message.getId().equals("1")) {
            return SELF;
        }
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messageArrayList.get(position);
        switch (message.type) {
            case TEXT:
                ((ChatBotAdapter.ViewHolder) holder).message.setText(message.getMessage());
                break;
            case IMAGE:
                ((ChatBotAdapter.ViewHolder) holder).message.setVisibility(View.GONE);
                ImageView iv = ((ChatBotAdapter.ViewHolder) holder).image;
                Glide
                        .with(iv.getContext())
                        .load(message.getUrl())
                        .into(iv);
        }
    }

    @Override
    public int getItemCount() { return messageArrayList.size(); }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView message;
        ImageView image;

        public ViewHolder(View view) {
            super(view);
            message = (TextView) itemView.findViewById(R.id.message);
            image = (ImageView) itemView.findViewById(R.id.image);

            //TODO: Uncomment this if you want to use a custom Font
            /*String customFont = "Montserrat-Regular.ttf";
            Typeface typeface = Typeface.createFromAsset(itemView.getContext().getAssets(), customFont);
            message.setTypeface(typeface);*/

        }
    }
}

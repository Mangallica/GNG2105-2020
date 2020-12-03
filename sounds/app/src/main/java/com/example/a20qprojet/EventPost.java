package com.example.a20qprojet;

import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import static android.content.ContentValues.TAG;

/**
 * Created by karsk on 25/04/2018.
 */

public class EventPost {

    long  likes, comments, postpic;
    long id, propic;
    String name, time, status,imageUrl,postType;
    Uri imgView;

    public EventPost(){

    }

    public EventPost (EventPost p ){
        this.id = p.id;
        this.postType=p.postType;
        this.imageUrl=p.imageUrl;
        this.likes = p.likes;
        this.comments = p.comments;
        this.propic = p.propic;
        this.postpic = p.postpic;
        this.name = p.name;
        this.time = p.time;
        this.status = p.status;
    }

    public EventPost( long id, long propic,String name, String time, String status,String imageUrl,String postType) {
        this.id = id;
        this.postType=postType;
        this.imageUrl=imageUrl;
        this.likes = likes;
        this.comments = comments;
        this.propic = propic;
        this.postpic = postpic;
        this.name = name;
        this.time = time;
        this.status = status;

    }

    public EventPost( long id, long propic,String name, String time, String status,String postType) {
        this.id = id;
        this.postType=postType;
        this.likes = likes;
        this.comments = comments;
        this.propic = propic;
        this.postpic = postpic;
        this.name = name;
        this.time = time;
        this.status = status;

    }

    public EventPost(long id, long propic, String name, String time, String status, String imageUrl, String postType, Uri imgView) {
        this.id = id;
        this.postType=postType;
        this.likes = likes;
        this.comments = comments;
        this.propic = propic;
        this.postpic = postpic;
        this.name = name;
        this.time = time;
        this.status = status;
        this.imgView=imgView;
    }

    public long getId() {
        return id;
    }

    public void setId(String type) {
        this.postType = postType;
    }

    public String getType() {
        return postType;
    }

    public void setType(long id) {
        this.id = id;
    }

    public long getLikes() {
        return likes;
    }

    public void setLikes(long likes) {
        this.likes = likes;
    }

    public long getComments() {
        return comments;
    }

    public String getImageUrl(){
        return imageUrl;
    }

    public void setComments(long comments) {
        this.comments = comments;
    }

    public long getPropic() {
        return propic;
    }

    public void setPropic(long propic) {
        this.propic = propic;
    }

    public long getPostpic() {
        return postpic;
    }

    public void setPostpic(long postpic) {
        this.postpic = postpic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public String getUrl() {
        return this.imageUrl;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String toString(){return this.id+"/"+this.name+"/"+this.postType+"/"+this.time;}
}

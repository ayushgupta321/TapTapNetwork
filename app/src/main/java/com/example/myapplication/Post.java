package com.example.myapplication;

public class Post {

     private int post_id;
     private String post_user_name,post_text,img_url;


    public Post(int postId,String username,String postText,String imgsrc)
    {
        this.img_url = imgsrc;
        this.post_id = postId;
        this.post_text = postText;
        this.img_url = username;

    }
    public Post()
    {

    }

    public String getPost_user_name()
    {
        return this.post_user_name;
    }
    public String getPost_text()
    {
        return this.post_text;
    }

    public String getImg_url()
    {
        return this.img_url;
    }

    public int getPost_id()
    {
        return this.post_id;
    }





}

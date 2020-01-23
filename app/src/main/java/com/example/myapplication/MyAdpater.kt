package com.example.myapplication

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(private val list: List<Post>): RecyclerView.Adapter<PostHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val inflater = LayoutInflater.from(parent.context)
        return PostHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        val post: Post = list[position]
        holder.bind(post)
    }

    override fun getItemCount(): Int = list.size



}

class PostHolder(inflater: LayoutInflater, parent: ViewGroup):
RecyclerView.ViewHolder(inflater.inflate(R.layout.list_item, parent, false)) {

    private var user: TextView? = null
    private var post_text: TextView? = null
    private var post_image: ImageView? = null


    init {
        user = itemView.findViewById(R.id.post_user)
        post_text = itemView.findViewById(R.id.post_text)
        post_image = itemView.findViewById(R.id.post_image)
    }

    fun bind(post: Post) {
        user?.text = post.post_user_name
        post_text?.text = post.post_text
        GlideApp.with(itemView.context).load(post.img_url).into(post_image!!)

    }

}
package com.example.myapplication


import android.os.Bundle

import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*


/**
 * A simple [Fragment] subclass.
 */


data class Post(val postId: Int,val username: String,val postText: String,val imgsrc: Int)




class Home : Fragment() {




    private val posts = listOf<Post>(
             Post(0, "User 1","This is my first post",R.mipmap.ss1),
             Post(1,"User 2","Yay! I won my tournament",R.mipmap.ss2)
    )


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        val view = inflater.inflate(R.layout.fragment_home, container, false)


        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler_view.apply { layoutManager = LinearLayoutManager(activity)
            adapter = MyAdapter(posts)
        }
    }



}// Required empty public constructor

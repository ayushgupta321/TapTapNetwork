package com.example.myapplication


import android.content.Context
import android.os.Bundle
import android.util.Log

import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
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








class Home : Fragment() {


    public var posts = listOf<Post>(
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val btn1 = view.findViewById<Button>(R.id.create)

        btn1.setOnClickListener { findNavController(nav_host_fragment).navigate(R.id.action_home2_to_create_post) }




        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val datab = FirebaseFirestore.getInstance()

        val query = datab.collection("posts").limit(5).get().addOnSuccessListener { result ->

            val newposts = result.toObjects<Post>()

            posts += newposts

            Log.i("my","Aa toh raha hai")

            recycler_view.apply { layoutManager = LinearLayoutManager(activity)
                adapter = MyAdapter(posts)



            }
        }
    }



}// Required empty public constructor

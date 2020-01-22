package com.example.myapplication


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main_profile.*

/**
 * A simple [Fragment] subclass.
 */
class MainProfile : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        val datab = FirebaseFirestore.getInstance()

        val users = datab.collection("users").whereEqualTo("level",1).get().addOnSuccessListener { result ->
            val show = result?.toObjects<Users>()
            val res = show?.get(0)

            UserName.text = res?.name
            level_text.text = res?.level.toString()
            email_text.text = res?.email

        }





        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_main_profile, container, false)

        view.findViewById<Button>(R.id.capture_button).setOnClickListener { findNavController(nav_host_fragment).navigate(R.id.action_mainProfile_to_profile)}

        return view
    }


}

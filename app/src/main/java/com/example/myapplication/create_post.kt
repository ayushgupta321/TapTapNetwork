package com.example.myapplication


import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_create_post.*
import java.io.File

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [create_post.newInstance] factory method to
 * create an instance of this fragment.
 */
class create_post : Fragment() {
    // TODO: Rename and change types of parameters

    val FILE_SELECT_CODE =1

    var uri_img:Uri = Uri.parse("null")
    var downloadUri:String = "null"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_create_post, container, false)

        val uploadimg1 = view.findViewById<ImageButton>(R.id.uploadimg)

        uploadimg1.setOnClickListener {



            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            intent.addCategory(Intent.CATEGORY_OPENABLE)

            try {
                startActivityForResult(
                        Intent.createChooser(intent, "Select a File to Upload"),
                        FILE_SELECT_CODE
                )
            } catch (ex: ActivityNotFoundException) { // Potentially direct the user to the Market with a Dialog
                Toast.makeText(
                        activity, "Please install a File Manager.",
                        Toast.LENGTH_SHORT
                ).show()
            }

        }



        val uploadbtn1 = view.findViewById<Button>(R.id.uploadbtn)

        uploadbtn1.setOnClickListener {

            val storage = FirebaseStorage.getInstance()
            val storageref = storage.reference
            val imgref = storageref.child("post_images/${uri_img.lastPathSegment}")
            val uploadTask = imgref.putFile(uri_img)

            val urlTask = uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                imgref.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                     downloadUri = task.result.toString()



                    val datab = FirebaseFirestore.getInstance()

                    val data = hashMapOf<String,Any>(
                            "img_url" to downloadUri,
                            "post_id" to 2,
                            "post_text" to post_text1.text.toString(),
                            "post_user_name" to "Ayush"
                    )

                    datab.collection("posts").document().set(data).addOnSuccessListener { Log.d("my", "DocumentSnapshot successfully written!")
                        findNavController(nav_host_fragment).navigate(R.id.create_to_home)

                    }
                            .addOnFailureListener { e -> Log.w("my", "Error writing document", e) }




                } else {
                    // Handle failures
                    // ...
                }
            }



        }


        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {


        when (requestCode) {
            FILE_SELECT_CODE -> if (resultCode == Activity.RESULT_OK) { // Get the Uri of the selected file
                val uri: Uri? = data?.data

                uri_img = uri!!
                // Get the path



            }
        }

        super.onActivityResult(requestCode, resultCode, data)


    }



}

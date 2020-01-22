package com.example.myapplication


import android.os.Bundle

import androidx.fragment.app.Fragment


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.util.Size
import android.graphics.Matrix
import android.os.Environment
import android.util.Log
import android.view.*
import android.widget.ImageButton
import android.widget.Toast
import androidx.camera.core.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import java.io.File
import java.util.concurrent.Executors
import androidx.camera.*
import java.util.concurrent.TimeUnit


// This is an arbitrary number we are using to keep track of the permission
// request. Where an app has multiple context for requesting permission,
// this can help differentiate the different contexts.
private const val REQUEST_CODE_PERMISSIONS = 10

// This is an array of all the permission specified in the manifest.
private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
private var lensFacing = CameraX.LensFacing.BACK
private var imageCapture: ImageCapture? = null
private var preview: Preview? = null




/**
 * A simple [Fragment] subclass.
 */
class profile : Fragment(), LifecycleOwner {

    var  MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0





    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment\

        val v = inflater.inflate(R.layout.fragment_profile, container, false)

        viewFinder = v.findViewById(R.id.view_finder)

        // Request camera permissions
        if (allPermissionsGranted()) {
            viewFinder.post { startCamera() }
        } else {
            ActivityCompat.requestPermissions(
                    context as Activity, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        // Every time the provided texture view changes, recompute layout
        viewFinder.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            updateTransform()
        }


        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(context as Activity,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(context as Activity,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(context as Activity,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS)

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }







        return v


    }

    private val executor = Executors.newSingleThreadExecutor()
    private lateinit var viewFinder: TextureView


    private fun updateTransform() {
        val matrix = Matrix()

        // Compute the center of the view finder
        val centerX = viewFinder.width / 2f
        val centerY = viewFinder.height / 2f

        // Correct preview output to account for display rotation
        val rotationDegrees = when (viewFinder.display.rotation) {
            Surface.ROTATION_0 -> 0
            Surface.ROTATION_90 -> 90
            Surface.ROTATION_180 -> 180
            Surface.ROTATION_270 -> 270
            else -> return
        }
        matrix.postRotate(-rotationDegrees.toFloat(), centerX, centerY)

        // Finally, apply transformations to our TextureView
        viewFinder.setTransform(matrix)
    }

        /**
     * Process result from permission request dialog box, has the request
     * been granted? If yes, start Camera. Otherwise display a toast
     */
    override fun onRequestPermissionsResult(
            requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                viewFinder.post { startCamera() }
            } else {
                Toast.makeText(context,"Permissions not granted by the user.",Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Check if all permission specified in the manifest have been granted
     */
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission( activity as Context, it) == PackageManager.PERMISSION_GRANTED
    }


    @SuppressLint("RestrictedApi")
    private fun startCamera() {




        // Create configuration object for the viewfinder use case
        val previewConfig = PreviewConfig.Builder().apply {
            setLensFacing(lensFacing)
            setTargetResolution(Size(640, 480))
        }.build()


        // Build the viewfinder use case
        val preview = Preview(previewConfig)

        // Every time the viewfinder is updated, recompute layout
        preview.setOnPreviewOutputUpdateListener {

            // To update the SurfaceTexture, we have to remove it and re-add it
            val parent = viewFinder.parent as ViewGroup
            parent.removeView(viewFinder)
            parent.addView(viewFinder, 0)

            viewFinder.surfaceTexture = it.surfaceTexture
            updateTransform()
        }


        /*val viewFinderConfig = PreviewConfig.Builder().apply {
            setLensFacing(lensFacing)
            // We request aspect ratio but no resolution to let CameraX optimize our use cases
            // Set initial target rotation, we will have to call this again if rotation changes
            // during the lifecycle of this use case
        }.build()
*/



        // Bind use cases to lifecycle
        // If Android Studio complains about "this" being not a LifecycleOwner
        // try rebuilding the project or updating the appcompat dependency to
        // version 1.1.0 or higher.


        val imageCaptureConfig = ImageCaptureConfig.Builder()
                .apply {
                    // We don't set a resolution for image capture; instead, we
                    // select a capture mode which will infer the appropriate
                    // resolution based on aspect ration and requested mode
                    setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY)
                }.build()

        // Build the image capture use case and attach button click listener
        val imageCapture = ImageCapture(imageCaptureConfig)

        view?.findViewById<ImageButton>(R.id.imageButton2)?.setOnClickListener {
            val file = File(Environment.getExternalStorageDirectory(),
                    "${System.currentTimeMillis()}.jpg")
            imageCapture.takePicture(file, executor,
                    object : ImageCapture.OnImageSavedListener {
                        override fun onError(
                                imageCaptureError: ImageCapture.ImageCaptureError,
                                message: String,
                                exc: Throwable?
                        ) {
                            val msg = "Photo capture failed: $message"
                            Log.e("CameraXApp", msg, exc)
                            viewFinder.post {
                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onImageSaved(file: File) {
                            val msg = "Photo capture Succeeded, Stored at: ${file.absolutePath}"
                            Log.d("CameraXApp", msg)
                            viewFinder.post {
                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                            }
                        }
                    })
        }


        view?.findViewById<ImageButton>(R.id.imageButton)?.setOnClickListener{
            lensFacing = if (CameraX.LensFacing.FRONT == lensFacing) {
                CameraX.LensFacing.BACK
            } else {
                CameraX.LensFacing.FRONT
            }
            try {
                // Only bind use cases if we can query a camera with this orientation
                CameraX.getCameraWithLensFacing(lensFacing)

                // Unbind all use cases and bind them again with the new lens facing configuration
                CameraX.unbindAll()
                startCamera()
            } catch (exc: Exception) {
                // Do nothing
            }

        }

        // Bind use cases to lifecycle
        // If Android Studio complains about "this" being not a LifecycleOwner
        // try rebuilding the project or updating the appcompat dependency to


        CameraX.bindToLifecycle(this, preview,imageCapture)
    }




}// Required empty public constructor

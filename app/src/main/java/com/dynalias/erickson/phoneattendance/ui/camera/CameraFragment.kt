package com.dynalias.erickson.phoneattendance.ui.camera

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.dynalias.erickson.phoneattendance.R
import com.dynalias.erickson.phoneattendance.databinding.CameraLayoutBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class CameraFragment : Fragment() {

    private var _binding: CameraLayoutBinding? = null
    private lateinit var safeContext: Context
    private lateinit var cameraExecutor: ExecutorService

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
        safeContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.camera_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startCamera()
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

private fun startCamera() {
    val cameraProviderFuture = ProcessCameraProvider.getInstance(safeContext)
    val previewView = this.view?.findViewById<PreviewView>(R.id.title_camera);

    cameraProviderFuture.addListener({
        // Used to bind the lifecycle of cameras to the lifecycle owner
        val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

        // Preview
        val preview = Preview.Builder()
            .build()
            .also {
                it.setSurfaceProvider(previewView?.surfaceProvider)
            }

        // Select back camera as a default
        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        try {
            // Unbind use cases before rebinding
            cameraProvider.unbindAll()

            // Bind use cases to camera
            cameraProvider.bindToLifecycle(
                this, cameraSelector, preview)

        } catch(exc: Exception) {
            Log.e("PhoneAttendance", "Use case binding failed", exc)
        }

    }, ContextCompat.getMainExecutor(safeContext))
}

    override fun onDestroyView() {
        super.onDestroyView()
        cameraExecutor.shutdown()
    }
}
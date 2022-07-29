package com.dynalias.erickson.phoneattendance.ui.camera

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.dynalias.erickson.phoneattendance.MainActivity
import com.dynalias.erickson.phoneattendance.databinding.CameraLayoutBinding
import com.dynalias.erickson.phoneattendance.services.BarcodeAnalyzer
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean
import com.dynalias.erickson.phoneattendance.R
import com.dynalias.erickson.phoneattendance.ui.absent.AbsentFragment


class CameraFragment : Fragment() {

    private val processingBarcode = AtomicBoolean(false)
    private var _binding: CameraLayoutBinding? = null
    private lateinit var safeContext: Context
    private lateinit var cameraExecutor: ExecutorService

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    //Used for information update
    private var className: String = ""
    private var classRoster = mutableMapOf<Int, String>()
    private var maxClassId: Int = 0
    private var absentSet = mutableSetOf<String>()




    override fun onAttach(context: Context) {
        super.onAttach(context)
        safeContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = CameraLayoutBinding.inflate(inflater, container, false)
        val root: View = binding.root
        className = "AS Period 1"
        classRoster.putAll(setOf(1 to "Katie", 2 to "Student 2", 3 to "Student 3", 4 to "Student 4"))
        maxClassId = classRoster.size
        setScanText()

        binding.doneButton.setOnClickListener {
            val fragment: Fragment = AbsentFragment.newInstance(className,
                convert(absentSet) as ArrayList<String>)
            val myactivity = activity as MainActivity
          //findNavController().navigate()
           myactivity.switchToSecondFragment(fragment)
           //parentFragmentManager.beginTransaction().replace(R.id.main_cam_layout, fragment).hide(this).commit()
        }

        return root
    }

    fun <T> convert(set: Set<T>): List<T> {
        val list: MutableList<T> = ArrayList()
        list.addAll(set);
        return list
    }

    private fun setScanText(){
        //Period 1 --- Total: 14 Scans: 10
        val msg = className + " --- Total: "+ classRoster.size + " Scans: " + absentSet.size
        val scansInfo: TextView = binding.scansInfo
        scansInfo.text = msg
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startCamera()
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

private fun startCamera() {
    val cameraProviderFuture = ProcessCameraProvider.getInstance(safeContext)
    val previewView = binding.titleCamera;

    cameraProviderFuture.addListener({
        // Used to bind the lifecycle of cameras to the lifecycle owner
        val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

        // Preview
        val preview = Preview.Builder()
            .build()
            .also {
                it.setSurfaceProvider(previewView?.surfaceProvider)
            }
        // Setup the ImageAnalyzer for the ImageAnalysis use case
        val imageAnalysis = ImageAnalysis.Builder()
            .build()
            .also {
                it.setAnalyzer(cameraExecutor, BarcodeAnalyzer { barcode ->
                        Log.i("PhoneAttendance", barcode)
                        if(barcode.toInt() <= maxClassId) {
                            val student = classRoster.get(barcode.toInt())
                            if(student != null){
                                absentSet.add(student)
                                setScanText()
                            }
                        }
                })
            }

        // Select back camera as a default
        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        try {
            // Unbind use cases before rebinding
            cameraProvider.unbindAll()

            // Bind use cases to camera
            cameraProvider.bindToLifecycle(
                this, cameraSelector, preview, imageAnalysis)

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
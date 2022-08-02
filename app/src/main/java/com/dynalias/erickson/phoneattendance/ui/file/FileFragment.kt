package com.dynalias.erickson.phoneattendance.ui.file

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.ListFragment
import androidx.navigation.fragment.findNavController
import com.dynalias.erickson.phoneattendance.databinding.FragmentFileBinding
import java.io.File


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "file_name"
private const val ARG_PARAM2 = "file_list"

/**
 * A simple [Fragment] subclass.
 * Use the [FileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class FileFragment : ListFragment() {
    private var fileName: String? = null
    private var _binding: FragmentFileBinding? = null
    private var fileArr:Array<out String> ?= null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            fileName = it.getString(ARG_PARAM1)
            fileArr = it.getStringArray(ARG_PARAM2)
        }

        val nvctrl = this.findNavController()
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    val action = FileFragmentDirections.actionBackToEdit()
                    nvctrl.navigate(action)

                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        var fileList = ArrayList<String>()
        if(fileList != null) {
            fileList = fileArr?.toCollection(ArrayList()) as ArrayList<String>
        }
        //set className
        binding.fileName.text = fileName

        //add adapter
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(inflater.context, android.R.layout.simple_list_item_1, fileList)
        setListAdapter(adapter)

        //Add Remove Button
        binding.remove.setOnClickListener() {
            activity?.deleteFile(fileName)
            val action = FileFragmentDirections.actionBackToEdit()
            this.findNavController().navigate(action)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FileFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: ArrayList<String>) =
            FileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putStringArrayList(ARG_PARAM2, param2)
                }
            }
    }
}
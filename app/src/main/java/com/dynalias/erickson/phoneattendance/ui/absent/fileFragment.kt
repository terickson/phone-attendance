package com.dynalias.erickson.phoneattendance.ui.absent

import android.R
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.ListFragment
import androidx.navigation.fragment.findNavController
import com.dynalias.erickson.phoneattendance.databinding.FragmentAbsentBinding
import com.dynalias.erickson.phoneattendance.ui.camera.CameraFragmentDirections


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "class_name"
private const val ARG_PARAM2 = "absent_list"

/**
 * A simple [Fragment] subclass.
 * Use the [AbsentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class AbsentFragment : ListFragment() {
    private var className: String? = null
    private var _binding: FragmentAbsentBinding? = null
    private var absentArr:Array<out String> ?= null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            className = it.getString(ARG_PARAM1)
            absentArr = it.getStringArray(ARG_PARAM2)
        }

        val nvctrl = this.findNavController()
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    val action = AbsentFragmentDirections.actionBackToHome()
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
        _binding = FragmentAbsentBinding.inflate(inflater, container, false)
        val root: View = binding.root
        var absentList = ArrayList<String>()
        if(absentList != null) {
            absentList = absentArr?.toCollection(ArrayList()) as ArrayList<String>
        }
        //set className
        binding.className.text = className

        //add adapter
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(inflater.context, android.R.layout.simple_list_item_1, absentList)
        setListAdapter(adapter)

        //Add Share Button
        binding.share.setOnClickListener() {
            val sendIntent: Intent = Intent().apply {
                var body = ""
                for(student in absentList){
                    body += student+ "<br/>"
                }
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_SUBJECT, "ABSENT: " + className)
                putExtra(Intent.EXTRA_TEXT,Html.fromHtml(body, 0))
                type = "text/html"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
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
         * @return A new instance of fragment AbsentFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: ArrayList<String>) =
            AbsentFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putStringArrayList(ARG_PARAM2, param2)
                }
            }
    }
}
package com.dynalias.erickson.phoneattendance.ui.home

import android.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.ListFragment
import androidx.navigation.fragment.findNavController
import com.dynalias.erickson.phoneattendance.databinding.FragmentHomeBinding


class HomeFragment : ListFragment() {

    private var _binding: FragmentHomeBinding? = null
    private var classList = ArrayList<String>()
    private var classData = mutableMapOf<String, Array<String>>()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //TODO These will be replaced by csv data in the long run
        classList.addAll(listOf("Period 1", "Period 2", "Period 4", "Period 8") )
        for(cl in classList) {
            var roster = mutableListOf<String>()
            for(i in 1..cl.length)
            {
                roster.add("Student "+i)
            }
            classData.put(cl, roster.toTypedArray())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(inflater.context, R.layout.simple_list_item_1, classList)
        setListAdapter(adapter)

        return root
    }

    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        super.onListItemClick(l, v, position, id)
        val  className = this.listAdapter?.getItem(position) as String
        Log.i("PhoneAttendance", className)
        val action = HomeFragmentDirections.actionHomeToCamera(className, classData.get(className) as Array<out String>)
        this.findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
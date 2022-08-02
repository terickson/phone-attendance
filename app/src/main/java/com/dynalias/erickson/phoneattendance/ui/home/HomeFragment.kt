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
import java.io.File
import java.io.FileInputStream
import java.io.IOException


class HomeFragment : ListFragment() {

    private var _binding: FragmentHomeBinding? = null
    private var classList = ArrayList<String>()
    private var fileList = ArrayList<String>()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        fileList = ArrayList<String>()
        classList = ArrayList<String>()
        val files = activity?.filesDir?.listFiles() as Array<File>
        for (file in files) {
            val fileName = file.name as String
            val className = fileName.replaceFirstChar{ it.uppercase() }.replace("_", " ").replace(".csv","")
            fileList.add(fileName)
            classList.add(className)
        }


        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(inflater.context, R.layout.simple_list_item_1, classList)
        setListAdapter(adapter)

        return root
    }

    @Throws(IOException::class)
    fun FileInputStream.readAsCSV() : List<List<String>> {
        val splitLines = mutableListOf<List<String>>()
        reader().buffered().forEachLine {
            splitLines += it.split(", ")
        }
        return splitLines
    }

    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        super.onListItemClick(l, v, position, id)
        val  className = this.listAdapter?.getItem(position) as String
        val  fileName = fileList?.get(position)
        Log.i("PhoneAttendance", className)
        var studentData = mutableListOf<String>()
        val rows = activity?.openFileInput(fileName)?.readAsCSV() as List<List<String>>
        for(row in rows){
            studentData.add(row.get(0))
        }
        val action = HomeFragmentDirections.actionHomeToCamera(className, studentData.toTypedArray())
        this.findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
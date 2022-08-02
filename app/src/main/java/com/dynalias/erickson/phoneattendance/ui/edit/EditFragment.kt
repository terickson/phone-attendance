package com.dynalias.erickson.phoneattendance.ui.edit

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
import com.dynalias.erickson.phoneattendance.databinding.FragmentEditBinding
import java.io.File
import java.io.FileInputStream
import java.io.IOException


class EditFragment : ListFragment() {

    private var _binding: FragmentEditBinding? = null
    private var fileList = ArrayList<String>()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEditBinding.inflate(inflater, container, false)
        val root: View = binding.root

        fileList = ArrayList<String>()
        val files = activity?.filesDir?.listFiles() as Array<File>
        for (file in files) {
            fileList.add(file.name as String)
        }

        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(inflater.context, R.layout.simple_list_item_1, fileList)
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
        val  fileName = this.listAdapter?.getItem(position) as String
        Log.i("PhoneAttendance", fileName)
        val rows = activity?.openFileInput(fileName)?.readAsCSV() as List<List<String>>
        var studentData = mutableListOf<String>()
        for(row in rows){
            studentData.add(row.get(0))
        }
        val action = EditFragmentDirections.actionEditToFile(fileName, studentData.toTypedArray())
        this.findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.project.mynotes.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.project.mynotes.R

class NoteDetails : Fragment() {


    private  var noteTitle : String? = null
    private  var noteContent : String? = null
    private  var noteId : String? = null

    private lateinit var floatingActionButton: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            noteId = it.getString("docId").toString()
            noteTitle = it.getString("title").toString()
            noteContent = it.getString("content").toString()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val toolBar = view.findViewById<Toolbar>(R.id.toolBarOfNoteDetails)

        toolBar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        floatingActionButton = view.findViewById(R.id.saveNoteFloatingButton1)
        floatingActionButton.setOnClickListener {

            val bundle = Bundle()
            bundle.putString("title",noteTitle)
            bundle.putString("content",noteContent)
            bundle.putString("docId",noteId)

            val editNoteFragment = EditNoteFragment()
            editNoteFragment.arguments = bundle

            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fragment_container_main,editNoteFragment)
                ?.commit()

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_note_details, container, false)
        view.findViewById<TextView>(R.id.noteDetailsTitle).text = noteTitle
        view.findViewById<TextView>(R.id.noteDetailsContent).text = noteContent
        return view
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String, docId: String) =
            NoteDetails().apply {

            }
    }
}
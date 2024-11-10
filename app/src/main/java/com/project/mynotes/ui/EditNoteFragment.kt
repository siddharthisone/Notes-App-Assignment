package com.project.mynotes.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import android.widget.Toolbar
import androidx.activity.OnBackPressedCallback
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.project.mynotes.R
import com.project.mynotes.model.FirebaseModel


class EditNoteFragment : Fragment() {


    private  var noteTitle : String? = null
    private  var noteContent : String? = null
    private  var docId : String? = null

    private lateinit var floatingSaveUpdateNoteBtn : FloatingActionButton
    private lateinit var editNoteTitle : EditText
    private lateinit var editNoteContent : EditText


    private lateinit var firebaseModel: FirebaseModel
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseUser: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

            noteTitle = it.getString("title").toString()
            noteContent = it.getString("content").toString()
            docId = it.getString("docId").toString()

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_edit_note, container, false)
        view.findViewById<EditText>(R.id.editNoteTitle).setText(noteTitle)
        view.findViewById<EditText>(R.id.editNoteContent).setText(noteContent)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editNoteTitle = view.findViewById<EditText>(R.id.editNoteTitle)
        editNoteContent = view.findViewById<EditText>(R.id.editNoteContent)

        auth = FirebaseAuth.getInstance()
        firebaseUser = auth.currentUser!!

        firestore = FirebaseFirestore.getInstance()

        val toolbar = view.findViewById<Toolbar>(R.id.toolBarOfEditNote)
        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        //update note

        floatingSaveUpdateNoteBtn = view.findViewById(R.id.saveNoteFloatingButton)
        floatingSaveUpdateNoteBtn.setOnClickListener {

            noteTitle = editNoteTitle.text.toString()
            noteContent = editNoteContent.text.toString()



            if (noteTitle!!.isEmpty() || noteContent!!.isEmpty())
            {
                Toast.makeText(context, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            }
            else {
                val documentReference = firestore.collection("notes").document(firebaseUser.uid)
                    .collection("myNotes").document(docId!!)

                val updatedNote = mapOf(
                    "title" to noteTitle,
                    "content" to noteContent,
                )
                documentReference.set(updatedNote).addOnSuccessListener {

                }.addOnFailureListener {
                    Toast.makeText(context, "Failed to update note", Toast.LENGTH_SHORT).show()
                }

                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.fragment_container_main,HomeFragment())
                    ?.commit()

            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.fragment_container_main,HomeFragment())
                    ?.commit()
            }
        })
    }


    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EditNoteFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}
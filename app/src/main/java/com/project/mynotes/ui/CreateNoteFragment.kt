package com.project.mynotes.ui

import android.os.Bundle
import android.view.ContentInfo
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import android.widget.Toolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.project.mynotes.R

class CreateNoteFragment : Fragment() {


    private lateinit var createNoteTitle : EditText
    private lateinit var createNoteContent : EditText
    private lateinit var saveNoteFloatingButton : FloatingActionButton

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createNoteTitle = view.findViewById(R.id.createNoteTitle)
        createNoteContent = view.findViewById(R.id.createNoteContent)
        saveNoteFloatingButton = view.findViewById(R.id.saveNoteFloatingButtonCreate)


        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        firebaseUser = FirebaseAuth.getInstance().currentUser!!



        val toolbar = view.findViewById<Toolbar>(R.id.toolBarOfCreateNote)
        activity?.setActionBar(toolbar)
        activity?.actionBar?.setDisplayHomeAsUpEnabled(true)


        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }


        //SAVE NOTE
        saveNoteFloatingButton.setOnClickListener {

            val title = createNoteTitle.text.toString()
            val content = createNoteContent.text.toString()

            if (title.isEmpty() || content.isEmpty())
            {
                Toast.makeText(context, "Both fields are required", Toast.LENGTH_SHORT).show()
            }
            else{

                val documentReference = firestore.collection("notes")
                    .document(firebaseUser.uid)
                    .collection("myNotes")
                    .document()

                val note = HashMap<String, String>()
                note["title"] = title
                note["content"] = content

                documentReference.set(note as Map<String, Any>).addOnSuccessListener {

                }.addOnFailureListener {
                    Toast.makeText(context, "Failed to create", Toast.LENGTH_SHORT).show()
                }
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.fragment_container_main,HomeFragment())?.commit()
            }

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home)
        {
            requireActivity().onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CreateNoteFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}
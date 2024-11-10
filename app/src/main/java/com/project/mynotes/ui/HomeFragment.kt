package com.project.mynotes.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.project.mynotes.MainActivity
import com.project.mynotes.R
import com.project.mynotes.model.FirebaseModel
import java.util.Random

class HomeFragment() : Fragment() {

    private lateinit var floatingButton : FloatingActionButton
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var recyclerView: RecyclerView
    private lateinit var staggeredGridLayoutManager: StaggeredGridLayoutManager

    private lateinit var firebaseUser: FirebaseUser
    private lateinit var firebaseFireStore: FirebaseFirestore
    private lateinit var noteAdapter : FirestoreRecyclerAdapter<FirebaseModel,NoteViewHolder>



    override  fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)
        firebaseAuth = FirebaseAuth.getInstance()

        firebaseUser = firebaseAuth.currentUser!!

        firebaseFireStore = FirebaseFirestore.getInstance()



        floatingButton = view.findViewById(R.id.floatingButtonAdd)
        floatingButton.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fragment_container_main,CreateNoteFragment())
                ?.addToBackStack(null)
                ?.commit()
        }

        recyclerView = view.findViewById(R.id.notesRecyclerView)
        recyclerView.hasFixedSize()
        recyclerView.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)

       val query:Query = firebaseFireStore.collection("notes")
                         .document(firebaseUser.uid).collection("myNotes")
                         .orderBy("title",Query.Direction.ASCENDING)

        val allUserNotes : FirestoreRecyclerOptions<FirebaseModel> = FirestoreRecyclerOptions
            .Builder<FirebaseModel>().setQuery(query,FirebaseModel::class.java).build()



        noteAdapter = object : FirestoreRecyclerAdapter<FirebaseModel,NoteViewHolder>(allUserNotes)
        {

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
                val views = LayoutInflater.from(parent.context).inflate(R.layout.notes_item,parent,false)
                return NoteViewHolder(views)
            }

            override fun onBindViewHolder(p0: NoteViewHolder, p1: Int, p2: FirebaseModel) {

                p0.noteTitle.text = p2.title
                p0.noteContent.text = p2.content
                val image : ImageView = p0.itemView.findViewById(R.id.imageViewOptions)

                p0.itemView.setOnClickListener {

                    //open note details

                    Toast.makeText(context,"clicked",Toast.LENGTH_SHORT).show()

                    val docId = noteAdapter.snapshots.getSnapshot(p1).id

                    val bundle = Bundle()
                    bundle.putString("docId",docId)
                    bundle.putString("title",p2.title)
                    bundle.putString("content",p2.content)

                    val notesDetailsFragment = NoteDetails()
                    notesDetailsFragment.arguments = bundle

                    activity?.supportFragmentManager?.beginTransaction()
                        ?.replace(R.id.fragment_container_main,notesDetailsFragment)
                        ?.addToBackStack(null)
                        ?.commit()
                }

                image.setOnClickListener {
                    val popUpMenu = PopupMenu(it.context,it)

                    popUpMenu.gravity = Gravity.END
                    popUpMenu.menu.add("Edit").setOnMenuItemClickListener {


                        val docId3 = noteAdapter.snapshots.getSnapshot(p1).id
                        val bundle = Bundle()
                        bundle.putString("title",p2.title)
                        bundle.putString("content",p2.content)
                        bundle.putString("docId",docId3)

                        val editNoteFragment = EditNoteFragment()
                        editNoteFragment.arguments = bundle

                        activity?.supportFragmentManager?.beginTransaction()
                            ?.replace(R.id.fragment_container_main,editNoteFragment)
                            ?.addToBackStack(null)
                            ?.commit()

                        false
                    }
                    popUpMenu.menu.add("Delete").setOnMenuItemClickListener {

                        val docId2  = noteAdapter.snapshots.getSnapshot(p1).id

                        val documentReference = firebaseFireStore.collection("notes")
                            .document(firebaseUser.uid)
                            .collection("myNotes")
                            .document(docId2)

                        documentReference.delete().addOnSuccessListener {
                            noteAdapter.notifyDataSetChanged()
                            }
                            .addOnFailureListener {
                                Toast.makeText(context,"Error Deleting Note",Toast.LENGTH_SHORT).show()
                            }
                        false
                    }
                    popUpMenu.show()
                }

            }

            override fun onDataChanged() {
                super.onDataChanged()
                if (itemCount==0)
                {
                    Toast.makeText(context,"No Notes",Toast.LENGTH_SHORT).show()
                }
            }

        }
        recyclerView.adapter  =  noteAdapter

    }

    private fun getRandomColor(): Int {

        val colorCode  : ArrayList<Int> = arrayListOf()
        colorCode.add(R.color.red)
        colorCode.add(R.color.blue)
        colorCode.add(R.color.pink)
        colorCode.add(R.color.green)
        colorCode.add(R.color.gold)
        colorCode.add(R.color.golden)
        colorCode.add(R.color.lightBlue)

        val random  = Random()
        val number = random.nextInt(colorCode.size)

        return colorCode[number]
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_home,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

       return  when(item.itemId){
             R.id.logout -> {
                 (activity as MainActivity).onLogout()
                 true
             }
           else -> super.onOptionsItemSelected(item)

        }

    }

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val noteTitle: TextView = itemView.findViewById(R.id.notesTitle)
        val noteContent: TextView = itemView.findViewById(R.id.notes_content)
        val mNote: LinearLayout = itemView.findViewById(R.id.note)

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onStart() {
        super.onStart()
        noteAdapter.startListening()
        recyclerView.adapter!!.notifyDataSetChanged()
    }

    override fun onStop() {
        super.onStop()
        if(noteAdapter!=null)
        {
            noteAdapter.stopListening()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String){



        }
    }
}
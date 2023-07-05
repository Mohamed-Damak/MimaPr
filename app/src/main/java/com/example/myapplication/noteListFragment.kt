
package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.view.*
import android.widget.BaseAdapter

import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.room.Update
import kotlinx.android.synthetic.main.fragment_note_list2.*
import kotlinx.android.synthetic.main.noteticket.view.*

class noteListFragment : Fragment() {

    val listNotes = ArrayList<Note>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_note_list2, container, false)
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        querySearch ("%")
    }
    @SuppressLint("Range", "UseRequireInsteadOfGet")
    fun querySearch(noteTitle:String){
        var dbManager=DbManager(this!!.activity!!)
        val projection= arrayOf("ID","Title","Description")
        val selectionArgs= arrayOf(noteTitle)
        val cursor=dbManager.query(projection,"Title like ?",selectionArgs,"Title")
        if(cursor.moveToFirst()){
            listNotes.clear()
            do {
                val id=cursor.getInt(cursor.getColumnIndex("ID"))
                val title=cursor.getString(cursor.getColumnIndex("Title"))
                val description=cursor.getString(cursor.getColumnIndex("Description"))
                listNotes.add(Note(id,title,description))
            }while (cursor.moveToNext())
        }
        var myAdapter=MyNoteAdapter(this!!.activity!!,listNotes)
        lvNotes.adapter=myAdapter

    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.notelist_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    @SuppressLint("UseRequireInsteadOfGet")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item!!.itemId){
            R.id.addnotesbu ->{
                view!!.findNavController().navigate(R.id.action_noteListFragment_to_addNoteFragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }inner class MyNoteAdapter:BaseAdapter{
        var listNoteAdapter=ArrayList<Note>()
        var context:Context?=null
        constructor(context: Context, listNoteAdapter:ArrayList<Note>):super(){
            this.context=context
            this.listNoteAdapter=listNoteAdapter;
        }
        override fun getView(p0: Int, p1: View?, p2: ViewGroup?):View {
            val myView = layoutInflater.inflate(R.layout.noteticket, null)
            val note = listNoteAdapter[p0]
            myView.tvTitle.text = note.nodeTitle
            myView.tvDes.text = note.nodeDes
                      //delete
            myView.ivDelete.setOnClickListener {

                val dbManager = DbManager(this.context!!)
                val selectionArgs = arrayOf(note.nodeID.toString())
                dbManager.delete("ID=?", selectionArgs)
                querySearch("%")
            }
            //edit
          myView.ivEdit.setOnClickListener{
          goToUpdate(note)
            }
            return myView
        }
        override fun getItem(p0: Int): Any {
            return listNoteAdapter[p0]
        }
        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }
        override fun getCount(): Int {
            return listNoteAdapter.size
        }
    }
    @SuppressLint("UseRequireInsteadOfGet")
    fun goToUpdate(note: Note){
        var bundle=Bundle()
        bundle.putInt("ID",note.nodeID!!)
        bundle.putString("Title",note.nodeTitle!!)
        bundle.putString("Description",note.nodeDes!!)
        view!!.findNavController().navigate(R.id.action_noteListFragment_to_addNoteFragment,bundle)
    }
    }
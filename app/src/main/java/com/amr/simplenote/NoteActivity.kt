package com.amr.simplenote

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.note_activity.*
import java.text.SimpleDateFormat
import java.util.*


class NoteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.note_activity)
        receiveData()
    }

    fun addNote() {

        var noteId = mRef.push().key.toString()
        var newNote = LvDataRow(noteId, txtTitle.text.toString(), txtNote.text.toString(), getCurrentDate())
        mRef.child(noteId).setValue(newNote) //noteId:is a child

    }
    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    fun updateNote(position: Int) {

        var rowId = noteArrList.get(position).id
        val updatedNote = LvDataRow(rowId!!, txtTitle.text.toString(), txtNote.text.toString(), getCurrentDate())
        mRef.child(rowId).setValue(updatedNote) //noteId:is a child


    }
    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    fun getCurrentDate():String{

        return SimpleDateFormat("EEEE hh:mm a").format(Calendar.getInstance().time)

    }
    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    fun receiveData(){
        var getTitle = intent.extras!!.getString("titleKey")
        var getNote= intent.extras!!.getString("noteKey")
        txtTitle.setText(getTitle)
        txtNote.setText(getNote)

    }
    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    //TODO*********************views Actions***************************************************************************************************************************************************
    fun btnSave(v: View){
        var itemPosition= intent.extras!!.getInt("itemPosition")
            if (noteUpdate){updateNote(itemPosition)}
            if (!noteUpdate){addNote()}
            finish()
    }
    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //TODO*********************simplify***************************************************************************************************************************************************************************

}


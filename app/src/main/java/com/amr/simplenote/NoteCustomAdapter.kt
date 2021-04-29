package com.amr.simplenote

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.listrow.view.*

//********************************************************************************************************************************************************************************************************************
class NoteCustomAdapter(context:Context,receivedArrLst:ArrayList<LvDataRow>): ArrayAdapter<LvDataRow>(context,0,receivedArrLst){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var inflatedListRow= LayoutInflater.from(context).inflate(R.layout.listrow,parent,false)
        var rowData: LvDataRow? =getItem(position)//to get data from this class

        inflatedListRow.tvTitle.text= rowData!!.title
        inflatedListRow.tvDate.text= rowData.timestamp
/*        inflatedListRow.setOnLongClickListener {
            inflatedListRow.btnDelete.visibility=View.VISIBLE
            false
        }
        inflatedListRow.setOnLongClickListener {
            var itemPosition = noteArrList.get(position) //get the position and its data to the other activity
            var noteIntent:Intent= Intent(this, Class(NoteActivity))
            noteIntent.putExtra("titleKey",itemPosition.title)
            noteIntent.putExtra("noteKey",itemPosition.noteText)
            noteIntent.putExtra("itemPosition",position)
            startActivity(noteIntent)
            checkUpdate=true //to use it in a condition so that update not add new note
        }*/


        return inflatedListRow
    }

}


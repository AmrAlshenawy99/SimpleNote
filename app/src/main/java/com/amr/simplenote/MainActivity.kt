package com.amr.simplenote

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog.view.*

lateinit var signUpDialog:View
lateinit var dialogHolder:AlertDialog
lateinit var mRef:DatabaseReference
val noteArrList=ArrayList<LvDataRow>()
var noteUpdate=false
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        testLogIn()
        initialFireBase()
        listRowClicked()

    }
    override fun onBackPressed() {finish()}

    override fun onStart() {
        loadDataList()
        super.onStart()
    }

    override fun onResume() {
        noteUpdate =false
        super.onResume()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_toolbar,menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        setMenuItemAct(item)
        return true
    }

    //TODO*******************functions***************************************************************************************************************************************************************************
    private fun initialFireBase(){
        mRef = FirebaseDatabase.getInstance().getReference("NoteRoot")
    }
    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    private fun listRowClicked(){
        noteLV.onItemClickListener= AdapterView.OnItemClickListener { parent, view, position, id ->

            var itemPosition = noteArrList.get(position) //get the position and its data to the other activity
            var noteIntent=Intent(this, NoteActivity::class.java)
            noteIntent.putExtra("titleKey",itemPosition.title)
            noteIntent.putExtra("noteKey",itemPosition.noteText)
            noteIntent.putExtra("itemPosition",position)
            startActivity(noteIntent)
            noteUpdate =true
        }
        noteLV.onItemLongClickListener =AdapterView.OnItemLongClickListener { parent, view, position, id ->
              //  var itemPosition = noteArrList.get(position)
                showDeleteDialog(position)
                false
        }

    }
    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    private fun loadDataList(){
        mRef.addValueEventListener(object :ValueEventListener{ //for reading from firebase
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(storedFbData: DataSnapshot) {
                noteArrList.clear()
                for (n in storedFbData.children){

                    noteArrList.add(0,n.getValue(LvDataRow::class.java)!!) //0 so that add the new note on top
                }//After adding data to the arrayList
                var receivedRowAdapter= NoteCustomAdapter(
                    applicationContext,
                    noteArrList
                )   //(context ,arrLst) are sent to customAdapter to fill data row by row then receive these rows one by one in receivedRowAdapter then give it to listviewComp
                noteLV.adapter=receivedRowAdapter
            }

        })

    }
    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    private fun testLogIn(){

        if (FirebaseAuth.getInstance().currentUser== null){
             finish()
            startActivity(Intent(this, LogInAct::class.java))}
    }
    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    private fun showDeleteDialog(position: Int) {

        var deleteDialog = layoutInflater.inflate(R.layout.dialog, null)
        dialogHolder = AlertDialog
            .Builder(this)
            .setView(deleteDialog)
            .create()
        dialogHolder.show()

        deleteDialog.delete.setOnClickListener {
            var itemId= noteArrList.get(position).id
            mRef.child(itemId!!).removeValue()
            dialogHolder.dismiss()


        }
    }
    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    private fun setMenuItemAct(item: MenuItem?){

        if (item?.itemId== R.id.logout ||item?.itemId== R.id.logoutIt){
            FirebaseAuth.getInstance().signOut()
            showToast("logged out")
            startActivity(Intent(this, LogInAct::class.java))
            finish()

        }
        else if (item?.itemId== R.id.userInfo){
            startActivity(Intent(this, UserInfo::class.java))
        }

    }
    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    //TODO*********************views Actions***************************************************************************************************************************************************
    fun btnAddNote(v:View){
        var noteIntent=Intent(this, NoteActivity::class.java)
        noteIntent.putExtra("titleKey","")
        noteIntent.putExtra("noteKey","")
        noteIntent.putExtra("itemPosition","")
        startActivity(noteIntent)

    }

    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    //TODO*********************simplify***************************************************************************************************************************************************************************
    fun showToast(toast:String){
        Toast.makeText(applicationContext, toast, Toast.LENGTH_LONG).show()
    }
    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    }

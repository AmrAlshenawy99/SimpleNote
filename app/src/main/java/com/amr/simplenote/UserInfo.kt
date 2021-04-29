package com.amr.simplenote

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.user_info.*

class UserInfo : AppCompatActivity() {
    //TODO**********************ov.Rt.func******************************************************************************************************************************************************************************

    lateinit var mStorage:StorageReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_info)
        userImg.setOnClickListener {
            uploadImg()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==2 && resultCode== Activity.RESULT_OK){
            var uriImg =data!!.data
            var imgId = mRef.push().key.toString()
            mStorage.child(imgId).putFile(uriImg!!).addOnSuccessListener {
                showToast("image uploaded")
               // userImg.setImageResource(getFileStreamPath())
              //  UserImg.setImageResource(mRef.child(imgId))
            }
        }
    }
    //TODO**********************ov.Rt.func******************************************************************************************************************************************************************************
    fun uploadImg(){
        mStorage=FirebaseStorage.getInstance().reference//the main root
        val intentImg=Intent(Intent.ACTION_PICK)
        intentImg.type="image/*"
        startActivityForResult(intentImg,2)

    }
    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    //TODO*********************views Actions***************************************************************************************************************************************************
    fun saveUserData(v:View){
        showToast("data saved")
        //how to setImageResource to the user image or change the image
    }
    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    fun userImg(v: View){
        uploadImg()
    }
    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    //TODO*********************simplify***************************************************************************************************************************************************************************
    fun showToast(toast:String){
        Toast.makeText(applicationContext, toast, Toast.LENGTH_LONG).show()
    }
}
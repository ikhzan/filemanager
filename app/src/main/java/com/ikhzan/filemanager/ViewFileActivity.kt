package com.ikhzan.filemanager

import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import java.io.File

class ViewFileActivity : AppCompatActivity(), View.OnClickListener {

    private var backbtn: ImageView? = null
    private var fileimg: ImageView? = null
    private val TAG = "ViewFileActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_file)
        backbtn = findViewById(R.id.backbtn)
        backbtn!!.setOnClickListener(this)

        fileimg = findViewById(R.id.fileimage)
        val filepath = intent!!.getStringExtra("path")
        val file = File(filepath!!)
        Log.d(TAG,"File-Path: ${file.absolutePath}")
        val filebitmap = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile( File(filepath).absolutePath),64,64)
        fileimg!!.setImageBitmap(filebitmap)
    }

    override fun onClick(view: View?) {
        if (view!!.id == R.id.backbtn){
            onBackPressed()
        }
    }
}
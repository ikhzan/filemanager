package com.ikhzan.filemanager.storages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import com.ikhzan.filemanager.R

class StoragesActivity : AppCompatActivity(), View.OnClickListener {

    private var toolbar: Toolbar? = null
    private var quickclean: ConstraintLayout? = null
    private var boost: ConstraintLayout? = null
    private var tip: ConstraintLayout? = null
    private var media: ConstraintLayout? = null
    private var apps: ConstraintLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_storages)

        setTitle(R.string.action_storages)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        quickclean = findViewById(R.id.quickclean)
        quickclean!!.setOnClickListener(this)

        boost = findViewById(R.id.boost)
        boost!!.setOnClickListener(this)

        tip = findViewById(R.id.tip)
        tip!!.setOnClickListener(this)

        media = findViewById(R.id.media)
        media!!.setOnClickListener(this)

        apps = findViewById(R.id.apps)
        apps!!.setOnClickListener(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onClick(view: View) {
        when(view.id){
            R.id.quickclean -> {
                Toast.makeText(this,"Quick Clean",Toast.LENGTH_SHORT).show()
            }
            R.id.boost -> {
                Toast.makeText(this,"Boost",Toast.LENGTH_SHORT).show()
            }
            R.id.tip -> {
                Toast.makeText(this,"Tip",Toast.LENGTH_SHORT).show()
            }
            R.id.media -> {
                Toast.makeText(this,"Media",Toast.LENGTH_SHORT).show()
            }
            R.id.apps -> {
                val intent = Intent(this, AppsActivity::class.java)
                startActivity(intent)
            }
        }
    }


}
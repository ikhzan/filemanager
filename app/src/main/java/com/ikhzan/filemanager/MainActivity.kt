package com.ikhzan.filemanager

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ikhzan.filemanager.cfiles.FilesAdapter
import com.ikhzan.filemanager.cfiles.ViewFileActivity
import com.ikhzan.filemanager.dialogs.AddItemDialog
import com.ikhzan.filemanager.storages.StoragesActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.ikhzan.filemanager.chat.ChatActivity
import com.snatik.storage.Storage
import com.snatik.storage.helpers.OrderType
import java.io.File
import java.util.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
        FilesAdapter.FilesItemListener, View.OnClickListener, AddItemDialog.AddItemListener {

    private val TAG = "MainActivity"
    private val PERMISSION_REQUEST_CODE = 3003
    private var navigationView: NavigationView? = null
    private var drawerLayout: DrawerLayout? = null
    private var toolbar: Toolbar? = null
    private var recyclerView: RecyclerView? = null
    private var adapter: FilesAdapter? = null
    private var mFiles : List<File> = emptyList()
    private var mStorage: Storage? = null
    private var mTreeSteps = 0
    private var pathView: TextView? = null
    private var isListView: Boolean? = null
    private var nofileLayout: LinearLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener(this)

        val homeView: ImageView = findViewById(R.id.homepath)
        homeView.setOnClickListener(this)

        nofileLayout = findViewById(R.id.nofilelayout)
        drawerLayout  = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
        navigationView!!.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        toggle.drawerArrowDrawable.color = Color.WHITE
        drawerLayout!!.addDrawerListener(toggle)
        toggle.syncState()

        isListView = true
        pathView = findViewById(R.id.mPathView)
        recyclerView = findViewById(R.id.file_recyclerview)
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.setLayoutManager(LinearLayoutManager(this))

        if (checkPermission()){
            mStorage = Storage(applicationContext)
            mFiles = mStorage!!.getFiles(Environment.getExternalStorageDirectory().absolutePath)
            if (!mFiles.isEmpty()){
                Collections.sort(mFiles, OrderType.NAME.comparator)
                Log.d(TAG,"File ")
                adapter = FilesAdapter(mFiles)
                adapter!!.notifyDataSetChanged()
                recyclerView!!.adapter = adapter
                adapter!!.setListener(this)
                adapter!!.setRootView(true)
                showFiles(mStorage!!.externalStorageDirectory)
            }
        }else{
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),PERMISSION_REQUEST_CODE)
        }

        isDarkModeOn()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == PERMISSION_REQUEST_CODE){
            mStorage = Storage(applicationContext)
            mFiles = mStorage!!.getFiles(Environment.getExternalStorageDirectory().absolutePath)
            showFiles(mStorage!!.externalStorageDirectory)
        }
    }

    private fun isDarkModeOn() {
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val isDarkModeOn = sharedPreferences.getBoolean("isDarkModeOn", false)
        if (isDarkModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun showFiles(path: String) {
        pathView!!.text = path
        val files = mStorage!!.getFiles(path)
        if (files != null && files.size > 0) {
            recyclerView!!.isVisible = true
            nofileLayout!!.isVisible = false
            Log.d(TAG,"File ${files.size}")
            Collections.sort(files, OrderType.NAME.comparator)
            adapter!!.setFiles(files)
            adapter!!.notifyDataSetChanged()
        }else{
            Log.d(TAG,"there is no files")
            recyclerView!!.isVisible = false
            nofileLayout!!.isVisible = true
        }
    }

    override fun onResume() {
        super.onResume()
        if (!checkPermission()){
            ActivityCompat.requestPermissions(
                    this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    PERMISSION_REQUEST_CODE)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_grid){
            if (isListView!!){
                isListView = false
                adapter!!.setRootView(isListView!!)
                recyclerView!!.adapter = adapter
                recyclerView!!.setLayoutManager(GridLayoutManager(this,3))
                item.setIcon(R.drawable.ic_baseline_grid_24)
            }else{
                isListView = true
                adapter!!.setRootView(isListView!!)
                recyclerView!!.setLayoutManager(LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false))
                recyclerView!!.adapter = adapter
                item.setIcon(R.drawable.ic_baseline_view_list_24)
            }
        }
        return false
    }

    fun checkPermission() : Boolean{
        return (ActivityCompat.checkSelfPermission(applicationContext,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_audios ->{
                if (checkPermission()){
                    val file = File(Environment.getExternalStorageDirectory().absolutePath + "/" +  Environment.DIRECTORY_MUSIC)
                    showFiles(file.absolutePath)
                }else
                    Toast.makeText(this,"Read External Audios",Toast.LENGTH_SHORT).show()
            }
            R.id.nav_videos ->{
                if (checkPermission()){
                    val file = File(Environment.getExternalStorageDirectory().absolutePath + "/" +  Environment.DIRECTORY_MOVIES)
                    showFiles(file.absolutePath)
                }else
                    Toast.makeText(this,"Videos",Toast.LENGTH_SHORT).show()
            }
            R.id.nav_images ->{
                if (checkPermission()){
                    val file = File(Environment.getExternalStorageDirectory().absolutePath + "/" +  Environment.DIRECTORY_DCIM)
                    showFiles(file.absolutePath)
                }else
                    Toast.makeText(this,"Images",Toast.LENGTH_SHORT).show()
            }
            R.id.nav_downloads ->{
                if (checkPermission()){
                    val file = File(Environment.getExternalStorageDirectory().absolutePath + "/" +  Environment.DIRECTORY_DOWNLOADS)
                    showFiles(file.absolutePath)
                }else
                    Toast.makeText(this,"Downloads",Toast.LENGTH_SHORT).show()
            }
            R.id.nav_settings -> {
                val intent = Intent(this, SettingActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_version -> {
                Toast.makeText(this,"Version 1.0.0",Toast.LENGTH_SHORT).show()
            }
            R.id.nav_storages -> {
                val intent = Intent(this, StoragesActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_chat -> {
                val intent = Intent(this,ChatActivity::class.java)
                startActivity(intent)
            }
        }
        drawerLayout!!.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onClick(file: File) {
        if (file.isDirectory){
            mTreeSteps++
            showFiles(file.absolutePath)
        }
        else {
            try {
                val intent = Intent(this, ViewFileActivity::class.java)
                intent.putExtra("path",file.absolutePath)
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
               e.printStackTrace()
            }
        }
    }

    private fun getCurrentPath(): String {
        return pathView!!.getText().toString()
    }

    private fun getPreviousPath(): String {
        val path = getCurrentPath()
        val lastIndexOf = path.lastIndexOf(File.separator)
        if (lastIndexOf < 0) {
            return getCurrentPath()
        }
        return path.substring(0, lastIndexOf)
    }

    override fun onBackPressed() {
        if (mTreeSteps > 0) {
            val path: String = getPreviousPath()
            mTreeSteps--
            showFiles(path)
            return
        }
        Log.d(TAG,"onBackPressed ")
        super.onBackPressed()
    }

    override fun onLongClick(file: File) {
        updateOrDeleteDialog(file.name)
    }

    fun updateOrDeleteDialog(filename:String){
        MaterialAlertDialogBuilder(this)
                .setTitle(resources.getString(R.string.app_name))
                .setMessage(resources.getString(R.string.updateordelete))
                .setNegativeButton(resources.getString(R.string.update)) { dialog, which ->
                    Log.d(TAG,"Update File $filename")
                }
                .setPositiveButton(resources.getString(R.string.delete)) { dialog, which ->
                    Log.d(TAG,"Delete File $filename")
                }
                .show()
    }

    override fun onClick(view: View) {
        when(view.id){
            R.id.homepath -> {
                showFiles(mStorage!!.externalStorageDirectory)
                Toast.makeText(this,"Show Home",Toast.LENGTH_LONG).show()
            }
            R.id.fab -> {
                addItemDialog()
            }
        }
    }

    fun addItemDialog(){
        MaterialAlertDialogBuilder(this)
                .setTitle(resources.getString(R.string.app_name))
                .setMessage(resources.getString(R.string.support_text))
                .setNegativeButton(resources.getString(R.string.cancel)) { dialog, which ->
                   dialog.dismiss()
                }
                .setPositiveButton(resources.getString(R.string.accept)) { dialog, which ->
                    val fragmentManager = supportFragmentManager
                    AddItemDialog().newInstance().show(fragmentManager,"Add Item")
                }
                .show()
    }

    override fun onNewFile(name: String, content: String) {
        Log.d(TAG,"Filename $name, Content $content")
    }
}
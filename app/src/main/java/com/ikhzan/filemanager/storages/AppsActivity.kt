package com.ikhzan.filemanager.storages

import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.lang.Exception
import com.ikhzan.filemanager.R
import java.io.File

class AppsActivity : AppCompatActivity() {

    private var adapter: AppAdapter? = null
    private var toolbar: Toolbar? = null
    private var recyclerView: RecyclerView? = null
    private var mApps: MutableList<App> = mutableListOf()
    private val TAG = "AppsActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apps)
        setTitle("Apps")

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        recyclerView = findViewById(R.id.app_rc)
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.setLayoutManager(LinearLayoutManager(this))

        mApps = getAppsInstalled()

        if (mApps.size > 0){
            adapter = AppAdapter(mApps)
            adapter!!.notifyDataSetChanged()
            recyclerView!!.adapter = adapter
        }
    }

    private fun getAppsInstalled(): MutableList<App> {
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
        val resolveInfoList: List<ResolveInfo> = getPackageManager().queryIntentActivities(intent, 0)

        for(info in resolveInfoList){
            val activityInfo: ActivityInfo = info.activityInfo
            val appname = getAppName(activityInfo.packageName)
            val filesize = File(activityInfo.applicationInfo.sourceDir)
            mApps.add(App(appname, activityInfo.packageName,activityInfo.applicationInfo.loadIcon(packageManager!!),formatSize(filesize.length())))
            activityInfo.applicationInfo.sourceDir
        }
        return mApps
    }

    private fun formatSize(size: Long): String {
        var fileSize: Long = size

        var suffixSize: String? = null

        if (fileSize >= 1024) {
            suffixSize = " KB"
            fileSize /=   1024
            if (fileSize >= 1024) {
                suffixSize = " MB"
                fileSize /= 1024
            }
        }
        val BufferSize = StringBuilder(
                java.lang.Long.toString(fileSize)
        )
        var commaOffset = BufferSize.length - 3
        while (commaOffset > 0) {
            BufferSize.insert(commaOffset, ',');
            commaOffset -= 3;
        }
        if (suffixSize != null) BufferSize.append(suffixSize);
        return BufferSize.toString()
    }

    fun getAppName(apppackage: String): String{
        var appname = ""
        var applicationInfo: ApplicationInfo? = null
        try {
            val packageManager: PackageManager = packageManager
            applicationInfo = packageManager.getApplicationInfo(apppackage,0)
            appname = packageManager.getApplicationLabel(applicationInfo) as String
        }catch (e: Exception){
            e.printStackTrace()
        }
        return appname
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
package com.ikhzan.filemanager.storages

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ikhzan.filemanager.R

class AppAdapter(val mApps: List<App>) : RecyclerView.Adapter<AppAdapter.AppViewHolder>() {

    private var rootView: View? = null
    private val TAG = "AppAdapter"

    class AppViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mName: TextView = itemView.findViewById(R.id.appname)
        var mSize: TextView = itemView.findViewById(R.id.appsize)
        var mAppIcon: ImageView = itemView.findViewById(R.id.appicon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        rootView = LayoutInflater.from(parent.context).inflate(R.layout.app_item_layout, parent, false)
        return AppViewHolder(rootView!!)
    }

    override fun getItemCount(): Int {
        return mApps.size
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
       val app = mApps[position]
        holder.mName.text = app.name
        holder.mAppIcon.setImageDrawable(app.icon)
        holder.mSize.text = app.appsize
    }
}
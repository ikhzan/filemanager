package com.ikhzan.filemanager.cfiles

import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.ikhzan.filemanager.R
import java.io.File

class FilesAdapter(var mFiles : List<File>) : RecyclerView.Adapter<FilesAdapter.FilesViewHolder>() {

    private val TAG = "FilesAdapter"
    private lateinit var mListener : FilesItemListener
    private var rootView: View? = null
    private var isListView: Boolean? = null

    class FilesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var mName: TextView = itemView.findViewById(R.id.name)
        var mSize: TextView = itemView.findViewById(R.id.size)
        var mIcon: ImageView = itemView.findViewById(R.id.icon)
        var mType: TextView = itemView.findViewById(R.id.ftype)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilesViewHolder {
        if (isListView!!)
            rootView = LayoutInflater.from(parent.context).inflate(R.layout.file_line_view, parent, false)
        else
            rootView = LayoutInflater.from(parent.context).inflate(R.layout.file_grid_view, parent, false)
        return FilesViewHolder(rootView!!)
    }

    fun setRootView(setlist: Boolean){
        isListView = setlist
    }

    override fun getItemCount(): Int {
        return mFiles.size
    }

    override fun onBindViewHolder(holder: FilesViewHolder, position: Int) {
        val file = mFiles[position]
        Log.d(TAG,"filename ${file.name}")
        holder.mName.setText(file.name)
        holder.mName.isVisible = true
        holder.mIcon.setImageResource(if (file.isDirectory) R.drawable.ic_baseline_folder_24 else R.drawable.ic_baseline_insert_drive_file_24)

        if (file.isDirectory){
            holder.mSize.isVisible = false
            holder.mType.isVisible = false
        }

        if (file.isFile){
            holder.mSize.text = formatSize(file.length())
            holder.mType.text = file.extension.toUpperCase()
            val regex = Regex("jpg|jpeg|png")
            if (file.extension.matches(regex)){
                holder.mType.isVisible = true
                holder.mSize.isVisible = true
                val filebitmap = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(file.absolutePath),64,64)
                holder.mIcon.setImageBitmap(filebitmap)
                holder.mIcon.imageAlpha
            }
        }

        if (!isListView!!){
            holder.mSize.isVisible = false
            holder.mType.isVisible = false
            if (file.extension == "jpg"){
                holder.mName.isVisible = false
            }
        }

        holder.itemView.setOnClickListener {
            mListener.onClick(file)
            Log.d(TAG,"OnClick ${file.name}")
        }
        holder.itemView.setOnLongClickListener {
            mListener.onLongClick(file)
            Log.d(TAG,"OnLongClick ${file.name}")
            true
        }
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

    fun setFiles(files : List<File>){
        mFiles = files
    }

    fun setListener(listener: FilesItemListener) {
        mListener = listener
    }

    interface FilesItemListener{
        fun onClick(file: File)
        fun onLongClick(file: File)
    }

}
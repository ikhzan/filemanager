package com.ikhzan.filemanager.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar
import java.util.*

class Helper {
    fun showSnackbar(message: String, root: View) {
        Snackbar.make(root, message, Snackbar.LENGTH_SHORT).show()
    }

    public fun fileExt(inurl: String): String? {
        var url = inurl
        if (url.indexOf("?") > -1) {
            url = url.substring(0, url.indexOf("?"))
        }
        return if (url.lastIndexOf(".") == -1) {
            null
        } else {
            var ext = url.substring(url.lastIndexOf(".") + 1)
            if (ext.indexOf("%") > -1) {
                ext = ext.substring(0, ext.indexOf("%"))
            }
            if (ext.indexOf("/") > -1) {
                ext = ext.substring(0, ext.indexOf("/"))
            }
            ext.toLowerCase(Locale.ROOT)
        }
    }

}
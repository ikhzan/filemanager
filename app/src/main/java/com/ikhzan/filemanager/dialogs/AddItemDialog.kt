package com.ikhzan.filemanager.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.ikhzan.filemanager.R


class AddItemDialog : DialogFragment() {

    private val TAG = "AddItemDialog"
    private var mListener: AddItemListener ? = null

    fun newInstance(): AddItemDialog {

        return AddItemDialog()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)

        val view: View = LayoutInflater.from(activity)
                .inflate(R.layout.new_item_layout, view as ViewGroup?, false)

        val filenametxt: EditText = view.findViewById(R.id.filenametxt)
        val textarea: EditText = view.findViewById(R.id.editTextTextMultiLine)

        builder.setPositiveButton(R.string.accept) { _, i -> mListener!!.onNewFile(filenametxt.getText().toString(), textarea.getText().toString()) }
        builder.setTitle(R.string.app_name)
        builder.setView(view)
        val dialog = builder.create()
        view.post { dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = true }
        view.post { dialog.getButton(AlertDialog.BUTTON_NEGATIVE).isEnabled = true }
        dialog.setCancelable(true)

        return dialog
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = try {
            activity as AddItemListener
        } catch (e: ClassCastException) {
            throw ClassCastException(activity.toString()
                    + " must implement AddItemListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        dialog.cancel()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    interface AddItemListener{
        fun onNewFile(name: String, content: String)
    }

}
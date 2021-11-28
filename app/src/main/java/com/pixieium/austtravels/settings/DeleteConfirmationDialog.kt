package com.pixieium.austtravels.settings

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.pixieium.austtravels.R
import com.pixieium.austtravels.databinding.DialogDeleteConfirmationBinding
import com.pixieium.austtravels.databinding.DialogSelectBusBinding
import com.pixieium.austtravels.models.BusInfo
import com.pixieium.austtravels.models.BusTiming
import kotlinx.coroutines.launch

class DeleteConfirmationDialog : DialogFragment() {

    private lateinit var mContext: Context
    private lateinit var mBinding: DialogDeleteConfirmationBinding
    private var listener: FragmentListener? = null

    companion object {
        const val TAG = "DeleteConfirmationDialog"

        fun newInstance(): DeleteConfirmationDialog {
            return DeleteConfirmationDialog()
        }
    }

    interface FragmentListener {
        fun onEnterPassword(password: String)
    }

    /**
     * The onCreateView() method is responsible for creating the Dialog Fragment.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DialogDeleteConfirmationBinding.inflate(layoutInflater)
        mContext = mBinding.root.context

        mBinding.enterBtn.setOnClickListener {
            val password = mBinding.password.editText?.text.toString()
            listener?.onEnterPassword(password)
            dismiss()
        }
        return mBinding.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = if (context is FragmentListener) {
            context
        } else {
            throw RuntimeException(context.toString())
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun getTheme() = R.style.RoundedCornersDialog
}
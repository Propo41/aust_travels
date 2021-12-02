package com.pixieium.austtravels.settings.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.pixieium.austtravels.R
import com.pixieium.austtravels.databinding.DialogWarningBinding

class DeleteConfirmationDialog : DialogFragment() {

    private lateinit var mContext: Context
    private lateinit var mBinding: DialogWarningBinding
    private var listener: FragmentListener? = null

    companion object {
        const val TAG = "DeleteConfirmationDialog"

        fun newInstance(): DeleteConfirmationDialog {
            return DeleteConfirmationDialog()
        }
    }

    interface FragmentListener {
        fun onDeleteConfirmClick()
    }

    /**
     * The onCreateView() method is responsible for creating the Dialog Fragment.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DialogWarningBinding.inflate(layoutInflater)
        mContext = mBinding.root.context

        mBinding.confirmBtn.setOnClickListener {
            listener?.onDeleteConfirmClick()
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
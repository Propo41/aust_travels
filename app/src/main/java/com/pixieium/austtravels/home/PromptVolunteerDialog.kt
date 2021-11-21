package com.pixieium.austtravels.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.pixieium.austtravels.R
import com.pixieium.austtravels.databinding.DialogPromptVolunteerBinding
import java.lang.RuntimeException


class PromptVolunteerDialog : DialogFragment() {

    private lateinit var mContext: Context
    private lateinit var mBinding: DialogPromptVolunteerBinding
    private var listener: FragmentListener? = null

    companion object {
        const val TAG = "PromptVolunteerDialogFragment"
        private lateinit var mUid: String

        fun newInstance(uid: String): PromptVolunteerDialog {
            mUid = uid
            return PromptVolunteerDialog()
        }
    }

    interface FragmentListener {
        fun onVolunteerApprovalClick()
    }

    /**
     * The onCreateView() method is responsible for creating the Dialog Fragment.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DialogPromptVolunteerBinding.inflate(layoutInflater)
        mContext = mBinding.root.context
        mBinding.selectBtn.setOnClickListener {
            Toast.makeText(context, "You are now a volunteer!", Toast.LENGTH_SHORT).show()
            listener?.onVolunteerApprovalClick()
            dismiss()
        }
        return mBinding.root
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

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    override fun getTheme() = R.style.RoundedCornersDialog
}
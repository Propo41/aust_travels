package com.pixieium.austtravels.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.pixieium.austtravels.R
import com.pixieium.austtravels.databinding.DialogSelectBusBinding

class SelectBusDialog : DialogFragment() {

    private lateinit var mContext: Context
    private lateinit var mBinding: DialogSelectBusBinding

    companion object {
        const val TAG = "PremiumDialogFragment"
        private lateinit var mUid: String

        fun newInstance(uid: String): SelectBusDialog {
            mUid = uid
            return SelectBusDialog()
        }
    }

    /**
     * The onCreateView() method is responsible for creating the Dialog Fragment.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DialogSelectBusBinding.inflate(layoutInflater)
        initSpinnerName()
        initSpinnerTime()
        mContext = mBinding.root.context
        mBinding.selectBtn.setOnClickListener {
            Toast.makeText(context, "bus selected!", Toast.LENGTH_SHORT).show()

        }
        return mBinding.root
    }

    private fun initSpinnerName() {
        val items = listOf("Option 1", "Option 2", "Option 3", "Option 4")
        val adapter = ArrayAdapter(mContext, R.layout.item_spinner, items)
        (mBinding.selectName.editText as? AutoCompleteTextView)?.setAdapter(adapter)
    }

    private fun initSpinnerTime() {
        val items = listOf("Option 1", "Option 2", "Option 3", "Option 4")
        val adapter = ArrayAdapter(mContext, R.layout.item_spinner, items)
        (mBinding.selectTime.editText as? AutoCompleteTextView)?.setAdapter(adapter)
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
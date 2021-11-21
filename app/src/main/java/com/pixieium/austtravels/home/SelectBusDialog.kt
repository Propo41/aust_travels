package com.pixieium.austtravels.home

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
import java.lang.RuntimeException

class SelectBusDialog : DialogFragment() {

    private lateinit var mContext: Context
    private lateinit var mBinding: DialogSelectBusBinding
    private var listener: FragmentListener? = null

    companion object {
        const val TAG = "SelectBusDialogFragment"
        private var REQUESTER: Int = 0

        fun newInstance(requester: Int): SelectBusDialog {
            REQUESTER = requester
            return SelectBusDialog()
        }
    }


    interface FragmentListener {
        fun onBusSelectClick(selectedBusName: String, selectedBusTime: String, requestCode: Int)
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
        mContext = mBinding.root.context
        initSpinnerName()
        initSpinnerTime()
        mBinding.selectBtn.setOnClickListener {
            val selectedBusName = mBinding.selectName.editText?.text.toString()
            val selectedBusTime = mBinding.selectTime.editText?.text.toString()

            Toast.makeText(context, "$selectedBusName is selected!", Toast.LENGTH_SHORT).show()

            listener?.onBusSelectClick(selectedBusName, selectedBusTime, REQUESTER)
            dismiss()
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
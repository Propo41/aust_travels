package com.pixieium.austtravels.home

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
import com.pixieium.austtravels.databinding.DialogSelectBusBinding
import com.pixieium.austtravels.models.BusInfo
import com.pixieium.austtravels.models.BusTiming
import kotlinx.coroutines.launch

class SelectBusDialog : DialogFragment() {

    private lateinit var mContext: Context
    private lateinit var mBinding: DialogSelectBusBinding
    private var listener: FragmentListener? = null
    private val mDatabase: HomeRepository = HomeRepository()

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

        mBinding.selectTime.isEnabled = false
        mBinding.selectName.isEnabled = false
        mBinding.selectBtn.isEnabled = false

        lifecycleScope.launch {
            try {
                val list: ArrayList<BusInfo> = mDatabase.fetchAllBusInfo()
                if (list.size != 0) {
                    initSpinnerName(list)
                } else {
                    Toast.makeText(
                        context,
                        "Couldn't fetch data from database. Please check your connection",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            } catch (e: Exception) {
                //e.printStackTrace()
                Toast.makeText(
                    context,
                    "Couldn't fetch data from database. Please check your connection",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }

        mBinding.selectBtn.setOnClickListener {
            val selectedBusName = mBinding.selectName.editText?.text.toString()
            val selectedBusTime = mBinding.selectTime.editText?.text.toString()

            //Toast.makeText(context, "$selectedBusName is selected!", Toast.LENGTH_SHORT).show()
            listener?.onBusSelectClick(selectedBusName, selectedBusTime, REQUESTER)
            dismiss()
        }
        return mBinding.root
    }


    private fun initSpinnerName(list: ArrayList<BusInfo>) {
        val items: ArrayList<String> = ArrayList()
        for (busInfo: BusInfo in list) {
            items.add(busInfo.name)
        }
        mBinding.selectName.isEnabled = true
        val adapter = ArrayAdapter(requireContext(), R.layout.item_spinner, items)
        (mBinding.selectName.editText as? AutoCompleteTextView)?.setAdapter(adapter)

        mBinding.selectName.editText?.setOnClickListener {
            mBinding.selectTime.editText?.text?.clear()
            mBinding.selectTime.isEnabled = false
        }

        mBinding.selectName.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Toast.makeText(requireContext(), s.toString(), Toast.LENGTH_SHORT).show()
                initSpinnerTime(s.toString(), list)
                mBinding.selectBtn.isEnabled = true

            }
        })

    }

    private fun initSpinnerTime(selectedName: String, list: ArrayList<BusInfo>) {
        val timingList: ArrayList<String> = ArrayList()
        mBinding.selectTime.isEnabled = true
        for (busInfo: BusInfo in list) {
            if (busInfo.name == selectedName) {
                for (timing: BusTiming in busInfo.timing) {
                    timingList.add(timing.startTime)
                }
                break
            }
        }
        val adapter = ArrayAdapter(requireContext(), R.layout.item_spinner, timingList)
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
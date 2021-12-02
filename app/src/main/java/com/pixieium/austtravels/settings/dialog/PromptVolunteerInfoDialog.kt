package com.pixieium.austtravels.settings.dialog

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
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
import com.pixieium.austtravels.databinding.DialogPromptVolunteerInfoBinding
import com.pixieium.austtravels.models.BusInfo
import com.pixieium.austtravels.settings.SettingsRepository
import kotlinx.coroutines.launch


class PromptVolunteerInfoDialog : DialogFragment() {

    private lateinit var mContext: Context
    private lateinit var mBinding: DialogPromptVolunteerInfoBinding
    private var listener: FragmentListener? = null
    private val mDatabase: SettingsRepository = SettingsRepository()

    companion object {
        const val TAG = "PromptVolunteerInfoDialogFragment"
        fun newInstance(): PromptVolunteerInfoDialog {
            return PromptVolunteerInfoDialog()
        }
    }

    interface FragmentListener {
        fun onVolunteerConfirmClick(busName: String, contact: String)
    }

    /**
     * The onCreateView() method is responsible for creating the Dialog Fragment.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DialogPromptVolunteerInfoBinding.inflate(layoutInflater)
        mContext = mBinding.root.context

        fetchInfo()
        mBinding.selectBtn.setOnClickListener {
            if (isValid()) {
                listener?.onVolunteerConfirmClick(
                    mBinding.selectName.editText?.text.toString(),
                    mBinding.phone.editText?.text.toString()
                )
                dismiss()
            } else {
                Toast.makeText(
                    mContext,
                    "Please enter the information correctly",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
        return mBinding.root
    }

    private fun fetchInfo() {
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
                    e.localizedMessage,
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
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

    private fun initSpinnerName(list: ArrayList<BusInfo>) {
        val items: ArrayList<String> = ArrayList()
        for (busInfo: BusInfo in list) {
            items.add(busInfo.name)
        }
        mBinding.selectName.isEnabled = true
        val adapter = ArrayAdapter(requireContext(), R.layout.item_spinner, items)
        (mBinding.selectName.editText as? AutoCompleteTextView)?.setAdapter(adapter)
    }

    private fun isValid(): Boolean {
        val busName = mBinding.selectName.editText?.text
        val phone = mBinding.phone.editText?.text
        return true
        if (!busName.isNullOrEmpty() && !phone.isNullOrEmpty() && !TextUtils.isEmpty(phone) && phone.length >= 11) {
            return true
        }
        return false
    }

}
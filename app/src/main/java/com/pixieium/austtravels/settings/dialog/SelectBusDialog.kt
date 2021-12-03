package com.pixieium.austtravels.settings.dialog

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
import androidx.lifecycle.lifecycleScope
import com.pixieium.austtravels.R
import com.pixieium.austtravels.databinding.DialogSelectBusBinding
import com.pixieium.austtravels.models.BusInfo
import com.pixieium.austtravels.settings.SettingsRepository
import kotlinx.coroutines.launch
import timber.log.Timber

class SelectBusDialog : DialogFragment() {

    private lateinit var mContext: Context
    private lateinit var mBinding: DialogSelectBusBinding
    private var listener: FragmentListener? = null
    private val mDatabase: SettingsRepository = SettingsRepository()

    companion object {
        const val TAG = "SelectBusDialogFragment"

        fun newInstance(): SelectBusDialog {
            return SelectBusDialog()
        }
    }


    interface FragmentListener {
        fun onBusSelectClick(selectedBusName: String)
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

        mBinding.selectTime.visibility = View.GONE
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
                Timber.e(e, e.localizedMessage)
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
            listener?.onBusSelectClick(selectedBusName)
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

        mBinding.selectBtn.isEnabled = true

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
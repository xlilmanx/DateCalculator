package com.pm.datecalculator

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.pm.datecalculator.databinding.FragmentSecondBinding
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private var mContext: Context? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    @SuppressLint("RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var numberDays:Long=0
        val text_initial: TextView = view.findViewById(R.id.text_initial) as TextView
        val text_number_days: TextView = view.findViewById(R.id.text_number_days) as TextView
        val text_final_date: TextView = view.findViewById(R.id.text_final_date) as TextView
        val text_final_date_day: TextView = view.findViewById(R.id.text_final_date_day) as TextView
        val localTimeInMillis = System.currentTimeMillis()

        val dtf: DateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
        var initialTime: LocalDate = Instant.ofEpochMilli(localTimeInMillis)
            .atZone(ZoneId.systemDefault()).toLocalDate()
        var newTime: LocalDate = Instant.ofEpochMilli(localTimeInMillis)
            .atZone(ZoneId.systemDefault()).toLocalDate()
        text_final_date_day.text = newTime.dayOfWeek.toString()

        text_initial.text = dtf.format(initialTime)
        text_number_days.text = numberDays.toString()
        newTime=initialTime.plusDays(numberDays)
        text_final_date.text = dtf.format(newTime)

        val datePickerInitial =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select initial date")
                .setSelection(localTimeInMillis + TimeZone.getDefault().getOffset(localTimeInMillis))
                .build()

        binding.buttonInitial.setOnClickListener {
            datePickerInitial.show(parentFragmentManager, "datePicker")
        }

        //inputEditTextField.inputType = InputType.TYPE_CLASS_NUMBER + InputType.TYPE_NUMBER_FLAG_SIGNED

        val days_input_view = layoutInflater.inflate(R.layout.days_input_edittext, null)
        val inputEditTextField = days_input_view.findViewById<EditText>(R.id.editTextNumber)
        inputEditTextField.setText(text_number_days.text)
        inputEditTextField.setSelectAllOnFocus(true)
        val numberDaysDialogBuilder: MaterialAlertDialogBuilder? = mContext?.let {
            MaterialAlertDialogBuilder(
                it
            )
        }

        numberDaysDialogBuilder?.setTitle(resources.getString(R.string.alert_days_title))
        numberDaysDialogBuilder?.setView(days_input_view)
        numberDaysDialogBuilder?.setNeutralButton(resources.getString(R.string.cancel)) { _, _ ->
            // Respond to neutral button press
        }
        numberDaysDialogBuilder?.setPositiveButton(resources.getString(R.string.accept)) { _, _ ->
            if (text_number_days.text != ""  && isInteger(text_number_days.text.toString())) {
                text_number_days.text = inputEditTextField.text.toString()
                numberDays = text_number_days.text.toString().toLong()
                newTime=initialTime.plusDays(numberDays)
                text_final_date.text = dtf.format(newTime)
                text_final_date_day.text = newTime.dayOfWeek.toString()
                inputEditTextField.clearFocus()
            }
        }
        val numberDaysDialog = numberDaysDialogBuilder?.create()
        binding.buttonNumberDays.setOnClickListener {

            numberDaysDialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
            numberDaysDialog?.show()

            inputEditTextField.requestFocus()

        }

        datePickerInitial.addOnPositiveButtonClickListener {
            val initialTimeInMillis = it - TimeZone.getDefault().getOffset(it)
            initialTime = Instant.ofEpochMilli(initialTimeInMillis)
                .atZone(ZoneId.systemDefault()).toLocalDate()
            text_initial.text = dtf.format(initialTime)

            text_final_date.text = dtf.format(initialTime.plusDays(numberDays))
        }

    }

    fun isInteger(str: String?): Boolean {
        if (str == null) {
            return false
        }
        val length = str.length
        if (length == 0) {
            return false
        }
        var i = 0
        if (str[0] == '-') {
            if (length == 1) {
                return false
            }
            i = 1
        }
        while (i < length) {
            val c = str[i]
            if (c < '0' || c > '9') {
                return false
            }
            i++
        }
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
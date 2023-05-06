package com.pm.datecalculator

import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.view.Gravity.CENTER_HORIZONTAL
import android.view.View
import android.view.WindowManager
import android.widget.EditText
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
class FirstFragment : Fragment(R.layout.fragment_second) {

    private lateinit var mContext: Context
    private var numberDays = 0L
    private val dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy")

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentSecondBinding.bind(view)

        val currentDate = LocalDate.now()
        binding.textInitial.text = dtf.format(currentDate)
        binding.textFinalDateDay.text = currentDate.dayOfWeek.toString()

        var datePickerInitial = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select initial date")
            .setSelection(System.currentTimeMillis() + TimeZone.getDefault().getOffset(System.currentTimeMillis()))
            .build()

        binding.buttonInitial.setOnClickListener {
            datePickerInitial = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select initial date")
                .setSelection(System.currentTimeMillis() + TimeZone.getDefault().getOffset(System.currentTimeMillis()))
                .build()
            datePickerInitial.show(parentFragmentManager, "datePicker")
        }

        binding.buttonNumberDays.setOnClickListener {
            val inputEditTextField = EditText(requireContext()).apply {
                setText(binding.textNumberDays.text)
                setSelectAllOnFocus(true)
                gravity = CENTER_HORIZONTAL
                inputType = InputType.TYPE_CLASS_NUMBER
            }


            val numberDaysDialog = MaterialAlertDialogBuilder(mContext)
                .setTitle(R.string.alert_days_title)
                .setView(inputEditTextField)
                .setNegativeButton(R.string.cancel) { dialog, _ -> dialog.dismiss() }
                .setPositiveButton(R.string.accept) { dialog, _ ->
                    val inputText = inputEditTextField.text.toString()
                    if (inputText.isDigitsOnly()) {
                        numberDays = inputText.toLong()
                        binding.textNumberDays.text = inputText
                        updateFinalDate(binding)
                    } else {
                        inputEditTextField.error = "Please enter a valid number"
                    }
                    dialog.dismiss()
                }
                .create()
            inputEditTextField.requestFocus()
            numberDaysDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
            numberDaysDialog.show()
        }

        datePickerInitial.addOnPositiveButtonClickListener {
            binding.textInitial.text = dtf.format(Instant.ofEpochMilli(it)
                .atZone(ZoneId.systemDefault()).toLocalDate())
            updateFinalDate(binding)
        }
    }

    private fun updateFinalDate(binding: FragmentSecondBinding) {
        val finalDate = LocalDate.now().plusDays(numberDays)
        binding.textFinalDate.text = dtf.format(finalDate)
        binding.textFinalDateDay.text = finalDate.dayOfWeek.toString()
    }

    private fun String.isDigitsOnly(): Boolean = matches("-?\\d+".toRegex())
}




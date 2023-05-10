package com.pm.datecalculator

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.view.Gravity.CENTER_HORIZONTAL
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.Fragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.pm.datecalculator.databinding.FragmentSecondBinding
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.absoluteValue


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class FirstFragment : Fragment(R.layout.fragment_second) {

    private lateinit var mContext: Context
    private var numberDays = 0L
    private val dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy")
    private val currentTimeMillis = System.currentTimeMillis()
    private val timeZoneOffset: Int = TimeZone.getDefault().getOffset(currentTimeMillis)
    private val zoneID = ZoneId.systemDefault()
    private val currentDateInMillis: Long
        get() = currentTimeMillis + timeZoneOffset

    private var initialDate: LocalDate = Instant.ofEpochMilli(currentDateInMillis - timeZoneOffset)
        .atZone(zoneID).toLocalDate()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentSecondBinding.bind(view)

        val currentDate = LocalDate.now()
        binding.textInitial.text = dtf.format(currentDate)
        binding.textFinalDateDay.text = currentDate.dayOfWeek.toString()

        updateFinalDate(binding)

        var datePickerInitial = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select initial date")
            .setSelection(System.currentTimeMillis() + TimeZone.getDefault().getOffset(System.currentTimeMillis()))
            .build()

        binding.buttonInitial.setOnClickListener {
            datePickerInitial = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select initial date")
                .setSelection(initialDate.atStartOfDay().atZone(zoneID).toInstant().toEpochMilli())
                .build()

            datePickerInitial.addOnPositiveButtonClickListener { dateInMillis ->
                val date = Instant.ofEpochMilli(dateInMillis - timeZoneOffset)
                    .atZone(zoneID).toLocalDate()

                    initialDate = date
                    binding.textInitial.text = DATE_FORMAT.format(date)
                    updateFinalDate(binding)
            }

            datePickerInitial.show(parentFragmentManager, "datePicker")
        }

        binding.buttonNumberDays.setOnClickListener {
            val inputEditTextField = EditText(requireContext()).apply {
                setText(if (binding.textNumberDays.text.isDigitsOnly()) binding.textNumberDays.text else numberDays.toString())
                setSelectAllOnFocus(true)
                gravity = CENTER_HORIZONTAL
                inputType = InputType.TYPE_CLASS_NUMBER
                filters = arrayOf(InputFilter.LengthFilter(10))
            }

            val inputEditTextFieldContainer = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.VERTICAL
                val layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                layoutParams.setMargins(245, 70, 245, 20)
                addView(inputEditTextField, layoutParams)
            }

            val numberDaysDialog = MaterialAlertDialogBuilder(mContext)
                .setTitle(R.string.alert_days_title)
                .setView(inputEditTextFieldContainer)
                .setNegativeButton(R.string.cancel) { dialog, _ -> dialog.dismiss() }
                .setPositiveButton(R.string.accept) { dialog, _ ->
                    val inputText = inputEditTextField.text.toString()
                    if (inputText.isDigitsOnly() && inputText.toLong().absoluteValue < 10000000000) {
                        numberDays = inputText.toLong()
                        binding.textNumberDays.text = inputText
                        updateFinalDate(binding)
                    } else {
                        inputEditTextField.error = "Please enter a valid number"
                        binding.textNumberDays.text = "Please enter a valid number"
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
        val finalDate = initialDate.plusDays(numberDays)
        binding.textFinalDate.text = dtf.format(finalDate)
        binding.textFinalDateDay.text = finalDate.dayOfWeek.toString()
    }
    private fun String.isDigitsOnly(): Boolean = matches("-?\\d+".toRegex())

    companion object {
        private val DATE_FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
    }
}




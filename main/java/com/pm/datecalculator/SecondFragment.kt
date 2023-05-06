package com.pm.datecalculator

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.pm.datecalculator.databinding.FragmentFirstBinding
import java.text.ParseException
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class SecondFragment : Fragment() {
    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val text_initial: TextView = view.findViewById(R.id.text_initial) as TextView
        val text_final: TextView = view.findViewById(R.id.text_final) as TextView


        val text_total: TextView = view.findViewById(R.id.text_number_days) as TextView

        val localTimeInMillis = System.currentTimeMillis()

        val dtf: DateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
        var initialTime: LocalDate = Instant.ofEpochMilli(localTimeInMillis)
            .atZone(ZoneId.systemDefault()).toLocalDate()
        var finalTime: LocalDate = Instant.ofEpochMilli(localTimeInMillis)
            .atZone(ZoneId.systemDefault()).toLocalDate()
        var daysBetween:Long = 0

        text_initial.text = dtf.format(initialTime)
        text_final.text = dtf.format(finalTime)

        try {
            val date1: LocalDate = LocalDate.parse(dtf.format(initialTime).toString(), dtf)
            val date2: LocalDate = LocalDate.parse(dtf.format(finalTime).toString(), dtf)
            daysBetween = ChronoUnit.DAYS.between(date1, date2)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        text_total.text =  daysBetween.toString()


        val datePickerInitial =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select initial date")
                .setSelection(localTimeInMillis + TimeZone.getDefault().getOffset(localTimeInMillis))
                .build()

        val datePickerFinal =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select final date")
                .setSelection(localTimeInMillis + TimeZone.getDefault().getOffset(localTimeInMillis))
                .build()

        binding.buttonInitial.setOnClickListener {
            datePickerInitial.show(parentFragmentManager, "datePicker")
        }

        binding.buttonFinal.setOnClickListener {
            datePickerFinal.show(parentFragmentManager, "datePicker")
        }

        datePickerInitial.addOnPositiveButtonClickListener {
            val initialTimeInMillis = it - TimeZone.getDefault().getOffset(it)
            initialTime = Instant.ofEpochMilli(initialTimeInMillis)
                .atZone(ZoneId.systemDefault()).toLocalDate()
            text_initial.text = dtf.format(initialTime)
            try {
                val date1: LocalDate = LocalDate.parse(dtf.format(initialTime).toString(), dtf)
                val date2: LocalDate = LocalDate.parse(dtf.format(finalTime).toString(), dtf)
                daysBetween = ChronoUnit.DAYS.between(date1, date2)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            text_total.text =  daysBetween.toString()
        }

        datePickerFinal.addOnPositiveButtonClickListener {
            val finalTimeInMillis = it - TimeZone.getDefault().getOffset(it)
            finalTime = Instant.ofEpochMilli(finalTimeInMillis)
                .atZone(ZoneId.systemDefault()).toLocalDate()
            text_final.text = dtf.format(finalTime)
            try {
                val date1: LocalDate = LocalDate.parse(dtf.format(initialTime).toString(), dtf)
                val date2: LocalDate = LocalDate.parse(dtf.format(finalTime).toString(), dtf)
                daysBetween = ChronoUnit.DAYS.between(date1, date2)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            text_total.text =  daysBetween.toString()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
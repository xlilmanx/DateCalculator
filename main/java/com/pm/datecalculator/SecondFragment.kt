package com.pm.datecalculator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.pm.datecalculator.databinding.FragmentFirstBinding
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class SecondFragment : Fragment(R.layout.fragment_second) {

    private lateinit var binding: FragmentFirstBinding
    private lateinit var initialDate: LocalDate
    private lateinit var finalDate: LocalDate
    private val currentTimeMillis = System.currentTimeMillis()
    private val timeZoneOffset: Int = TimeZone.getDefault().getOffset(currentTimeMillis)
    private val zoneID = ZoneId.systemDefault()

    private val currentDateInMillis: Long
        get() = currentTimeMillis + timeZoneOffset

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialDate = Instant.ofEpochMilli(currentDateInMillis - timeZoneOffset)
            .atZone(zoneID).toLocalDate()

        finalDate = Instant.ofEpochMilli(currentDateInMillis - timeZoneOffset)
            .atZone(zoneID).toLocalDate()

        binding.apply {
            textInitial.text = DATE_FORMAT.format(initialDate)
            textFinal.text = DATE_FORMAT.format(initialDate)
            buttonInitial.setOnClickListener { showDatePicker(true) }
            buttonFinal.setOnClickListener { showDatePicker(false) }
        }
    }

    private fun showDatePicker(isInitial: Boolean) {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(if (isInitial) "Select initial date" else "Select final date")
            .setSelection(if (isInitial) initialDate.atStartOfDay().atZone(zoneID).toInstant().toEpochMilli() else finalDate.atStartOfDay().atZone(zoneID).toInstant().toEpochMilli())
            .build()
        datePicker.addOnPositiveButtonClickListener { dateInMillis ->
            val date = Instant.ofEpochMilli(dateInMillis - timeZoneOffset)
                .atZone(zoneID).toLocalDate()
            if (isInitial) {
                initialDate = date
                binding.textInitial.text = DATE_FORMAT.format(date)
                updateDaysBetween()
            } else {
                finalDate = date
                binding.textFinal.text = DATE_FORMAT.format(date)
                updateDaysBetween()
            }
        }
        datePicker.show(parentFragmentManager, "datePicker")
    }

    private fun updateDaysBetween() {
        val daysBetween = ChronoUnit.DAYS.between(initialDate, finalDate)
        binding.textNumberDays.text = daysBetween.toString()
    }

    companion object {
        private val DATE_FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
    }
}


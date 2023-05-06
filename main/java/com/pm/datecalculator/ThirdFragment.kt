package com.pm.datecalculator

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import com.pm.datecalculator.databinding.FragmentThirdBinding


/**
 * A simple [Fragment] subclass as the third destination in the navigation.
 */
class ThirdFragment : Fragment() {

    private var _binding: FragmentThirdBinding? = null
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

        _binding = FragmentThirdBinding.inflate(inflater, container, false)
        return binding.root

    }

    @SuppressLint("RestrictedApi", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val TextViewInputOne: AutoCompleteTextView = view.findViewById(R.id.acinputone) as AutoCompleteTextView
        TextViewInputOne.setText("Liter", false)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
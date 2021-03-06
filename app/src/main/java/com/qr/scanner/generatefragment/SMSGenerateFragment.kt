package com.qr.scanner.generatefragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider


import com.qr.scanner.R
import com.qr.scanner.activity.ViewQrCodeActivity
import com.qr.scanner.extension.isNotBlank
import com.qr.scanner.extension.textString
import com.qr.scanner.extension.unsafeLazy
import com.qr.scanner.model.Parsers
import com.qr.scanner.model.Sms
import com.qr.scanner.preference.UserPreferences
import com.qr.scanner.viewmodel.HistoryViewModel
import kotlinx.android.synthetic.main.fragment_s_m_s_generate.edit_text_message
import kotlinx.android.synthetic.main.fragment_s_m_s_generate.edit_text_phone
import kotlinx.android.synthetic.main.fragment_s_m_s_generate.generateQrcode


class SMSGenerateFragment : Fragment() {

    private val viewModel by lazy {
        ViewModelProvider(this).get(HistoryViewModel::class.java)
    }

    private val userPreferences by unsafeLazy {
        UserPreferences(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_s_m_s_generate, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toggleCreateBarcodeButton()
        initTitleEditText()
        handleTextChanged()

        generateQrcode?.setOnClickListener {
           createQrCode(parse())
        }
    }

    private fun createQrCode(parse: Parsers) {
        val result = com.qr.scanner.model.Result(
            text = parse.toBarcodeText(),
            formattedText = parse.toFormattedText(),
            format = com.google.zxing.BarcodeFormat.QR_CODE,
            parse = parse.parser,
            date = System.currentTimeMillis(),
            isGenerated = true
        )
        viewModel.insert(result,userPreferences?.doNotSaveDuplicates)
        ViewQrCodeActivity.start(requireContext(), result)

    }

    private fun parse(): Parsers {
        return Sms(edit_text_phone.textString,edit_text_message.textString)
    }

    private fun initTitleEditText() {
        edit_text_phone.requestFocus()
    }

    private fun handleTextChanged() {
        edit_text_phone.addTextChangedListener { toggleCreateBarcodeButton() }
        edit_text_message.addTextChangedListener { toggleCreateBarcodeButton() }
    }

    private fun toggleCreateBarcodeButton() {
        if(edit_text_phone.isNotBlank() || edit_text_message.isNotBlank()){
            generateQrcode?.isEnabled = true
            generateQrcode?.isClickable = true
            generateQrcode?.background = resources?.getDrawable(R.drawable.button_background_1)
        } else {
            generateQrcode?.isEnabled = false
            generateQrcode?.isClickable = false
            generateQrcode?.background = resources?.getDrawable(R.drawable.circle_button_background)
        }
    }

}
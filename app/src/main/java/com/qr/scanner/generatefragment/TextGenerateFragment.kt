package com.qr.scanner.generatefragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.google.zxing.BarcodeFormat
import com.qr.scanner.R
import com.qr.scanner.activity.ViewQrCodeActivity
import com.qr.scanner.extension.isNotBlank
import com.qr.scanner.extension.textString
import com.qr.scanner.extension.unsafeLazy
import com.qr.scanner.model.Other
import com.qr.scanner.model.Parsers
import com.qr.scanner.preference.UserPreferences
import com.qr.scanner.viewmodel.HistoryViewModel
import kotlinx.android.synthetic.main.fragment_text_generate.*
import kotlinx.android.synthetic.main.fragment_text_generate.generateQrcode

class TextGenerateFragment : Fragment() {

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
        return inflater.inflate(R.layout.fragment_text_generate, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toggleCreateBarcodeButton()
        handleTextChanged()
        initEditText()

        generateQrcode?.setOnClickListener {
            createQrCode(parse())
        }
    }

    private fun createQrCode(parse: Parsers) {
        val result = com.qr.scanner.model.Result(
            text = parse.toBarcodeText(),
            formattedText = parse.toFormattedText(),
            format = BarcodeFormat.QR_CODE,
            parse = parse.parser,
            date = System.currentTimeMillis(),
            isGenerated = true
        )
        viewModel.insert(result,userPreferences?.doNotSaveDuplicates)
        ViewQrCodeActivity.start(requireContext(), result)

    }

    private fun parse(): Parsers {
        return Other(edit_text.textString)
    }

    private fun initEditText() {
        edit_text.requestFocus()

    }

    private fun handleTextChanged() {
        edit_text.addTextChangedListener {
          toggleCreateBarcodeButton()
        }
    }

    private fun toggleCreateBarcodeButton(){
        if (edit_text.isNotBlank()) {
            generateQrcode?.isEnabled = true
            generateQrcode?.isClickable = true
            generateQrcode?.background = resources?.getDrawable(R.drawable.button_background_1)
        } else {
            generateQrcode?.isEnabled = false
            generateQrcode?.isClickable = false
            generateQrcode?.background =
                resources?.getDrawable(R.drawable.circle_button_background)

        }
    }
}
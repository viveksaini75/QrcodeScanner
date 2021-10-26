package com.qr.scanner.activity

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.MenuItem
import com.core.Result
import com.core.client.result.ParsedResultType
import com.qr.scanner.R
import com.qr.scanner.result.ResultHandlerFactory
import kotlinx.android.synthetic.main.toolbar.*
import com.core.WriterException
import com.qr.scanner.qrencoder.QRGContents
import com.qr.scanner.qrencoder.QRGEncoder
import kotlinx.android.synthetic.main.activity_view_barcode.*
import kotlinx.android.synthetic.main.activity_view_qr_code.*
import com.core.BarcodeFormat
import com.core.MultiFormatWriter

import com.core.common.BitMatrix
import com.qr.scanner.constant.RESULT
import com.qr.scanner.utils.saveImageToGallery
import com.qr.scanner.utils.shareBitmap
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.IllegalArgumentException
import java.lang.NullPointerException


class ViewQRcodeActivity : AppCompatActivity() {
    private var result: Result? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_qr_code)

        if (toolbar != null) {
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        if (intent.extras != null) {
            result = intent.getParcelableExtra(RESULT) as Result?
        }

        val resultHandler = ResultHandlerFactory.makeResultHandler(this, result)

        when (resultHandler.type) {
            ParsedResultType.WIFI -> {
                toolbar?.title = "Wifi"
            }
            ParsedResultType.SMS -> {
                toolbar?.title = "SMS"
            }
            ParsedResultType.URI -> {
                toolbar?.title = "Website"
            }
            ParsedResultType.TEL -> {
                toolbar?.title = "Phone"
            }
            ParsedResultType.EMAIL_ADDRESS -> {
                toolbar?.title = "Email"
            }
            ParsedResultType.ADDRESSBOOK -> {
                toolbar?.title = "Contact"
            }
            ParsedResultType.CALENDAR -> {
                toolbar?.title = "Calender"
            }
            ParsedResultType.TEXT -> {
                toolbar?.title = "Text"
            }
        }
        val image: Bitmap? = generateQr(result.toString(),640,640)
        qrImage?.setImageBitmap(image)

        saveQrCodeLayout?.setOnClickListener {
            saveImageToGallery(applicationContext,image)
        }
        shareQrcodeLayout?.setOnClickListener {
            shareBitmap(this,image)
        }
    }

    @Throws(WriterException::class, NullPointerException::class)
    private fun generateQr(text: String, width: Int, height: Int): Bitmap? {
        val bitMatrix: BitMatrix = try {
            MultiFormatWriter().encode(
                text, BarcodeFormat.QR_CODE,
                width, height, null
            )
        } catch (Illegalargumentexception: IllegalArgumentException) {
            return null
        }
        val bitMatrixWidth = bitMatrix.width
        val bitMatrixHeight = bitMatrix.height
        val pixels = IntArray(bitMatrixWidth * bitMatrixHeight)
        val colorWhite = -0x1
        val colorBlack = -0x1000000
        for (y in 0 until bitMatrixHeight) {
            val offset = y * bitMatrixWidth
            for (x in 0 until bitMatrixWidth) {
                pixels[offset + x] = if (bitMatrix[x, y]) colorBlack else colorWhite
            }
        }
        val bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444)
        bitmap.setPixels(pixels, 0, width, 0, 0, bitMatrixWidth, bitMatrixHeight)
        return bitmap
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
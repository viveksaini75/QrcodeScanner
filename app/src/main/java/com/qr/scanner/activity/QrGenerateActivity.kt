package com.qr.scanner.activity

import android.os.Bundle
import android.view.MenuItem
import com.qr.scanner.R
import com.qr.scanner.constant.TYPE
import com.qr.scanner.generatefragment.*
import com.qr.scanner.utils.loadFragment
import kotlinx.android.synthetic.main.toolbar.*

class QrGenerateActivity : BaseActivity() {

    private var type: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrgenerate)

        if (intent.extras != null) {
            type = intent.getStringExtra(TYPE)
        }

        if (toolbar != null) {
            toolbar?.title = "Generate"
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        when (type) {

            "email" -> {
                toolbar?.title = "Email"
                EmailGenerateFragment().loadFragment(this, R.id.container)
            }
            "text" -> {
                toolbar?.title = "Text"
                TextGenerateFragment().loadFragment(this, R.id.container)
            }
            "website" -> {
                toolbar?.title = "Website"
                UrlGenerateFragment().loadFragment(this, R.id.container)
            }
            "contact" -> {
                ContactGenerateFragment().loadFragment(this, R.id.container)
                toolbar?.title = "Contact"
            }
            "event" -> {
                EventGenerateFragment().loadFragment(this, R.id.container)
                toolbar?.title = "Event"
            }
            "wifi" -> {
                WifiGenerateFragment().loadFragment(this, R.id.container)
                toolbar?.title = "Wifi"
            }
            "phone" -> {
                PhoneGenerateFragment().loadFragment(this, R.id.container)
                toolbar?.title = "Phone"
            }
            "sms" -> {
                SMSGenerateFragment().loadFragment(this, R.id.container)
                toolbar?.title = "Sms"
            }
            "app" -> {
                AppGenerateFragment().loadFragment(this, R.id.container)
                toolbar?.title = "App"
            }

        }
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
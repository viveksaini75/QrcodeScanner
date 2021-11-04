package com.qr.scanner.model

import com.qr.scanner.extension.removePrefixIgnoreCase
import com.qr.scanner.extension.startsWithIgnoreCase


class Phone(val phone: String) : Parsers {

    companion object {
        private const val PREFIX = "tel:"

        fun parse(text: String): Phone? {
            if (text.startsWithIgnoreCase(PREFIX).not()) {
                return null
            }

            val phone = text.removePrefixIgnoreCase(PREFIX)
            return Phone(phone)
        }
    }

    override val parser = ParsedResultType.PHONE
    override fun toFormattedText(): String = phone
    override fun toBarcodeText(): String = "$PREFIX$phone"
}
package com.andyha.coreextension

import android.content.Context
import android.provider.MediaStore
import androidx.annotation.Nullable
import com.andyha.coreextension.utils.BaseTextUtils
import com.andyha.coreextension.utils.ValidationRegex
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.Phonenumber
import timber.log.Timber
import java.io.File
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.security.KeyStore
import java.text.Normalizer
import java.util.*
import java.util.regex.Pattern



private fun String.newEmptyKeyStore(): KeyStore? {
    return KeyStore.getInstance(KeyStore.getDefaultType())
}

fun String.capitalizeFirstChar(): String {
    return this.split(" ").joinToString(" ") {
        it.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale.getDefault()
            ) else it.toString()
        }
    }.trimEnd()
}

fun String.replaceAllNewLines(prefix: String = " "): String {
    return this.replace("\\r?\\n|\\r".toRegex(), prefix)
}

fun String.findIndex(fromLast: Boolean): ((String, Int, Boolean) -> Int) =
    if (fromLast) ::lastIndexOf else ::indexOf

fun CharSequence.firstIndexOf(
    subStr: String?,
    enableWithoutAccents: Boolean = false,
    ignoreCase: Boolean = true,
    enableOverlapped: Boolean = true,
): Int? {
    return indexesOf(
        subStr,
        enableWithoutAccents,
        ignoreCase,
        enableOverlapped,
        false,
        1
    ).firstOrNull()
}

fun CharSequence.lastIndexOf(
    subStr: String?,
    enableWithoutAccents: Boolean = false,
    ignoreCase: Boolean = true,
    enableOverlapped: Boolean = true,
): Int? {
    return indexesOf(
        subStr,
        enableWithoutAccents,
        ignoreCase,
        enableOverlapped,
        true,
        1
    ).lastOrNull()
}

fun CharSequence.indexesOf(
    subStr: String?,
    enableWithoutAccents: Boolean = false,
    ignoreCase: Boolean = true,
    enableOverlapped: Boolean = true,
    fromLast: Boolean = false,
    maxItems: Int? = null,
): List<Int> {
    return this.toString()
        .indexesOf(subStr, enableWithoutAccents, ignoreCase, enableOverlapped, fromLast, maxItems)
}

fun String.firstIndexOf(
    subStr: String?,
    enableWithoutAccents: Boolean = false,
    ignoreCase: Boolean = true,
    enableOverlapped: Boolean = true,
): Int? {
    return indexesOf(
        subStr,
        enableWithoutAccents,
        ignoreCase,
        enableOverlapped,
        false,
        1
    ).firstOrNull()
}

fun String.lastIndexOf(
    subStr: String?,
    enableWithoutAccents: Boolean = false,
    ignoreCase: Boolean = true,
    enableOverlapped: Boolean = true,
): Int? {
    return indexesOf(
        subStr,
        enableWithoutAccents,
        ignoreCase,
        enableOverlapped,
        true,
        1
    ).lastOrNull()
}

fun String.indexesOf(
    subStr: String?,
    enableWithoutAccents: Boolean = false,
    ignoreCase: Boolean = true,
    enableOverlapped: Boolean = true,
    fromLast: Boolean = false,
    maxItems: Int? = null,
): List<Int> {
    val list = mutableListOf<Int>()

    if (subStr.isNullOrBlank()) return list

    try {
        val orgSrc = this.toString()
        val engSrc = BaseTextUtils.convertToEnglish(orgSrc)
        val engSubStr = BaseTextUtils.convertToEnglish(subStr)

        val step = when {
            enableOverlapped && !fromLast -> 1
            enableOverlapped && fromLast -> -1
            !enableOverlapped && !fromLast -> subStr.length
            else -> -(subStr.length)
        }

        var i = (if (fromLast) length else -1) - step // initial index for first round

        while (true) {
            val iOrg = orgSrc.findIndex(fromLast)(subStr, i + step, ignoreCase)
            val iEng = engSrc.findIndex(fromLast)(engSubStr, i + step, ignoreCase)

            when {
                iEng != -1 && enableWithoutAccents -> {
                    i = iEng
                    list.add(iEng)
                }
                iOrg != -1 -> {
                    i = iOrg
                    list.add(iOrg)
                }
                else -> {
                    return list
                }
            }

            if (maxItems != null && list.size == maxItems) {
                return list
            }
        }

    } catch (e: Exception) {
        Timber.d("exception in indexOf, text=$this, substring=$subStr")
        return list
    }
}


fun String.isValidEmail(
    context: Context,
    acceptEmpty: Boolean = false,
    fieldTitle: String? = null,
    resultMessage: (String) -> Unit
): Boolean {
    return when {
        this.isEmpty() -> { acceptEmpty }

        !Pattern.matches(ValidationRegex.REGEX_EMAIL, this) -> false

        else -> true
    }
}

fun String.isValidPassword(
    context: Context,
    fieldTitle: String? = null,
): Boolean {
    return when {
        this.isEmpty() -> {
            false
        }

        this.length < ValidationRegex.LIMIT_SHORTEST_PASSWORD_LENGTH -> {
            false
        }

        Pattern.matches(ValidationRegex.REGEX_PASS_WORD_CONTAIN_NUMBER, this)
                && Pattern.matches(ValidationRegex.REGEX_PASS_WORD_CONTAIN_LOWER_CASE, this)
                && Pattern.matches(ValidationRegex.REGEX_PASS_WORD_CONTAIN_UPPER_CASE, this) -> {
            true
        }
        else -> false
    }
}


fun String.isValidPhoneNumber(
    context: Context,
    acceptEmpty: Boolean = false,
    fieldTitle: String? = null,
): Boolean {
    return when {
        this.trim().isEmpty() -> { acceptEmpty }

        else -> {
            try {
                val phoneNumberUtil = PhoneNumberUtil.getInstance()
                val phoneNumberDetect = phoneNumberUtil.parse(
                    this,
                    Phonenumber.PhoneNumber.CountryCodeSource.UNSPECIFIED.name
                )
                phoneNumberUtil.isValidNumber(phoneNumberDetect)
            } catch (ex: Exception) {
                false
            }
        }
    }
}

fun String.convertToLocale(): Locale {
    return try {
        val tag = this.replace("_", "-")
        Locale.forLanguageTag(tag)
    } catch (e: Exception) {
        Locale(this)
    }
}

fun String?.removeImageFileByPath(context: Context) {
    this?.let {
        try {
            val imageCrop = File(it)
            if (imageCrop.exists()) {
                val result = imageCrop.delete()
                Timber.d("Delete file \"$this\" result: $result")
                if (result) {
                    val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    val contentResolver = context.contentResolver
                    val url = MediaStore.Images.Media.DATA + "=?"
                    val deleteRows = contentResolver.delete(uri, url, arrayOf(it))
                    Timber.d("Delete image data \"$this\" result: $deleteRows")
                }
            }
        } catch (er: Exception) {

        }
    } ?: kotlin.run {
        Timber.d("The image path is null")
    }
}

fun String.unAccent(): String {
    val output = StringBuffer()
    val pattern = Pattern.compile(
        "(?:[${ValidationRegex.VIETNAMESE_DIACRITIC_CHARACTERS}]|[A-Z])++",
        Pattern.CASE_INSENSITIVE or Pattern.UNICODE_CASE
    )
    val m = pattern.matcher(Normalizer.normalize(this, Normalizer.Form.NFD))
    while (m.find()) {
        output.append(m.group())
    }
    return if (output.toString().isNotEmpty()) output.toString().replace(" ", "") else this
}

fun String.cleanPhoneNumber(): String? {
    try {
        val phoneNumberUtil = PhoneNumberUtil.getInstance()
        val phoneNumberDetect = phoneNumberUtil.parse(
            this,
            Phonenumber.PhoneNumber.CountryCodeSource.UNSPECIFIED.name
        )
        return phoneNumberUtil.format(
            phoneNumberDetect,
            PhoneNumberUtil.PhoneNumberFormat.E164
        )
    } catch (ex: java.lang.Exception) {
        ex.printStackTrace()
    }
    return null
}

fun Array<String?>?.convertToString(): String? {
    var result: String? = null
    if (this.isNullOrEmpty()) {
        return result
    }
    this.forEach {
        it?.let { content ->
            result = if (result.isNullOrEmpty()) {
                content
            } else {
                result.plus(", ").plus(content)
            }
        }
    }
    return result
}

/**
 * Format phone number: Using for show phone number or format data send to server
 *
 * E164: "+84987654321" :Using to format data send to server
 * INTERNATIONAL: "+84 987 654 321"
 * NATIONAL: "0987 654 321"
 * RFC3966: "tel:+84-987-654-321"
 *
 * If phone number is hotline -> using PhoneNumberUtil.PhoneNumberFormat.NATIONAL
 *
 * @param context
 * @param phoneFormat
 * @return
 */
fun String?.formatPhoneNumber(
    context: Context? = null,
    phoneFormat: PhoneNumberUtil.PhoneNumberFormat = PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL
): String? {
    if (this.isNullOrEmpty()) {
        return this
    }
    val rawData = this.trim()
    if (rawData.isEmpty()) return ""

    var country: String? = context.getUserCountry()

    if (country.isNullOrEmpty()) {
        country = "VN"
    }

    return try {
        val phoneNumberUtil = PhoneNumberUtil.getInstance()
        val phoneNumber = phoneNumberUtil.parse(rawData, country?.toUpperCase(Locale.ROOT))
        val numberFormat = phoneNumberUtil.format(
            phoneNumber,
            phoneFormat
        )
        Timber.d("Format Phone Number -- $numberFormat -- ${Locale.getDefault().country}")
        numberFormat
    } catch (ex: Exception) {
        ex.printStackTrace()
        rawData
    }
}

fun String?.formatTitleToMessage(): String? {
    if (this?.endsWith("*") == true) {
        return this.substring(0, this.length - 1)
    }
    return this
}

/**
 * issue: https://stackoverflow.com/questions/68621404/android-12-beta-cannot-view-pdf-link-with-customtabsintent
 * issue: https://stackoverflow.com/questions/63250795/pdf-no-preview-available-in-android-webview
 */
fun String.makeGoogleDocPreviewUrl(): String {
    //Url Convert to UTF-8 It important.
    val encodeUrl = try {
        URLEncoder.encode(this, "UTF-8")
    } catch (ex: UnsupportedEncodingException) {
        this
    }
    return "https://docs.google.com/gview?embedded=true&url=$encodeUrl"
}

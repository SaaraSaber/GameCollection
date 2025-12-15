package ir.developer.goalorpooch_compose.feature.home.presentation.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.net.toUri
import ir.developer.goalorpooch_compose.BuildConfig
import ir.developer.goalorpooch_compose.R
import ir.developer.goalorpooch_compose.core.util.Utils

fun Context.openMarket() {
    if (BuildConfig.FLAVOR == "bazar") {
        try {
            val intent = Intent(Intent.ACTION_EDIT)
            intent.setData(("bazaar://details?id=" + Utils.PACKAGE_NAME).toUri())
            intent.setPackage("com.farsitel.bazaar")
            startActivity(intent)
        } catch (t: Throwable) {
            Toast.makeText(
                this,
                "از نصب برنامه بازار مطمئن شوید",
                Toast.LENGTH_LONG
            ).show()
        }
    } else {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setData(("myket://comment?id=" + Utils.PACKAGE_NAME).toUri())
            startActivity(intent)
        } catch (t: Throwable) {
            Toast.makeText(
                this,
                "از نصب برنامه مایکت مطمئن شوید",
                Toast.LENGTH_LONG
            ).show()
        }
    }

}

fun Context.openEmail() {
    try {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = "mailto:".toUri()
            putExtra(
                Intent.EXTRA_EMAIL,
                arrayOf(this@openEmail.getString(R.string.address_email))
            )
            putExtra(
                Intent.EXTRA_SUBJECT,
                this@openEmail.getString(R.string.support)
            )
        }
        startActivity(intent)
    } catch (t: Throwable) {
        Toast.makeText(
            this,
            "ارتباط دچار مشکل شده است",
            Toast.LENGTH_LONG
        ).show()
    }
}

fun Context.openAppInMarket(packageName: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW)
        // لینک استاندارد بازار برای یک برنامه خاص
        intent.data = Uri.parse("bazaar://details?id=$packageName")
        intent.setPackage("com.farsitel.bazaar")
        startActivity(intent)
    } catch (e: Exception) {
        // اگر بازار نصب نبود، لینک وب را باز کن
        val webIntent =
            Intent(Intent.ACTION_VIEW, Uri.parse("https://cafebazaar.ir/app/$packageName"))
        startActivity(webIntent)
    }
}
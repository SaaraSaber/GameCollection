package ir.developer.goalorpooch_compose.feature.home.presentation.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import ir.developer.goalorpooch_compose.BuildConfig
import ir.developer.goalorpooch_compose.core.util.Utils

fun openMarket(context: Context) {
    if (BuildConfig.FLAVOR == "bazar") {
        try {
            val intent = Intent(Intent.ACTION_EDIT)
            intent.setData(Uri.parse("bazaar://details?id=" + Utils.PACKAGE_NAME))
            intent.setPackage("com.farsitel.bazaar")
            context.startActivity(intent)
        } catch (t: Throwable) {
            Toast.makeText(
                context,
                "از نصب برنامه بازار مطمئن شوید",
                Toast.LENGTH_LONG
            ).show()
        }
    } else {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse("myket://comment?id=" + Utils.PACKAGE_NAME))
            context.startActivity(intent)
        } catch (t: Throwable) {
            Toast.makeText(
                context,
                "از نصب برنامه مایکت مطمئن شوید",
                Toast.LENGTH_LONG
            ).show()
        }
    }

}
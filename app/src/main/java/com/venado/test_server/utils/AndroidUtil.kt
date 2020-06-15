package com.venado.test_server.utils

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import timber.log.Timber
import java.io.BufferedReader
import java.io.InputStreamReader
import com.venado.test_server.ui.App
import java.io.InputStream

object AndroidUtil {

    const val UTF8 = "UTF-8"

    fun openLink(context: Context, link: String) {
        openLink(context, link, "")
    }

    fun openLink(context: Context, link: String, mime: String) {
        val i = Intent()
        i.action = Intent.ACTION_VIEW
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            i.data = Uri.parse(link)
            if (!TextUtils.isEmpty(mime)) {
                i.type = mime
            }
            context.startActivity(i)
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    val appVersion: String = getAppVersion(App.appInstance)

    fun getAppVersion(context: Context?): String {
        val packageName = context?.packageName ?: ""
        try {
            return context?.packageManager?.getPackageInfo(packageName, 0)?.versionName ?: ""
        } catch (t: Throwable) {
            Timber.e(t)
        }
        return ""
    }

    fun readAsset(context: Context?, fileName: String): String {
        val manager = context?.assets
        val sb = StringBuilder()
        try {
            val iStream = manager?.open(fileName)
            val br = BufferedReader(InputStreamReader(iStream, UTF8))
            var str: String?
            while (true) {
                str = br.readLine()
                if (str == null) {
                    break
                }
                sb.append(str)
            }
            br.close()
        } catch (e: Exception) {
            Timber.e(e)
        }
        return sb.toString()
    }

    fun readAssetStream(context: Context?, fileName: String): InputStream? {
        val manager = context?.assets
        var iStream: InputStream? = null
        try {
            iStream = manager?.open(fileName)
        } catch (e: Exception) {
            Timber.e(e)
        }
        return iStream
    }

    fun printAppMemoryInfo(context: Context?) {
        val activityManager = context?.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memClass= activityManager?.memoryClass
        val largeMemClass= activityManager?.largeMemoryClass
        Timber.i("[MEMORY] memClass: $memClass largeMemClass: $largeMemClass")
    }
}
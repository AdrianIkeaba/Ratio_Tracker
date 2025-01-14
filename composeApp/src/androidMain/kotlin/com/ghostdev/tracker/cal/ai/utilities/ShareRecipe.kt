package com.ghostdev.tracker.cal.ai.utilities

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri

class AndroidSystemService(private val context: Context) : SystemService {
    @SuppressLint("QueryPermissionsNeeded")
    override fun shareRecipe(shareLink: String) {
        val intent = Intent().apply {
            type = "text/plain"
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareLink)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        try {
            context.startActivity(intent)
        } catch (ex: ActivityNotFoundException) {
            println(ex.toString())
        }
    }

    override fun openUrl(url: String) {
        try {
            val webpage = Uri.parse(url)
            val intent = Intent(Intent.ACTION_VIEW, webpage).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            println("Error opening URL: ${e.message}")
        }
    }
}
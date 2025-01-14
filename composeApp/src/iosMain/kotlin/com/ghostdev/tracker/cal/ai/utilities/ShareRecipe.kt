package com.ghostdev.tracker.cal.ai.utilities

import platform.Foundation.NSURL
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import platform.UIKit.UIViewController
import platform.UIKit.UIWindow
import platform.UIKit.popoverPresentationController


class IOSSystemService : SystemService {
    override fun shareRecipe(shareLink: String) {
        val activityController = UIActivityViewController(
            activityItems = listOf(shareLink),
            applicationActivities = null
        )
        val window = UIApplication.sharedApplication.windows().first() as UIWindow?
        activityController.popoverPresentationController()?.sourceView = window
        window?.rootViewController?.presentViewController(
            activityController as UIViewController,
            animated = true,
            completion = null
        )
    }

    override fun openUrl(url: String) {
        try {
            val nsUrl = NSURL.URLWithString(url)
            nsUrl?.let {
                UIApplication.sharedApplication.openURL(it)
            }
        } catch (e: Exception) {
            println("Error opening URL: ${e.message}")
        }
    }
}
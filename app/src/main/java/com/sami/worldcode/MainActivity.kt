package com.sami.worldcode

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Native/offline start screen: no WebView and no external URL,
        // so the app cannot show an HTTP 404 on startup.
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(40, 40, 40, 40)
            setBackgroundColor(Color.WHITE)
        }

        val title = TextView(this).apply {
            text = "WorldCode"
            textSize = 36f
            gravity = Gravity.CENTER
        }

        val version = packageManager.getPackageInfo(packageName, 0).versionName ?: "0.0.1"
        val info = TextView(this).apply {
            text = "Version $version\n\nWorldCode ist gestartet!"
            textSize = 20f
            gravity = Gravity.CENTER
            setPadding(0, 24, 0, 0)
        }

        root.addView(title)
        root.addView(info)
        setContentView(root)
    }
}

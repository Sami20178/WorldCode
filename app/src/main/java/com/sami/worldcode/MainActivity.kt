package com.sami.worldcode

import android.app.Activity
import android.os.Bundle
import android.widget.TextView

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val version = packageManager.getPackageInfo(packageName, 0).versionName ?: "0.0.1"
        val text = TextView(this).apply {
            text = "WorldCode\n\nVersion $version\n\nWorldCode wird gestartet..."
            textSize = 24f
            setPadding(32, 48, 32, 32)
        }
        setContentView(text)
    }
}

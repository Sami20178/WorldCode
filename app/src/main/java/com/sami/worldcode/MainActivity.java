package com.sami.worldcode;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.core.content.FileProvider;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class MainActivity extends Activity {
    private static final String VERSION = "0.0.2";
    private static final String RELEASE_API = "https://api.github.com/repos/Sami20178/WorldCode/releases/latest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WebView webView = new WebView(this);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("file:///android_asset/index.html");
        setContentView(webView);
        checkForUpdate(false);
    }

    private void checkForUpdate(boolean manual) {
        new Thread(() -> {
            try {
                HttpURLConnection c = (HttpURLConnection) new URL(RELEASE_API).openConnection();
                c.setRequestProperty("Accept", "application/vnd.github+json");
                c.setConnectTimeout(7000); c.setReadTimeout(7000);
                JSONObject release = new JSONObject(read(c.getInputStream()));
                String tag = release.optString("tag_name", "").replace("v", "");
                String apkUrl = "";
                for (Object o : release.getJSONArray("assets")) {
                    JSONObject a = (JSONObject)o;
                    if (a.optString("name").endsWith(".apk")) { apkUrl = a.optString("browser_download_url"); break; }
                }
                if (!tag.isEmpty() && isNewer(tag, VERSION) && !apkUrl.isEmpty()) {
                    String finalApkUrl = apkUrl;
                    runOnUiThread(() -> new AlertDialog.Builder(this)
                        .setTitle("Aktualisierung verfügbar")
                        .setMessage("WorldCode " + tag + " ist verfügbar. Jetzt aktualisieren?")
                        .setNegativeButton("Später", null)
                        .setPositiveButton("Aktualisieren", (d,w) -> downloadAndInstall(finalApkUrl, tag))
                        .show());
                } else if (manual) runOnUiThread(() -> new AlertDialog.Builder(this).setTitle("WorldCode").setMessage("Du hast bereits die neueste Version.").setPositiveButton("OK", null).show());
            } catch (Exception e) { if (manual) runOnUiThread(() -> new AlertDialog.Builder(this).setTitle("Update").setMessage("Update konnte nicht geprüft werden.").setPositiveButton("OK", null).show()); }
        }).start();
    }

    private boolean isNewer(String a, String b) {
        try { return Integer.parseInt(a.replace(".", "")) > Integer.parseInt(b.replace(".", "")); }
        catch (Exception e) { return !a.equals(b); }
    }

    private void downloadAndInstall(String url, String version) {
        new Thread(() -> {
            try {
                HttpURLConnection c = (HttpURLConnection)new URL(url).openConnection();
                c.connect();
                File apk = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "WorldCode-" + version + ".apk");
                try (InputStream in = c.getInputStream(); FileOutputStream out = new FileOutputStream(apk)) {
                    byte[] buf = new byte[8192]; int n; while ((n = in.read(buf)) != -1) out.write(buf, 0, n);
                }
                Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", apk);
                Intent i = new Intent(Intent.ACTION_VIEW, uri); i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); startActivity(i);
            } catch (Exception e) { runOnUiThread(() -> new AlertDialog.Builder(this).setTitle("Update-Fehler").setMessage(e.getMessage()).setPositiveButton("OK", null).show()); }
        }).start();
    }

    private String read(InputStream in) throws Exception { java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream(); byte[] b = new byte[4096]; int n; while((n=in.read(b))!=-1) out.write(b,0,n); return out.toString("UTF-8"); }
}

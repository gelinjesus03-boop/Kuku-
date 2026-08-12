package com.example.whatsdiagnostic

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import java.io.File

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etPhone = findViewById<EditText>(R.id.etPhone)
        val btnCollect = findViewById<Button>(R.id.btnCollect)
        val btnOpenPlay = findViewById<Button>(R.id.btnOpenPlay)
        val btnLaunchWA = findViewById<Button>(R.id.btnLaunchWA)
        val btnContact = findViewById<Button>(R.id.btnContactSupport)
        val tvStatus = findViewById<TextView>(R.id.tvStatus)

        btnCollect.setOnClickListener {
            val info = collectDiagnostics()
            val f = File(cacheDir, "diagnostic.txt")
            f.writeText(info)
            tvStatus.text = "Diagnostic sauvegardé: ${f.absolutePath}"
        }

        btnOpenPlay.setOnClickListener {
            val appPackage = "com.whatsapp"
            try {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackage")))
            } catch (e: Exception) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appPackage")))
            }
        }

        btnLaunchWA.setOnClickListener {
            val pm = packageManager
            val launch = pm.getLaunchIntentForPackage("com.whatsapp")
            if (launch != null) {
                startActivity(launch)
            } else {
                tvStatus.text = "WhatsApp non installé."
            }
        }

        btnContact.setOnClickListener {
            val phone = etPhone.text.toString().trim()
            val body = buildSupportBody(phone)
            // prepare file
            val f = File(cacheDir, "diagnostic.txt")
            if (!f.exists()) f.writeText(collectDiagnostics())

            val uri = FileProvider.getUriForFile(this, "${packageName}.fileprovider", f)

            val emailIntent = Intent(Intent.ACTION_SEND).apply {
                type = "message/rfc822"
                putExtra(Intent.EXTRA_EMAIL, arrayOf("support@whatsapp.com"))
                putExtra(Intent.EXTRA_SUBJECT, "Demande concernant possible suspension du numéro")
                putExtra(Intent.EXTRA_TEXT, body)
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            startActivity(Intent.createChooser(emailIntent, "Envoyer l'e‑mail via"))
        }
    }

    private fun collectDiagnostics(): String {
        val sb = StringBuilder()
        sb.append("Device manufacturer/model: ${Build.MANUFACTURER} ${Build.MODEL}\n")
        sb.append("Android version: ${Build.VERSION.RELEASE} (SDK ${Build.VERSION.SDK_INT})\n")
        val pm = packageManager
        try {
            val info = pm.getPackageInfo("com.whatsapp", 0)
            sb.append("WhatsApp installed: yes, version ${info.versionName}\n")
        } catch (e: PackageManager.NameNotFoundException) {
            sb.append("WhatsApp installed: no\n")
        }
        sb.append("App package: ${applicationContext.packageName}\n")
        sb.append("Android ID: ${Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)}\n")
        sb.append("Timestamp: ${System.currentTimeMillis()}\n")
        sb.append("\nNotes: This file does not include personal messages or private tokens.\n")
        return sb.toString()
    }

    private fun buildSupportBody(phone: String): String {
        val diag = collectDiagnostics()
        return """
Bonjour,
Mon numéro: $phone

Je rencontre un problème d'activation/suspension sur WhatsApp. Merci de trouver ci‑dessous des informations de diagnostic :

$diag

Je n'ai pas violé volontairement les conditions d'utilisation et souhaite connaître la raison du blocage et les actions possibles.

Cordialement,
""".trimIndent()
    }
}

package ch.hslu.mobpro.intentfilterandwidget

import android.os.Bundle
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity


class MyBrowsingActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // view creation without layout inflation
        val webView = WebView(this)
        setContentView(webView)

        webView.loadUrl(intent.dataString?:"https://sbb.ch")

        actionBar?.setDisplayShowHomeEnabled(true)
        actionBar?.setIcon(R.mipmap.ic_launcher)
    }

}
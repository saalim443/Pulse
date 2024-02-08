package codeflies.com.pulse.Settings

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import codeflies.com.pulse.Helpers.Constants
import codeflies.com.pulse.databinding.ActivityWebviewBinding

class WebviewActivity : AppCompatActivity() {

    lateinit var binding: ActivityWebviewBinding
    companion object{
        lateinit var pageTitle: String
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityWebviewBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.back.setOnClickListener {
            finish()
        }

        binding.title.text=pageTitle


        binding.webview.webViewClient=webClient()
        binding.webview.webChromeClient= WebChromeClient()

        binding.progressBar.visibility= View.VISIBLE
        binding.webview.loadUrl(Constants.HELP_URL)

    }

    inner class webClient : WebViewClient(){
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            return super.shouldOverrideUrlLoading(view, url)
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            binding.progressBar.visibility= View.VISIBLE
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            binding.progressBar.visibility= View.GONE
        }
    }

}
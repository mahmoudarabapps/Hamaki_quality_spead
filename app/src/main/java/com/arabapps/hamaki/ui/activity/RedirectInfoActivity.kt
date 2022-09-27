package com.arabapps.hamaki.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.arabapps.hamaki.databinding.ActivityRedirectInfoBinding

class RedirectInfoActivity : AppCompatActivity() {
    lateinit var binding: ActivityRedirectInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRedirectInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textTitle.text = HtmlCompat.fromHtml(
            "<p style=\"text-align: center;\">&nbsp;</p>\n" +
                    "<p style=\"text-align: center;\">&nbsp;</p>\n" +
                    "<p style=\"text-align: center;\">&nbsp;</p>\n" +
                    "<p><strong>برجاء اطفاء الراوتر لمدة دقيقتين وتشغيله مره اخري وذلك لاعادة تشغيل البرنامج بكفاءة</strong></p>\n" +
                    "<p style=\"text-align: center;\">&nbsp;</p>",
            HtmlCompat.FROM_HTML_MODE_COMPACT
        )
        binding.finish.setOnClickListener { finish() }
    }
}
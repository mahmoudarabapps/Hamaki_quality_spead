package com.arabapps.hamaki.ui.fragment.instructions

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.arabapps.hamaki.databinding.InstructionsFragmentBinding
import com.arabapps.hamaki.helper.HtmlHelper

class InstructionsFragment : Fragment() {


    private lateinit var viewModel: InstructionsViewModel
    private lateinit var binding: InstructionsFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = InstructionsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(InstructionsViewModel::class.java)


        binding.textTitle.text = HtmlHelper.getString(
            "<h2 style=\"text-align: center;\"><strong>تعليمات هامه</strong></h2>\n" +
                    "<p style=\"text-align: center;\"><strong>عزيزي الطالب برجاء قراءة التعليمات الاتية بعناية شديدة ما قبل الاتشراك :&nbsp;</strong></p>\n" +
                    "<p style=\"text-align: center;\"><strong>تطبيق حماقي اون لاين خالي تمام من اي عيوب فنية بعد خضوع لفحص وتجارب عديدة وبالتالي فأن مشاكل&nbsp; تشغيل الفيديوهات أو عدم تشغليها من الاساس هي مشاكل أما متعلقة بموبايلك أو بسرعة الأنترنت التي تستخدمها .</strong></p>\n" +
                    "<p style=\"text-align: center;\"><strong>تحذيرات هامة&nbsp;</strong></p>\n" +
                    "<p style=\"text-align: center;\"><strong>الاشتراك الخاص بالطالب علي التطبيق لن يعمل الا علي هاتف واحد</strong></p>\n" +
                    "<p style=\"text-align: center;\"><strong>أثناء تشغيل الفيديوهات يجب وضع سماعة وغلق البلوتوث تمام قبل تشغيل الفيديوهات</strong></p>\n" +
                    "<p style=\"text-align: center;\">&nbsp;</p>\n" +
                    "<p style=\"text-align: center;\"><strong>تطبيق حماقي اون لاين يتمني لكم دوام التوفيق والنجاح</strong></p>\n" +
                    "<p style=\"text-align: center;\">&nbsp;</p>"
        )
        binding.finish.setOnClickListener { activity?.onBackPressed() }
        binding.link.setOnClickListener {

            val intent = Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse("https://arab-apps.com/"))
            try {
                context?.startActivity(intent)
            } catch (ex: Exception) {
            }

        }


    }


}
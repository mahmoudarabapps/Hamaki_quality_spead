package com.arabapps.hamaki.ui.fragment.account

import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.arabapps.hamaki.R
import com.arabapps.hamaki.databinding.AccountFragmentBinding
import com.arabapps.hamaki.ui.activity.login.LoginActivity
import com.bumptech.glide.Glide
import com.sasco.user.helper.DialogHelper
import com.sasco.user.helper.ImagePickerHelper
import com.sasco.user.helper.SharedHelper
import okhttp3.RequestBody

class AccountFragment : Fragment() {


    private lateinit var viewModel: AccountViewModel
    private lateinit var binding: AccountFragmentBinding
    private var image: RequestBody? = null
    private lateinit var loading: Dialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AccountFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loading = DialogHelper.getLoading(binding.root.context)

        binding.apply {

            edtName.setText(SharedHelper.getString(view.context, SharedHelper.NAME))
            edtPhone.setText(SharedHelper.getString(view.context, SharedHelper.PHONE))
            edtClass.setText(SharedHelper.getString(view.context, SharedHelper.GROUP))
            Glide.with(circleImage).load(
                "" +
                        SharedHelper.getString(
                            view.context, SharedHelper.IMAGE
                        )
            ).placeholder(R.drawable.default_user).into(circleImage)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(AccountViewModel::class.java)
        binding.apply {
            pickImage.setOnClickListener { ImagePickerHelper.pickImage(this@AccountFragment) }
        }
        context?.let { viewModel.profile(it) }
        if (view == null)
            return
        viewModel.loginLiveData.observe(viewLifecycleOwner, Observer {
            if (view == null)
                return@Observer

            if (it == null && context?.let { it1 ->
                    SharedHelper.getString(it1, SharedHelper.TOKEN)
                        .isNullOrEmpty()
                }!!
            ) {
                val intent = Intent(context, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                activity?.finish()
            } else if (it != null) {
                binding.apply {
                    edtName.setText(it.fullName)
                    edtPhone.setText(it.email)
                    edtClass.setText(it.group?.name)
                    Glide.with(circleImage).load("" + it.imagePath).into(circleImage)
                }
            }
        })
        binding.buttonUpdate.setOnClickListener {


            context?.let { it1 ->
                loading.show()
                viewModel.updateProfile(
                    it1,
                    binding.edtPassword.text.toString(),
                    image
                )
            }
        }
        viewModel.updateLiveData.observe(viewLifecycleOwner, Observer {
            if (view == null)
                return@Observer
            loading.dismiss()
            if (it == null && context?.let { it1 ->
                    SharedHelper.getString(it1, SharedHelper.TOKEN)
                        .isNullOrEmpty()
                }!!
            ) {
                val intent = Intent(context, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                activity?.finish()
            } else if (it != null) {
                binding.apply {
                    context?.let { it1 ->
                        SharedHelper.saveString(
                            it1,
                            SharedHelper.IMAGE,
                            it.imagePath.toString()
                        )
                    }
                    Glide.with(circleImage).load(it.imagePath).into(circleImage)
                }
            }
        })

    }

   

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val bitmap: Bitmap? =
            context?.let { ImagePickerHelper.pickedImage(it, requestCode, resultCode, data) }
        if (bitmap != null) {
            binding.circleImage.setImageBitmap(bitmap)
            image = ImagePickerHelper.toRequestbody(bitmap)
        }
    }

}
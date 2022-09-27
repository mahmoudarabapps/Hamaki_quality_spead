package com.arabapps.hamaki.ui.fragment.notifications

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arabapps.hamaki.BuildConfig
import com.arabapps.hamaki.adapter.NotificationsAdapter
import com.arabapps.hamaki.databinding.NotificationsFragmentBinding
import com.arabapps.hamaki.ui.activity.login.LoginActivity
import com.sasco.user.helper.SharedHelper

private const val TAG = "NotificationsFragment"
class NotificationsFragment : Fragment() {


    private lateinit var viewModel: NotificationsViewModel
    private lateinit var binding: NotificationsFragmentBinding
    var page = 1
    private var loadmore: Boolean = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = NotificationsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = activity?.let { ViewModelProvider(it).get(NotificationsViewModel::class.java) }!!
        val adapter = NotificationsAdapter()
        binding.apply {
            recyclerNotifications.adapter = adapter

                viewModel.notifications(binding.root.context,page)
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
                    if (it.currentPage != null)
                        page = it.currentPage
                    if (it.currentPage == 1)
                        adapter.setAll(it.notificationData)
                    else
                        adapter.addAll(it.notificationData)
                }
                loadmore = true
            })

            val layoutManager =
                binding.recyclerNotifications.getLayoutManager() as LinearLayoutManager
            binding.recyclerNotifications.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    // super.onScrolled(recyclerView, dx, dy);
                    val lastVisiblePosition = layoutManager.findLastVisibleItemPosition()+1
                    if (BuildConfig.DEBUG)  Log.d(TAG, "onScrolled: "+lastVisiblePosition+ "  vs "+adapter.itemCount)
                    if (lastVisiblePosition >= adapter.itemCount) {
                        if (loadmore) {
                            loadmore = false
                            page++
                            viewModel.notifications(binding.root.context, page)
                        }
                    }
                }
            })


            imageBack.setOnClickListener { activity?.onBackPressed() }
        }


    }

}
package com.arabapps.hamaki.ui.fragment.search

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.arabapps.hamaki.BuildConfig
import com.arabapps.hamaki.R
import com.arabapps.hamaki.adapter.FavAdapter
import com.arabapps.hamaki.data.LecturesItem
import com.arabapps.hamaki.databinding.FragmentFavBinding
import com.arabapps.hamaki.ui.activity.login.LoginActivity
import com.sasco.user.helper.SharedHelper


private const val TAG = "LectureFragment"

class SearchFragment : Fragment(), FavAdapter.ItemClicked {


    private lateinit var viewModel: LectureViewModel

    lateinit var binding: FragmentFavBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavBinding.inflate(inflater, container, false)

        return binding.root
    }


    lateinit var adapter: FavAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (BuildConfig.DEBUG) Log.d(TAG, "onViewCreated: ")
        adapter = FavAdapter(this)
        binding.recyclerView.adapter = adapter
        viewModel = ViewModelProvider(this).get(LectureViewModel::class.java)

        binding.apply {
            textTitle.text = "Search"
            searchView.setOnQueryTextListener(object :
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let { viewModel.search(binding.root.context, it) }
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
        }

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
                    adapter.setAll(it.data as ArrayList<LecturesItem?>?)
                }
            }
        })
        arguments?.getString("text")?.let { it1 -> viewModel.search(binding.root.context, it1) }


    }


    override fun addToFav(id: Int, position: Int) {
        viewModel.addBookmark(binding.root.context, id).observe(viewLifecycleOwner, Observer {
            if (it != null)
                adapter.itemAdded(position)
        })
    }

    override fun removeFav(id: Int, position: Int) {
        viewModel.deleteBookmarks(binding.root.context, id).observe(viewLifecycleOwner, Observer {
            if (it != null)
                adapter.deleteItem(position)
        })
    }

    override fun favItemClicked(dataa: LecturesItem?) {
        val bundle = Bundle()
        dataa?.id?.let { it1 -> bundle.putInt("id", it1) }
        findNavController().navigate(  R.id.lectureFragment,bundle)

    }

}
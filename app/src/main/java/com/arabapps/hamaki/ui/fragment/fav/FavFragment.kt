package com.arabapps.hamaki.ui.fragment.fav

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arabapps.hamaki.BuildConfig
import com.arabapps.hamaki.adapter.FavAdapter
import com.arabapps.hamaki.R
import com.arabapps.hamaki.data.LecturesItem
import com.arabapps.hamaki.databinding.FragmentFavBinding
import com.arabapps.hamaki.ui.activity.login.LoginActivity
import com.sasco.user.helper.SharedHelper


private const val TAG = "FavFragment"

class FavFragment : Fragment(), FavAdapter.ItemClicked {

    private lateinit var viewmodel: favViewModel
    private lateinit var binding: FragmentFavBinding
    private var loadmore: Boolean = false
    var page = 1
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentFavBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Navigation.findNavController(view)
        binding.textTitle.text = view.context.resources.getString(R.string.fav)

    }

    lateinit var adapter: FavAdapter
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewmodel =
            ViewModelProvider(this).get(favViewModel::class.java)
        adapter = FavAdapter(this@FavFragment)
        binding.recyclerView.adapter = adapter
        viewmodel.allBookmarks(binding.root.context, page)
        viewmodel.loginLiveData.observe(viewLifecycleOwner, Observer {
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
                    adapter.setAll(it.data as ArrayList<LecturesItem?>?)
                else
                    adapter.addAll(it.data as ArrayList<LecturesItem?>?)
            }
            loadmore = true
        })
        val layoutManager =
            binding.recyclerView.getLayoutManager() as LinearLayoutManager
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                // super.onScrolled(recyclerView, dx, dy);
                val lastVisiblePosition = layoutManager.findLastVisibleItemPosition() + 1
                if (BuildConfig.DEBUG)  Log.d(TAG, "onScrolled: " + lastVisiblePosition + "  vs " + adapter.itemCount)
                if (lastVisiblePosition >= adapter.itemCount) {
                    if (loadmore) {
                        loadmore = false
                        page++
                        viewmodel.allBookmarks(binding.root.context, page)
                    }
                }
            }
        })

    }

    override fun addToFav(id: Int, position: Int) {

        viewmodel.addBookmark(binding.root.context, id).observe(viewLifecycleOwner, Observer {
            if (it != null)
                adapter.itemAdded(position)
        })
    }

    override fun removeFav(id: Int, position: Int) {
        viewmodel.deleteBookmarks(binding.root.context, id).observe(viewLifecycleOwner, Observer {
            if (it != null)
                adapter.deleteItem(position)
        })
    }


    override fun onResume() {
        super.onResume()
        binding.apply {

            searchView.setOnCloseListener(object : SearchView.OnCloseListener {
                override fun onClose(): Boolean {
                    loadmore = true
                    return false
                }
            })
            searchView.setOnQueryTextListener(object :
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {

                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    loadmore = false
                    adapter.filter.filter(searchView.query.toString())
                    return false
                }

            })
        }
    }
    override fun favItemClicked(dataa: LecturesItem?) {
        val bundle = Bundle()
        dataa?.id?.let { it1 -> bundle.putInt("id", it1) }
        findNavController().navigate(  R.id.lectureFragment,bundle)

    }
}
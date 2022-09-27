package com.arabapps.hamaki.ui.fragment.subject_content

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.arabapps.hamaki.adapter.FavAdapter
import com.arabapps.hamaki.R
import com.arabapps.hamaki.data.LecturesItem
import com.arabapps.hamaki.databinding.FragmentFavBinding
import com.arabapps.hamaki.ui.activity.login.LoginActivity
import com.sasco.user.helper.SharedHelper


class SubjectContentFragment : Fragment(), FavAdapter.ItemClicked {

    private lateinit var subjectViewmodel: SubjectContentViewModel
    private lateinit var binding: FragmentFavBinding
    lateinit var adapter: FavAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        subjectViewmodel =
            ViewModelProvider(this).get(SubjectContentViewModel::class.java)
        binding = FragmentFavBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        Navigation.findNavController(binding.root)
        binding.textTitle.text = binding.root.context.resources.getString(R.string.title_subjects)
        val adapter = FavAdapter(this@SubjectContentFragment)
        binding.recyclerView.adapter = adapter
        binding.searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(binding.searchView.query)
                return false
            }
        })
        subjectViewmodel.loginLiveData.observe(viewLifecycleOwner, Observer {
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

                    adapter.setAll(it.lectures as ArrayList<LecturesItem?>?)
                }
            }
        })
        context?.let {
            arguments?.getInt("id")?.let { it1 -> subjectViewmodel.subjectByID(it, it1) }
        }
    }

    override fun addToFav(id: Int, positon: Int) {

        arguments?.getInt("id")?.let {
            subjectViewmodel.addBookmark(binding.root.context, id, it)
                .observe(viewLifecycleOwner, Observer {
                    if (it != null) {
                        adapter.itemAdded(positon)
                    }
                })
        }
    }

    override fun removeFav(id: Int, positon: Int) {
        arguments?.getInt("id")?.let {
            subjectViewmodel.deleteBookmarks(
                binding.root.context, id,
                it
            ).observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    adapter.itemRemove(positon)
                }
            })
        }
    }

}
package com.arabapps.hamaki.ui.fragment.subjects

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.arabapps.hamaki.adapter.AllSubjectsAdapter
import com.arabapps.hamaki.R
import com.arabapps.hamaki.databinding.FragmentFavBinding
import com.arabapps.hamaki.ui.activity.login.LoginActivity
import com.sasco.user.helper.SharedHelper


class SubjectsFragment : Fragment() {

    private lateinit var subjectViewmodel: SubjectsViewModel
    private lateinit var binding: FragmentFavBinding
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
        binding.textTitle.text = view.context.resources.getString(R.string.title_subjects)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        subjectViewmodel =
            ViewModelProvider(requireActivity()).get(SubjectsViewModel::class.java)
        val adapter = AllSubjectsAdapter()
        binding.recyclerView.adapter = adapter
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(binding.searchView.query.toString())
                return true

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
                    adapter.setAll(it.subjects)
                }
            }
        })
        context?.let { subjectViewmodel.materials(it) }
    }
}
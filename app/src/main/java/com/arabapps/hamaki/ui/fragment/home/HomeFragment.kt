package com.arabapps.hamaki.ui.fragment.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.arabapps.hamaki.BuildConfig
import com.arabapps.hamaki.adapters.AllSubjectsHomeAdapter
import com.arabapps.hamaki.adapters.ArticlesAdapter
import com.arabapps.hamaki.adapter.LastAddedLecturesAdapter
import com.arabapps.hamaki.R
import com.arabapps.hamaki.data.SubjectsItem
import com.arabapps.hamaki.databinding.FragmentHomeBinding
import com.arabapps.hamaki.ui.activity.login.LoginActivity
import com.arabapps.hamaki.ui.fragment.bottom_sheet_react.BottomSheetFragment
import com.sasco.user.helper.SharedHelper


private const val TAG = "HomeFragment"

class HomeFragment : Fragment(), ArticlesAdapter.PostReaction {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding
    var loading = true
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Navigation.findNavController(view)

    }

    lateinit var allsubjectAdapter: AllSubjectsHomeAdapter
    lateinit var articlesAdapter: ArticlesAdapter
    lateinit var lastaddhomeAdapter: LastAddedLecturesAdapter
    var page = 1
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        homeViewModel =
            ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)
        binding.apply {
            allsubjectAdapter = AllSubjectsHomeAdapter()
            recyclerAllSubjects.adapter = allsubjectAdapter
            articlesAdapter = ArticlesAdapter(this@HomeFragment)
            recyclerLastArticles.adapter = articlesAdapter
            lastaddhomeAdapter = LastAddedLecturesAdapter()
            recyclerLastAdded.adapter = lastaddhomeAdapter

            textUsername.text =
                SharedHelper.getString(binding.root.context, SharedHelper.NAME) + "\n" +
                        SharedHelper.getString(binding.root.context, SharedHelper.PHONE)

            textClassName.text = SharedHelper.getString(binding.root.context, SharedHelper.GROUP)

            textAllSubjects.setOnClickListener {
                Navigation.createNavigateOnClickListener(R.id.action_navigation_home_to_navigation_subjects)
                    .onClick(it)
            }
            notifications.setOnClickListener {
                Navigation.createNavigateOnClickListener(R.id.action_navigation_home_to_notificationsFragment)
                    .onClick(it)
            }
            page = 1
            loading = true
            homeViewModel.post(binding.root.context)
            homeViewModel.materials(binding.root.context)
            homeViewModel.lectureLatest(binding.root.context)

            homeViewModel.loginLiveData.observe(viewLifecycleOwner, Observer {

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
                    page = it.current_page
                    if (it.current_page == 1)
                        articlesAdapter.setAll(it.data)
                    else if (it.current_page > 1)
                        articlesAdapter.addAll(it.data)

                }
            })

            homeViewModel.lastLecturesLivedata.observe(viewLifecycleOwner, Observer {
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
                    lastaddhomeAdapter.setAll(it)
                }
                loading = false
            })
            homeViewModel.materialsViemodel.observe(viewLifecycleOwner, Observer {
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
                    allsubjectAdapter.setAll(it.subjects)

                }
            })
            /*   nestedScroll.viewTreeObserver
                   .addOnScrollChangedListener {
                       val view =
                           nestedScroll.getChildAt(nestedScroll.getChildCount() - 1) as View
                       val diff: Int = view.bottom - (nestedScroll.getHeight() + nestedScroll
                           .getScrollY())
                       if (diff == 0) {
                           if (!loading) {
                               loading = true
                               page++
                               homeViewModel.post(binding.root.context, page)
                           }
                       }
                   }*/


            searchView.setOnQueryTextListener(object :
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    val bundle = Bundle()
                    bundle.putString("text", query)
                    Navigation.createNavigateOnClickListener(
                        R.id.action_navigation_home_to_searchFragment,
                        bundle
                    ).onClick(searchView)
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    /*  allsubjectAdapter.filter.filter(searchView.query.toString())
  //                    articlesAdapter.filter.filter(searchView.query.toString())
                      lastaddhomeAdapter.filter.filter(searchView.query.toString())*/


                    return false
                }

            })

        }
    }

    override fun react(id: Int, adapterPosition: Int, reaction: Int, isReacted: Int) {

        homeViewModel.addReaction(binding.root.context, id, reaction)
            .observe(viewLifecycleOwner, Observer {
                /*if (it != null) {
                    if (it.id > 0) {
                        articlesAdapter.notifyItemChange(adapterPosition)
                    }
                }*/
            })
    }

    override fun delete(id: Int, position: Int) {


        homeViewModel.deleteReaction(binding.root.context, id)
            .observe(viewLifecycleOwner, Observer {
                /* if (it != null) {
                     if (it.id > 0) {
                         articlesAdapter.deleteReaction(position)
                     }
                 }*/
            })
    }


    override fun openReacts(id: Int) {
        val bottomSheetFragment = BottomSheetFragment(id)
        bottomSheetFragment.show(childFragmentManager, "bottomSheetFragment.tag")


    }

}
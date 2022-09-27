package com.arabapps.hamaki.ui.fragment.bottom_sheet_react

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.arabapps.hamaki.R
import com.arabapps.hamaki.adapters.ReactsStudentsAdapter
import com.arabapps.hamaki.databinding.DialogReactsBinding
import com.arabapps.hamaki.helper.PaginationScrollListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BottomSheetFragment(val postID: Int) : BottomSheetDialogFragment() {
    private var page: Int = 1
    private var lastPage: Int = 1
    lateinit var binding: DialogReactsBinding
    lateinit var viewModel: ReactsViewModel
    private var isLoading = true
    private var position = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogReactsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(ReactsViewModel::class.java)
        val adapter = ReactsStudentsAdapter()
        binding.imageLike.setOnClickListener {
            adapter.removeAll()
            page = 1
            binding.imageLike.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_like_green,
                0,
                0,
                0
            )
            binding.imageLove.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_love, 0, 0, 0);
            binding.imageSad.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_sad, 0, 0, 0);


            isLoading = true
            position=0
            viewModel.likesStudent(
                binding.root.context, postID, page,
                position
            )
        }
        binding.imageLove.setOnClickListener {
            page = 1
            adapter.removeAll()


            binding.imageLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like, 0, 0, 0);
            binding.imageLove.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_love_green,
                0,
                0,
                0
            );
            binding.imageSad.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_sad, 0, 0, 0);


            isLoading = true
            position=1
            viewModel.likesStudent(
                binding.root.context, postID, page,
                position
            )
        }
        binding.imageSad.setOnClickListener {
            page = 1
            adapter.removeAll()

            binding.imageLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like, 0, 0, 0);
            binding.imageLove.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_love, 0, 0, 0);
            binding.imageSad.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_sad_green,
                0,
                0,
                0
            );


            isLoading = true


            position=2
            viewModel.likesStudent(
                binding.root.context, postID, page,
                position
            )
        }

        binding.imageLike.performClick()
        binding.recycler.layoutManager?.let {
            binding.recycler.addOnScrollListener(object : PaginationScrollListener(it) {
                override fun isLastPage(): Boolean {
                    return lastPage == page
                }

                override fun isLoading(): Boolean {
                    return isLoading
                }

                override fun loadMoreItems() {
                    page++
                    isLoading = true
                    viewModel.likesStudent(
                        binding.root.context, postID, page,
                        position
                    )
                }

            })
        }

        binding.recycler.adapter = adapter
        viewModel.loginLiveData.observe(viewLifecycleOwner, Observer {
            if (getView() == null)
                return@Observer
            if (it != null) {
                page = it.currentPage ?: 1
                lastPage = it.lastPage ?: 1
                isLoading = false
                if (it.currentPage == 1)
                    adapter.set(it.data)
                else
                    adapter.add(it.data)

            }
        })
    }
}
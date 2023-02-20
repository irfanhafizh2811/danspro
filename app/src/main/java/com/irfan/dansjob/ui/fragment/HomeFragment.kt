package com.irfan.dansjob.ui.fragment

import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.irfan.dansjob.R
import com.irfan.dansjob.adapter.JobAdapter
import com.irfan.dansjob.databinding.FragmentHomeBinding
import com.irfan.dansjob.extension.view.gone
import com.irfan.dansjob.extension.view.hideKeyboard
import com.irfan.dansjob.extension.view.visible
import com.irfan.dansjob.network.ApiResponse
import com.irfan.dansjob.vm.JobViewModel
import io.reactivex.disposables.CompositeDisposable
import org.koin.android.ext.android.inject

class HomeFragment : Fragment() {

    private val viewModel by inject<JobViewModel>()

    private var _binding: FragmentHomeBinding? = null

    private var myCompositeDisposable: CompositeDisposable? = null

    private var adapterBrand: JobAdapter? = null

    private var isLoading = true
    private var page = 0
    private var totalPage = 0

    private var description: String = ""
    private var location: String = ""
    private var fulltime = false

    private var isShowFilter = false

    private val waitingTimeSearch: Long = 500
    private var cdtSaveSearch: CountDownTimer? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.clFilter.gone()

        view()

        return root
    }

    private fun view() {

        binding.srPosition.setOnRefreshListener {
            binding.rlPageLoadingScreen.gone()

            myCompositeDisposable?.clear()
            adapterBrand?.clear()
            fulltime = false
            location = ""
            binding.etFilterSearch.text.clear()
            binding.etFilterSearch.clearFocus()
            binding.scFilterFulltime.isChecked = false
            binding.etFilterLocation.text.clear()
            binding.etFilterLocation.clearFocus()

            onLoadData()

            binding.root.hideKeyboard()

            binding.srPosition.isRefreshing = false
        }

        binding.etFilterSearch.doOnTextChanged { text, _, _, _ ->
            if (text?.length!! > 1) {
                description
            }
        }

        binding.etFilterSearch.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(data: Editable) {

            }

            override fun beforeTextChanged(
                data: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                data: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                if (data.isNotEmpty()) {
                    description = data.toString()

                    if (cdtSaveSearch != null) {
                        cdtSaveSearch?.cancel()
                    }

                    cdtSaveSearch = object : CountDownTimer(waitingTimeSearch, 500) {
                        override fun onTick(millisUntilFinished: Long) {
                            Log.d(
                                tag,
                                "CDT Search seconds remaining: " + millisUntilFinished / 1000
                            )
                        }

                        override fun onFinish() {
                            onLoadData()
                        }
                    }

                    (cdtSaveSearch as CountDownTimer).start()
                } else {
                    description = ""
                    cdtSaveSearch?.cancel()
                    onLoadData()
                }
            }
        })

        binding.nsvLayoutPositionList.setOnScrollChangeListener(
            NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, oldScrollY ->

                if (v.getChildAt(v.childCount - 1) != null) {
                    if (scrollY >= v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight && scrollY > oldScrollY) {
                        val thisLayoutManager =
                            binding.rvPositionData.layoutManager as LinearLayoutManager
                        val countItem = thisLayoutManager.itemCount

                        val lastVisiblePosition =
                            thisLayoutManager.findLastCompletelyVisibleItemPosition()
                        val isLastPosition = countItem.minus(1) == lastVisiblePosition

                        if (!isLoading && isLastPosition && page < totalPage) {
                            showLoading(true)
                            page = page.plus(1)
                            doLoadData()
                        }
                    }
                }
            })

        binding.ivShowFilter.setOnClickListener {
            if (isShowFilter) {
                binding.ivShowFilter.setImageResource(R.drawable.ic_caret_down_24dp)
                isShowFilter = false
                binding.clFilter.gone()
            } else {
                binding.ivShowFilter.setImageResource(R.drawable.ic_caret_up_24dp)
                isShowFilter = true
                binding.clFilter.visible()
            }
            binding.root.hideKeyboard()
        }

        Log.d("DataHomeFragment", "Fulltime: $fulltime")
        binding.scFilterFulltime.apply {
            isChecked = fulltime
            setOnCheckedChangeListener { _, checked ->
                fulltime = checked
                Log.d("DataHomeFragment", "Fulltime: $fulltime")
            }
        }

        binding.btnApplyFilter.setOnClickListener {
            location =
                if (!binding.etFilterLocation.text.isNullOrEmpty()) binding.etFilterLocation.text.toString()
                else ""

            onLoadData()

            binding.etFilterSearch.clearFocus()
            binding.etFilterLocation.clearFocus()
            binding.root.hideKeyboard()
        }

        onLoadData()
    }

    private fun onLoadData() {
        binding.clLayoutPositionList.gone()
        myCompositeDisposable = CompositeDisposable()
        page = 1
        totalPage = 0

        doLoadData()
    }

    private fun doLoadData() {
        Log.d(tag, "page: $page")

        if (page == 1) {
            showLoading(false)
        } else {
            showLoading(true)
        }

        viewModel.getJobs(
            page = page,
            description = description,
            location = location,
            fulltime = fulltime,
        ).observe(viewLifecycleOwner) {
            when (it) {
                is ApiResponse.Success -> {
                    Log.d(tag, "onSuccess")
                    if (page == 1) {
                        binding.clLayoutPositionList.visible()
                        adapterBrand = JobAdapter(it.data)
                        binding.rvPositionData.apply {
                            layoutManager = LinearLayoutManager(
                                context,
                                LinearLayoutManager.VERTICAL,
                                false
                            )
                            adapter = adapterBrand
                        }
                        binding.rlPageLoadingScreen.gone()
                    } else {
                        adapterBrand?.refreshAdapter(it.data)
                    }

                    // Static from max page on API, because there is response for pagination max limit, page, etc.
                    totalPage = 2

                    hideLoading()
                    Log.d(tag, "onComplete")
                }
                else -> {}
            }
        }
    }

    private fun showLoading(isRefresh: Boolean) {
        if (isRefresh) {
            binding.pbOnLoading.visible()
            binding.rlPageLoadingScreen.gone()
        } else {
            binding.pbOnLoading.gone()
            binding.rlPageLoadingScreen.visible()
        }

        binding.rvPositionData.visibility.let {
            if (isRefresh) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }

    private fun hideLoading() {
        isLoading = false
        binding.pbOnLoading.gone()
        binding.rvPositionData.visible()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
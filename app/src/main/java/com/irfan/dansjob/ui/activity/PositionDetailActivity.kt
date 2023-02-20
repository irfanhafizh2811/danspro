package com.irfan.dansjob.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.irfan.dansjob.R
import com.irfan.dansjob.databinding.ActivityHomePositionDetailBinding
import com.irfan.dansjob.extension.view.gone
import com.irfan.dansjob.extension.view.visible
import com.irfan.dansjob.network.ApiResponse
import com.irfan.dansjob.vm.JobDetailViewModel
import org.koin.android.ext.android.inject

class PositionDetailActivity : AppCompatActivity() {

    private val viewModel by inject<JobDetailViewModel>()
    private lateinit var binding: ActivityHomePositionDetailBinding

    private var id: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomePositionDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        id = intent?.getStringExtra("position_id") ?: ""
        Log.d("DataHomeFragment", "ID $id")

        binding.ivPositionDetail.setOnClickListener {
            onBackPressed()
        }

        binding.srPositionDetail.setOnRefreshListener {
            loadDataPositionDetail()

            binding.srPositionDetail.isRefreshing = false
        }

        loadDataPositionDetail()
    }

    private fun loadDataPositionDetail() {
        viewModel.getJobDetail(id = id).observe(this) {
            when (it) {
                is ApiResponse.Success -> {
                    val data = it.data
                    Glide.with(binding.root.context).load(data.companyLogo)
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.ic_launcher)
                        .into(binding.ivPositionCompanyLogo)

                    binding.tvPositionDetailCompanyTitle.text = data.company
                    binding.tvPositionDetailLocation.text = data.location

                    val website = data.companyUrl
                    if (website.isNullOrEmpty()) {
                        binding.tvPositionCompanyWebsite.gone()
                    } else {
                        binding.tvPositionCompanyWebsite.apply {
                            visible()
                            setOnClickListener {
                                val i = Intent(Intent.ACTION_VIEW)
                                i.data = Uri.parse(website)
                                startActivity(i)
                            }
                        }
                    }

                    // Job Specification Section
                    binding.tvPositionDetailJobSpecificationTitle.text = data.title

                    if (data.type == "Full Time") {
                        binding.tvPositionDetailJobSpecificationFulltime.text = "Yes"
                    } else {
                        binding.tvPositionDetailJobSpecificationFulltime.text = "No"
                    }

                    binding.wvPositionDetailJobSpecificationDescription.loadDataWithBaseURL(
                        null,
                        data.description.toString(),
                        "text/html",
                        "utf-8",
                        null
                    )
                }
                is ApiResponse.Error -> {
                }
            }
        }
    }
}
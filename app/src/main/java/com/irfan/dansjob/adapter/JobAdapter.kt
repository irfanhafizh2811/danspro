package com.irfan.dansjob.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.irfan.dansjob.ui.activity.PositionDetailActivity
import com.irfan.dansjob.R
import com.irfan.dansjob.databinding.FragmentHomePositionItemBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.irfan.dansjob.extension.common.clazz
import com.irfan.dansjob.extension.view.visible
import com.irfan.dansjob.data.Job

class JobAdapter(private val data: ArrayList<Job>) :
    RecyclerView.Adapter<JobAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = FragmentHomePositionItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return Holder(binding)
    }

    fun refreshAdapter(data: ArrayList<Job>) {
        data.let {
            this.data.addAll(it)
        }

        notifyDataSetChanged()
    }

    fun clear() {
        this.data.clear()
        notifyItemRangeRemoved(0, data.size ?: 0)
    }

    override fun getItemCount(): Int = data.size ?: 0

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(data[position])
    }

    inner class Holder(private val binding: FragmentHomePositionItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Job?) {
            data?.let { job ->
                with(binding) {
                    binding.root.visible()

                    job.companyLogo?.let {
                        Glide.with(binding.root.context).load(it)
                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                            .placeholder(R.mipmap.ic_launcher)
                            .error(R.mipmap.ic_launcher)
                            .into(binding.ivPositionCompanyLogo)
                    }

                    binding.tvPositionTitle.text = job.title
                    binding.tvPositionCompany.text = job.company
                    binding.tvPositionLocation.text = job.location

                    binding.root.setOnClickListener {
                        val intent = Intent(binding.root.context, clazz<PositionDetailActivity>())
                        intent.putExtra("position_id", job.id)
                        binding.root.context.startActivity(intent)
                    }
                }
            }
        }
    }
}
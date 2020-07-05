package com.example.mytube.adapter

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.mytube.R
import com.example.mytube.getFormattedDate
import com.example.mytube.repository.Video
import kotlinx.android.synthetic.main.rv_video_row.view.*
import kotlin.collections.ArrayList


class RVAdapter(private var data: ArrayList<Video>, private val imageLoadedListener: ImageLoadingompletedListener):
    RecyclerView.Adapter<RVAdapter.ViewHolder>() {

    fun addMoreData(newData:ArrayList<Video>): Boolean {
        if(newData.count()<1) return false
        data.addAll(newData)
        notifyDataSetChanged()
        return true
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.rv_video_row,parent,false))
    }

    override fun getItemCount(): Int {
        return data.count()
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentVideo = data[position]
        holder.itemView.apply {
            Glide.with(this)
                .load(currentVideo.thumbnail_image)
                .listener(object :RequestListener<Drawable>{
                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        //if(position==data.count()-1) imageLoadedListener.onLoadComplete()
                        return false
                    }
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }
                })
                .placeholder(R.drawable.ic_ondemand_video_24dp)
                .into(rv_iv_post_img)
            rv_tv_title.text = currentVideo.event_name
            rv_tv_date.text = "Event Date: ${getFormattedDate(currentVideo.event_date)}"
            rv_tv_likes_count.text = "Likes: ${currentVideo.likes.toString()}"
            rv_tv_shares_count.text = "Shares: ${currentVideo.shares.toString()}"
            rv_tv_views_count.text = "Views: ${currentVideo.views.toString()}"
            vw_desc_section_container.visibility = if(currentVideo.isCollapsed) View.VISIBLE else View.GONE

            setOnClickListener {
                currentVideo.isCollapsed = !currentVideo.isCollapsed
                notifyItemChanged(position)
            }
            if(position==itemCount-1) imageLoadedListener.onLoadComplete()
        }
    }

    fun sortData(criteria: SpinnerItems) {
        when(criteria){
            SpinnerItems.NEW ->{data.sortWith(compareBy {it.event_date.toLong()}) }
            SpinnerItems.OLD ->{data.sortWith(compareBy {it.event_date.toLong()});data.reverse()}
            SpinnerItems.LIKE_ASC ->{data.sortWith(compareBy {it.likes})}
            SpinnerItems.LIKE_DESC ->{data.sortWith(compareBy {it.likes});data.reverse()}
            SpinnerItems.VIEWS_ASC ->{data.sortWith(compareBy {it.views})}
            SpinnerItems.VIEWS_DESC ->{data.sortWith(compareBy {it.views});data.reverse()}
            SpinnerItems.SHARED_ASC ->{data.sortWith(compareBy {it.shares})}
            SpinnerItems.SHARED_DESC->{data.sortWith(compareBy {it.shares});data.reverse()}
        }
        this.notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){}
}

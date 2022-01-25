package com.demo.damcogroup.damcocolorlovers.adapters

import ColorDataModel
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.demo.damcogroup.damcocolorlovers.R


class RecyclerViewAdapter(private val mContext: Context, private val mData: List<ColorDataModel>) :
    RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view: View
        val mInflater = LayoutInflater.from(mContext)
        view = mInflater.inflate(R.layout.colors_gridcard_layout, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.tvTitle.text = "Title: " + mData.get(position).title
        holder.tvHex.text = "Hex: " + mData.get(position).hex

        Glide.with(mContext).load(mData.get(position).imageUrl).into(holder.ivColorImage);

        holder.ivLikeDislike.setOnClickListener {

//            Persisting “liked” options so if a user clicks like on a specific colour, next time they load
//           the app the colour is still liked.

            var resLike: Drawable = mContext.resources.getDrawable(R.drawable.like);
            var resDisLike: Drawable = mContext.resources.getDrawable(R.drawable.dislike);
            if (holder.ivLikeDislike.drawable.constantState == resLike.constantState) {
                holder.ivLikeDislike.setImageDrawable(resDisLike)
            } else {
                holder.ivLikeDislike.setImageDrawable(resLike)
            }
        }

        holder.ivColorImage.setOnClickListener {
            //  open the url for the selected result in either a webview
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(mData.get(position).url))
            mContext.startActivity(browserIntent)
        }

    }

    override fun getItemCount(): Int {
        return mData.size
    }

    //
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal var ivColorImage: ImageView
        internal var ivLikeDislike: ImageView
        internal var tvTitle: TextView
        internal var tvHex: TextView

        init {
            ivColorImage = itemView.findViewById(R.id.iv_colorImage)
            ivLikeDislike = itemView.findViewById(R.id.likeDislikeImg)
            tvTitle = itemView.findViewById(R.id.tv_title)
            tvHex = itemView.findViewById(R.id.tv_hex)
        }
    }
}
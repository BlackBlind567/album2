package com.atomsgroup.album

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import com.bumptech.glide.Glide


class SliderAdapter(private val context: Context,
                    private val images: Array<String?>) : PagerAdapter() {
    private var layoutInflater: LayoutInflater? = null

    init {
        Log.d("response7", images.size.toString())
    }


    override fun getCount(): Int {
        return images.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = layoutInflater!!.inflate(R.layout.sliding_images, null)
        val imageView = view.findViewById<View>(R.id.image_sliding) as ImageView
        Log.d("response8", images[position])
        Glide.with(context).load(images[position]).into(imageView)


        val vp = container as ViewPager
        vp.addView(view, 0)
        return view

    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {

        val vp = container as ViewPager
        val view = `object` as View
        vp.removeView(view)

    }
}
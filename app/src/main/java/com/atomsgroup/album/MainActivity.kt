package com.atomsgroup.album

import android.os.Handler
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout

import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

import org.json.JSONException

import java.util.Arrays
import java.util.Timer
import java.util.TimerTask

class MainActivity : AppCompatActivity() {

    private lateinit var requestQueue: RequestQueue
    private lateinit var sliderDotspanel: LinearLayout
    var dotscount: Int = 0
    private lateinit var viewPager: ViewPager
    private  var dots: Array<ImageView?>? = null
    private lateinit var images: Array<String?>
    private var currentPage = 0
    private lateinit var timer: Timer
    private val DELAY_MS: Long = 5000//delay in milliseconds before task is to be executed
    private val PERIOD_MS: Long = 3000 // time in milliseconds between successive task executions.


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestQueue = Volley.newRequestQueue(this)
        getBannerUrls()

    }

    private fun getBannerUrls() {
        val bannerurl = Constants.Base_URL + "banner/"
        val jsonObjectRequest = JsonObjectRequest(bannerurl, null,
                { response ->
                    Log.d("response1", response.toString())
                    try {
                        val jsonArray = response.getJSONArray("response")
                        images = arrayOfNulls(jsonArray.length())
                        //                        Log.d("resTAG",  Arrays.toString(images));
                        for (i in 0 until jsonArray.length()) {
                            val pagerResponse = jsonArray.getJSONObject(i)

                            val imageObject = pagerResponse.getString("url")
                            //                                String titleObject = pagerResponse.getString("title");


                            Log.d("response2", imageObject)
                            //                                Log.d("response3", titleObject);

                            images[i] = pagerResponse.getString("url")
                            //                              .add(new Slider(imageObject,titleObject));
                            //                              String[] images = sliderArrayList;

                            val imageSize = images.size
                            Log.d("responseSize", images[i])

                        }

                        Log.d("response+4", Arrays.toString(images))
                        viewPager = findViewById(R.id.viewPager)
                        sliderDotspanel = findViewById(R.id.sliderDots)
                        val slideAdapter = SliderAdapter(this@MainActivity,
                                images)
                        viewPager.adapter = slideAdapter

                        dotscount = slideAdapter.count
                        dots = arrayOfNulls(dotscount)

                        for (i in 0 until dotscount) {
                            dots!![i] = ImageView(this@MainActivity)
                            dots!![i]?.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.nonactive_dots))
                            val params1 = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                            params1.setMargins(8, 0, 8, 0)
                            sliderDotspanel.addView(dots!![i], params1)
                        }

                        dots!![0]?.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.active_dot))
                        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

                            }

                            override fun onPageSelected(position: Int) {
                                for (i in 0 until dotscount) {
                                    dots!![i]?.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.nonactive_dots))
                                }
                                dots!![position]?.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.active_dot))
                            }

                            override fun onPageScrollStateChanged(state: Int) {}
                        })
                        //
                        //                            Timer timer = new Timer();
                        //                            timer.scheduleAtFixedRate(new MyTimerTask(), 6000, 4000);

                        /*After setting the adapter use the timer */
                        val handler = Handler()
                        val Update = Runnable {
                            if (currentPage == images.size + 1 - 1) {
                                currentPage = 0
                            }
                            viewPager.setCurrentItem(currentPage++, true)
                        }

                        timer = Timer() // This will create a new Thread
                        timer.schedule(object : TimerTask() { // task to be scheduled
                            override fun run() {
                                handler.post(Update)
                            }
                        }, DELAY_MS, PERIOD_MS)

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }, { error -> Log.d(TAG + "error2", error.message.toString()) })
        requestQueue.add(jsonObjectRequest)


    }

    companion object {

        private val TAG = "RespMain"
    }
}

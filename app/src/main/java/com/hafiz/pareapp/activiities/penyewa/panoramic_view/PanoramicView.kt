package com.hafiz.pareapp.activiities.penyewa.panoramic_view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_panoramic_view.*
import android.view.WindowManager
import com.google.vr.sdk.widgets.pano.VrPanoramaView
import android.view.View
import com.google.vr.sdk.widgets.pano.VrPanoramaEventListener

class PanoramicView : AppCompatActivity() {
    private var backgroundImageLoaderTask: ImageLoaderTask? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.hafiz.pareapp.R.layout.activity_panoramic_view)
        setFullscreen()
        loadPanoImage()
    }

    private fun setFullscreen(){
        val w = window // in Activity's onCreate() for instance
        w.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }


    @Synchronized
    private fun loadPanoImage() {
        loading.visibility = View.VISIBLE
        var task = backgroundImageLoaderTask
        if (task != null && !task.isCancelled()) {
            task.cancel(true)
        }
        val viewOptions = VrPanoramaView.Options()
        viewOptions.inputType = VrPanoramaView.Options.TYPE_MONO
        task = ImageLoaderTask(pano_view, viewOptions, getUriImage())
        task.execute(this.assets)
        backgroundImageLoaderTask = task
        loading.visibility = View.GONE
    }

    private fun getUriImage(): String {
        return "https://video.360cities.net/new-gamer-x/01744171_360_VR_VIDEO___PARACHUTE_JUMP_FROM_A_HEIGHT_OF_10000_METERS-1024x512.jpg"
    }

}

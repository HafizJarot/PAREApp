package com.hafiz.pareapp.activiities.penyewa.panoramic_view

import com.google.vr.sdk.widgets.pano.VrPanoramaView
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.content.res.AssetManager
import android.os.AsyncTask
import android.util.Log
import android.widget.Toast
import java.io.IOException
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.URL


class ImageLoaderTask(view: VrPanoramaView, private val viewOptions: VrPanoramaView.Options, private val assetName: String) : AsyncTask<AssetManager, Void, Bitmap>() {

    private val viewReference: WeakReference<VrPanoramaView> = WeakReference(view)

    override fun doInBackground(vararg params: AssetManager): Bitmap? {
        val assetManager = params[0]

        if (assetName == lastName && lastBitmap.get() != null) {
            return lastBitmap.get()
        }

        try {
            val url = URL(assetName)
            val connection = url.openConnection() as HttpURLConnection
            connection.setDoInput(true)
            connection.connect()
            val input = connection.getInputStream()
            val myBitmap = BitmapFactory.decodeStream(input)
            //return myBitmap;


            //Bitmap b = BitmapFactory.decodeStream(istr);

            lastBitmap = WeakReference(myBitmap)
            lastName = assetName
            return myBitmap
        } catch (e: IOException) {
            Log.e(TAG, "Could not decode default bitmap: $e")
            return null
        }

    }

    override fun onPostExecute(bitmap: Bitmap?) {
        val vw = viewReference.get()
        if (vw != null && bitmap != null) {
            if(checkIs360(bitmap)){
                vw.loadImageFromBitmap(bitmap, viewOptions)
            }else{
                Toast.makeText(vw.context, "Bukan gambar 360", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun checkIs360(bitmap: Bitmap): Boolean {
        val width  = bitmap.width
        val height = bitmap.height
        val ratio  = width/2
        return ratio >= height
    }

    companion object {
        private val TAG = "ImageLoaderTask"
        private var lastBitmap: WeakReference<Bitmap> = WeakReference<Bitmap>(null)
        private var lastName: String? = null
    }
}
package com.thesimplycoder.imagegallery.activity

import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.View
import com.thesimplycoder.imagegallery.R
import com.thesimplycoder.imagegallery.adapter.GalleryImageAdapter
import com.thesimplycoder.imagegallery.adapter.GalleryImageClickListener
import com.thesimplycoder.imagegallery.adapter.Image
import com.thesimplycoder.imagegallery.fragment.GalleryFullscreenFragment
import com.thesimplycoder.imagegallery.model.ImageGalleryUiModel
import com.thesimplycoder.imagegallery.service.MediaHelper
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : AppCompatActivity(), GalleryImageClickListener, View.OnClickListener {

    private val TAG = "Main Activity"
    // gallery column count
    private val SPAN_COUNT = 3

    private val imageList = ArrayList<Image>()
    lateinit var galleryAdapter: GalleryImageAdapter

    private var imageGalleryUiModelList:MutableMap<String, ArrayList<ImageGalleryUiModel>> = mutableMapOf()

    private var isMultiSelectable = false

    private var originalDistance = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnSelect.setOnClickListener(this)
        btnCamera.setOnClickListener(this)
        btnInfo.setOnClickListener(this)
        btnSave.setOnClickListener(this)
        btnShare.setOnClickListener(this)
        btnTrash.setOnClickListener(this)

        // init adapter
        galleryAdapter = GalleryImageAdapter(imageList)
        galleryAdapter.listener = this

        // init recyclerview
        recyclerView.layoutManager = GridLayoutManager(this, SPAN_COUNT)
        recyclerView.adapter = galleryAdapter

        imageGalleryUiModelList = MediaHelper.getImageGallery(this)
        if(imageGalleryUiModelList.isNotEmpty()){
            imageGalleryUiModelList.forEach{
                Log.i(TAG, it.key + ": " + it.value)
            }
        }else{
            Log.i(TAG, "imageGalleryUiModelList is empty")
        }

        // load images
        //loadImages()
        loadExtenalImages()
    }


    private fun loadExtenalImages(){
        if(imageList.size > 0) imageList.clear()

        var imageList:ArrayList<ImageGalleryUiModel> = imageGalleryUiModelList["CraigCam2"]!!
        imageList.forEach{
            this.imageList.add(Image(it.imageUri, it.imageUri.substring(it.imageUri.length-10, it.imageUri.length), false))
        }
        galleryAdapter.notifyDataSetChanged()
    }

    private fun loadImages() {

        imageList.add(Image("/storage/3561-6462/DCIM/Camera/20190308_001321.jpg", "Beach Houses", false))
        imageList.add(Image("https://i.ibb.co/gM5NNJX/butterfly.jpg", "Butterfly",false))
        imageList.add(Image("https://i.ibb.co/10fFGkZ/car-race.jpg", "Car Racing",false))
        imageList.add(Image("https://i.ibb.co/ygqHsHV/coffee-milk.jpg", "Coffee with Milk",false))
        imageList.add(Image("https://i.ibb.co/7XqwsLw/fox.jpg", "Fox",false))
        imageList.add(Image("https://i.ibb.co/L1m1NxP/girl.jpg", "Mountain Girl",false))
        imageList.add(Image("https://i.ibb.co/wc9rSgw/desserts.jpg", "Desserts Table",false))
        imageList.add(Image("https://i.ibb.co/wdrdpKC/kitten.jpg", "Kitten",false))
        imageList.add(Image("https://i.ibb.co/dBCHzXQ/paris.jpg", "Paris Eiffel",false))
        imageList.add(Image("https://i.ibb.co/JKB0KPk/pizza.jpg", "Pizza Time",false))
        imageList.add(Image("https://i.ibb.co/VYYPZGk/salmon.jpg", "Salmon ",false))
        imageList.add(Image("https://i.ibb.co/JvWpzYC/sunset.jpg", "Sunset in Beach",false))

        galleryAdapter.notifyDataSetChanged()
    }

    override fun onClick(position: Int) {
        // handle click of image

        if(isMultiSelectable){

        }else{
            val bundle = Bundle()
            bundle.putSerializable("images", imageList)
            bundle.putInt("position", position)
            Log.d(TAG, "image detail: ${imageList[position]}")

            val fragmentTransaction = supportFragmentManager.beginTransaction()
            val galleryFragment = GalleryFullscreenFragment()
            galleryFragment.setArguments(bundle)
            galleryFragment.show(fragmentTransaction, "gallery")
        }
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.btnSelect -> {
                if(isMultiSelectable) {
                    isMultiSelectable = false
                    btnSelect.setImageResource(R.drawable.ic_launcher_select)
                }
                else{
                    isMultiSelectable = true
                    btnSelect.setImageResource(R.drawable.ic_launcher_cancel)
                }

                recyclerView.layoutManager = GridLayoutManager(this, SPAN_COUNT)
                galleryAdapter = GalleryImageAdapter(imageList)
                galleryAdapter.listener = this
                recyclerView.adapter = galleryAdapter

            }
            R.id.btnCamera ->{

            }
            R.id.btnInfo -> {

            }
            R.id.btnSave -> {

            }
            R.id.btnShare -> {

            }
            R.id.btnTrash -> {
                val file = File(Environment.getExternalStorageDirectory().toString() + "/DCIM", "2020-04-09 14:20:04.jpeg")
                val deleted: Boolean = file.delete()
                Log.d(TAG, "File deleted: ${deleted}")
            }
        }
    }

}

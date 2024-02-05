package com.example.oblig1

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class GalleryAdapter(context: Context, photos: ArrayList<PhotoDescription>):
    ArrayAdapter<PhotoDescription>(context, R.layout.gallery_item, photos) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val gridView: View = convertView ?: inflater.inflate(R.layout.gallery_item, parent, false)

        val currentPhoto: PhotoDescription? = getItem(position)

        val imageView: ImageView = gridView.findViewById(R.id.photoImage)
        val descriptionView: TextView = gridView.findViewById(R.id.descriptionText)

        if(currentPhoto != null){
            descriptionView.text = currentPhoto.description

            if(currentPhoto.photo is Int)
                imageView.setImageResource(currentPhoto.photo)
            else if(currentPhoto.photo is Uri)
                imageView.setImageURI(currentPhoto.photo)

        }



        return gridView
    }

}
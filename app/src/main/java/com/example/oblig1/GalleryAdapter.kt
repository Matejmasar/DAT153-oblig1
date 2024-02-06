package com.example.oblig1

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

/**
 * This class is an adapter to populate gallery GridView of photos
 * it uses gallery_item.xml layout for each item in GridView, it consists of a photo and description
 */
class GalleryAdapter(context: Context, photos: ArrayList<PhotoDescription>):
    ArrayAdapter<PhotoDescription>(context, R.layout.gallery_item, photos) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // inflater to change views
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        // if convertView is not null (meaning it is recycled) it becomes the gridView, otherwise inflate new one
        val gridView: View = convertView ?: inflater.inflate(R.layout.gallery_item, parent, false)

        // current item in drawing
        val currentPhoto: PhotoDescription? = getItem(position)

        // get view for the image
        val imageView: ImageView = gridView.findViewById(R.id.photoImage)
        // get view for the description
        val descriptionView: TextView = gridView.findViewById(R.id.descriptionText)

        if(currentPhoto != null){
            descriptionView.text = currentPhoto.description

            // if Int => comes from R.drawable
            if(currentPhoto.photo is Int)
                imageView.setImageResource(currentPhoto.photo)
            // if Uri => comes from user's selection
            else if(currentPhoto.photo is Uri)
                imageView.setImageURI(currentPhoto.photo)

        }

        return gridView
    }

}
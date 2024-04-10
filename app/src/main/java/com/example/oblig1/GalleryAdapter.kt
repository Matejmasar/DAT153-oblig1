package com.example.oblig1

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

/**
 * Adapter for the RecyclerView in the GalleryActivity, compatible with Room
 * Takes function onDelete as a parameter, which is called when a photo is deleted
 * @param onDelete function to call when a photo is deleted
 */
class GalleryAdapter(private val onDelete: (PhotoDescription) -> Unit) :
    ListAdapter<PhotoDescription, GalleryAdapter.PhotoViewHolder>(PhotosComparator()) {

    /**
     * This method is called when the RecyclerView needs a new ViewHolder
     * @param parent ViewGroup that contains other views
     * @param viewType Int
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        return PhotoViewHolder.create(parent)
    }

    /**
     * This method is called when the RecyclerView needs to bind a ViewHolder to actual data
     * @param holder PhotoViewHolder ViewHolder to bind
     * @param position Int position of the ViewHolder
     */
    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)

        // set on click listener to show confirmation dialog before deleting photo
        holder.itemView.setOnClickListener {
            showConfirmationDialog(holder.itemView.context) { onDelete(current) }
        }
    }

    /**
     * Show a confirmation dialog before deleting a photo
     * @param context Context context of the app
     * @param onDelete function to call when a photo is deleted
     */
    private fun showConfirmationDialog(context: Context, onDelete: () -> Unit) {
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setTitle(R.string.delete_photo_title)
        alertDialogBuilder.setMessage(R.string.delete_photo_message)

        alertDialogBuilder.setPositiveButton(R.string.yes) { _, _ ->
            // remove photo
            onDelete()
        }

        alertDialogBuilder.setNegativeButton(R.string.no) { _, _ ->
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }


    /**
     * This class allows binding to ImageView and TextView in the RecyclerView
     * @param itemView View view to bind
     */
    class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val photoImageView: ImageView = itemView.findViewById(R.id.photoImage)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionText)

        /**
         * This method binds the photo and description to the ImageView and TextView
         * @param photoDescription PhotoDescription photo and description to bind
         */
        fun bind(photoDescription: PhotoDescription) {
            photoImageView.setImageURI(photoDescription.photo)
            descriptionTextView.text = photoDescription.description

        }

        /**
         * Exposing a static create function to handle inflating the layout
         */
        companion object {
            /**
             * This method is used to inflate the layout
             * @param parent ViewGroup that contains other views
             * @return PhotoViewHolder ViewHolder of the actual data
             */
            fun create(parent: ViewGroup): PhotoViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.gallery_item, parent, false)
                return PhotoViewHolder(view)
            }
        }
    }

    /**
     * This class is used to compare items in the RecyclerView
     */
    class PhotosComparator : DiffUtil.ItemCallback<PhotoDescription>() {
        /**
         * This method is used to check if two items are the same
         * @param oldItem PhotoDescription old item
         * @param newItem PhotoDescription new item
         * @return Boolean true if items are the same, false otherwise
         */
        override fun areItemsTheSame(
            oldItem: PhotoDescription, newItem: PhotoDescription
        ): Boolean {
            return oldItem.photo == newItem.photo
        }

        /**
         * This method is used to check if two items have the same content
         * @param oldItem PhotoDescription old item
         * @param newItem PhotoDescription new item
         * @return Boolean true if items have the same content, false otherwise
         */
        override fun areContentsTheSame(
            oldItem: PhotoDescription, newItem: PhotoDescription
        ): Boolean {
            return oldItem.photo == newItem.photo && oldItem.description == newItem.description
        }
    }
}
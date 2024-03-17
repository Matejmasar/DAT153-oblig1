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

class GalleryAdapter(private val onDelete: (PhotoDescription) -> Unit) : ListAdapter<PhotoDescription, GalleryAdapter.PhotoViewHolder>(PhotosComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        return PhotoViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)


        holder.itemView.setOnClickListener {
            showConfirmationDialog(holder.itemView.context) { onDelete(current) }
        }
    }

    // show confirmation dialog before deleting photo
    private fun showConfirmationDialog(context: Context, onDelete: () -> Unit){
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


    class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val photoImageView: ImageView = itemView.findViewById(R.id.photoImage)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionText)

        fun bind(photoDescription: PhotoDescription) {
            photoImageView.setImageURI(photoDescription.photo)
            descriptionTextView.text = photoDescription.description

        }

        companion object {
            fun create(parent: ViewGroup): PhotoViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.gallery_item, parent, false)
                return PhotoViewHolder(view)
            }
        }
    }

    class PhotosComparator : DiffUtil.ItemCallback<PhotoDescription>() {
        override fun areItemsTheSame(oldItem: PhotoDescription, newItem: PhotoDescription): Boolean {
            return oldItem.photo == newItem.photo
        }

        override fun areContentsTheSame(oldItem: PhotoDescription, newItem: PhotoDescription): Boolean {
            return oldItem.photo == newItem.photo && oldItem.description == newItem.description
        }
    }
}
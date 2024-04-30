package com.example.myapplication;

import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class StoryViewHolder extends RecyclerView.ViewHolder
{
    // View holder will have story views which will be populated by the adapter

    // Protected views for adapter to access
    protected TextView author_entry, date_entry, title_entry, text_preview_entry, page_entry;
    protected ImageView image_entry, image_entry_loading;



    public StoryViewHolder(@NonNull View itemView)
    {
        super(itemView);
        author_entry = itemView.findViewById(R.id.author_entry);
        date_entry = itemView.findViewById(R.id.date_entry);
        title_entry = itemView.findViewById(R.id.title_entry);

        // Scrollable text, if the preview has a lot of content
        text_preview_entry = itemView.findViewById(R.id.text_preview_entry);
        text_preview_entry.setMovementMethod(new ScrollingMovementMethod());

        page_entry = itemView.findViewById(R.id.page_entry);
        image_entry = itemView.findViewById(R.id.image_entry_loading);
        image_entry_loading = itemView.findViewById(R.id.image_entry);
    }
}

package com.example.myapplication;

import android.graphics.drawable.Drawable;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import java.util.ArrayList;

public class StoryAdapter extends RecyclerView.Adapter<StoryViewHolder>
{

    private final MainActivity mainActivity;
    private final ArrayList<Story> als;
    private String title = "", date = "", zuluDate = "", zuluMonth = "",
            author = "", preview = "", image = "", format = "";
    private boolean check;



    public StoryAdapter(ArrayList <Story> als, MainActivity mainActivity)
    {
        this.als = als;
        this.mainActivity = mainActivity;
    }



    @NonNull
    @Override
    public StoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new StoryViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_entry, parent, false));
    }



    @Override
    public void onBindViewHolder(@NonNull StoryViewHolder holder, int position)
    {
        // Get the particular story and all of its attributes
        Story s = als.get(position);
        title = s.getTitle();
        date = s.getDate();
        author = s.getAuthor();
        image = s.getImage();
        preview = s.getPreview();

        // Show title if available
        check = title.equals("null");
        if (check)
        {
            holder.title_entry.setVisibility(View.INVISIBLE);
        }
        else
        {
            holder.title_entry.setText(title);
        }



        check = date.equals("null");
        if (check)
        {
            holder.date_entry.setVisibility(View.INVISIBLE);
        }
        else
        {
            zuluMonth = date.substring(5, 7);

            if (zuluMonth.equals("01"))
            {
                zuluMonth = "January";
            }
            else if (zuluMonth.equals("02"))
            {
                zuluMonth = "February";
            }
            else if (zuluMonth.equals("03"))
            {
                zuluMonth = "March";
            }
            else if (zuluMonth.equals("04"))
            {
                zuluMonth = "April";
            }
            else if (zuluMonth.equals("05"))
            {
                zuluMonth = "May";
            }
            else if (zuluMonth.equals("06"))
            {
                zuluMonth = "June";
            }
            else if (zuluMonth.equals("07"))
            {
                zuluMonth = "July";
            }
            else if (zuluMonth.equals("08"))
            {
                zuluMonth = "August";
            }
            else if (zuluMonth.equals("09"))
            {
                zuluMonth = "September";
            }
            else if (zuluMonth.equals("10"))
            {
                zuluMonth = "October";
            }
            else if (zuluMonth.equals("11"))
            {
                zuluMonth = "November";
            }
            else if (zuluMonth.equals("12"))
            {
                zuluMonth = "December";
            }

            zuluDate += zuluMonth + " " + date.substring(8, 10) + ", " + date.substring(0, 4) + " "
                    + date.substring(11, 16);
            holder.date_entry.setText(zuluDate);

            // Clear date field for the next story
            zuluDate = "";
        }

        // Show author if available
        check = author.equals("null");
        if (check)
        {
            holder.author_entry.setVisibility(View.INVISIBLE);
        }
        else
        {
            holder.author_entry.setText(author);
        }

        // Show proper image based on availability
        check = image.equals("null");
        holder.image_entry.setVisibility(View.VISIBLE);
        holder.image_entry_loading.setVisibility(View.VISIBLE);
        if (!check)
        {
            Glide.with(mainActivity).load(s.getImage()).addListener(new RequestListener<Drawable>()
                    {
                        @Override
                        public boolean onLoadFailed
                                (@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource)
                        {
                            holder.image_entry.setVisibility(View.VISIBLE);
                            holder.image_entry_loading.setVisibility(View.INVISIBLE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady
                                (Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource)
                        {
                            holder.image_entry_loading.setVisibility(View.INVISIBLE);
                            holder.image_entry.setVisibility(View.VISIBLE);
                            return false;
                        }
                    })
                    .error(R.drawable.brokenimage)
                    .into(holder.image_entry);
        }
        else
        {
            holder.image_entry_loading.setVisibility(View.INVISIBLE);
            holder.image_entry.setImageResource(R.drawable.noimage);
        }

        // Show the preview if available and make it scrollable
        holder.text_preview_entry.setMovementMethod(new ScrollingMovementMethod());
        check = preview.equals("null");
        if (check)
        {
            holder.text_preview_entry.setVisibility(View.INVISIBLE);
        }
        else
        {
            holder.text_preview_entry.setText(s.getPreview());
        }

        // Proper page count shown at bottom of screen
        format = s.getPage() + " of " + s.getNumStories();
        holder.page_entry.setText(format);
    }



    @Override
    public int getItemCount() {
        return als.size();
    }
}

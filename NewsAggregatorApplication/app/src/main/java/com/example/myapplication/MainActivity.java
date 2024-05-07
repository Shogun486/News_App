package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.example.myapplication.databinding.ActivityMainBinding;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity
{
    private final String TAG = "MainActivity", appTitle = "News App";
    private String [] items, categories;
    private String mediaName, format, show = "";
    private static final HashMap<String, ArrayList<Media>> almc = new HashMap<>(); // Media categories
    protected static ArrayList <Media> alm = new ArrayList<>(); // Media outlets
    private static ArrayList <String> almn = new ArrayList<>(); // Media outlet names
    private static ArrayList <Story> als = new ArrayList<>(); // Stories -- changed to static for data persistency
    private static ArrayList <Media> newList;
    private StoryAdapter sa;
    protected DrawerAdapter da;
    private Menu menu;
    private ActionBarDrawerToggle mDrawerToggle;
    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // Using view binding in MainActivity (it's the only activity)
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (savedInstanceState == null)
        {
            // basic iteration of drawer text
            items = new String[10];

            // Without initialization, app will crash upon startup
            for (int i = 0; i < 10; i++)
                items[i] = "Item No. " + (i + 1);
        }

        show = "";
        if (savedInstanceState != null)
        {
            binding.mainViewPager.setBackgroundColor(Color.WHITE);
            show = savedInstanceState.getString("TITLE");
            items = savedInstanceState.getStringArray("ITEMS");
            categories = savedInstanceState.getStringArray("CATEGORIES");
            getSupportActionBar().setTitle(show);

            // For matching content selected in options-menu and drawer view
            if ((items != null) && (categories != null))
            {
                da = new DrawerAdapter(this, R.layout.drawer_item, items, categories);
                binding.mainLeftDrawer.setAdapter(da);
            }
        }
        else
        {
            binding.mainDrawerLayout.setBackgroundColor(Color.RED);
            binding.mainConstraint.setBackground((Drawable)getDrawable(R.drawable.news_background));
            format = appTitle + " (" + items.length + ")";
            getSupportActionBar().setTitle(format);
        }

        // Drawer adapter is for color-customization purposes
        if (savedInstanceState == null)
        {
            da = new DrawerAdapter(this, R.layout.drawer_item, items);
            binding.mainLeftDrawer.setAdapter(da);
        }

        binding.mainLeftDrawer.setOnItemClickListener(
                (parent, view, position, id) -> selectItem(position)
        );

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                binding.mainDrawerLayout,
                R.string.drawer_open,
                R.string.drawer_close
        );
        mDrawerToggle.setDrawerIndicatorEnabled(true);

        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        // Swipe features enabled
        sa = new StoryAdapter(als, this);
        binding.mainViewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        binding.mainViewPager.setAdapter(sa);

        // Get media outlets to display in drawer first
        alm.clear();
        almn.clear();
        almc.clear();
        MediasAPI.fetchOutlets(this);
    }



    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }



    // Clicking on a category option
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        show = "";
        newList = new ArrayList<Media>();
        if (mDrawerToggle.onOptionsItemSelected(item))
        {
            Log.d(TAG, "onOptionsItemSelected: mDrawerToggle " + item);
            return true;
        }

        // Clear the media fields immediately and then change to appropriate drawer view
        menu.clear();
        alm.clear();
        almn.clear();

        if (item.getTitle().toString().equals("All"))
        {
            almc.clear();
            MediasAPI.fetchOutlets(this);
        }
        else
        {
            setTitle(item.getTitle());

            ArrayList <Media> ml_temp = almc.get(item.getTitle().toString());


            if (ml_temp != null)
                alm.addAll(ml_temp);

            // Now that relevant media are fetched, get their names
            for (Media m: alm)
            {
                mediaName = m.getName();
                almn.add(mediaName);

                if (!newList.contains(m))
                    newList.add(m);
            }

            // New data set received, so update views
            updateData(newList);

            // Ensure toggle/drawer functionality
            binding.mainLeftDrawer.setOnItemClickListener(
                    (parent, view, position, id) -> selectItem(position)
            );

            mDrawerToggle = new ActionBarDrawerToggle(
                    this,
                    binding.mainDrawerLayout,
                    R.string.drawer_open,
                    R.string.drawer_close
            );
            mDrawerToggle.setDrawerIndicatorEnabled(true);


            if (getSupportActionBar() != null)
            {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeButtonEnabled(true);
            }
        }

        // This is for the proper title to show
        if (!show.isEmpty())
        {
            getSupportActionBar().setTitle(show);
        }
        else
        {
            format = appTitle + " (" + newList.size() + ")";
            getSupportActionBar().setTitle(format);
        }
        return true;
    }



    public void updateData(ArrayList <Media> toUpdate)
    {
        int numItems = toUpdate.size();

        for (Media m : toUpdate)
        {
            String category = m.getCategory();

            // Each category has specific media sources
            if (!almc.containsKey(category)) {
                String toShow = String.valueOf(category.charAt(0)).toUpperCase() + category.substring(1, category.length());
                almc.put(toShow, new ArrayList<>());
            }

            ArrayList<Media> temp = almc.get(category);
            if (temp != null)
                temp.add(m);
        }

        // Selecting "All" means show all categories
        almc.put("All", toUpdate);
        ArrayList <String> tempList = new ArrayList<>(almc.keySet());

        // Arrangement of menu items
        Collections.sort(tempList);
        for (String s : tempList)
        {
            int len = s.length();

            // Set appropriate colors to each category
            SpannableString ss = new SpannableString(s);
            if (s.equals("Business"))
            {
                ss.setSpan(new ForegroundColorSpan(Color.GREEN), 0, len, 0);
            }
            else if (s.equals("Entertainment"))
            {
                ss.setSpan(new ForegroundColorSpan(Color.rgb(250, 113, 7)), 0, len, 0);
            }
            else if (s.equals("General"))
            {
                ss.setSpan(new ForegroundColorSpan(Color.rgb(155, 158, 157)), 0, len, 0);
            }
            else if (s.equals("Health"))
            {
                ss.setSpan(new ForegroundColorSpan(Color.rgb(250, 5, 5)), 0, len, 0);
            }
            else if (s.equals("Science"))
            {
                ss.setSpan(new ForegroundColorSpan(Color.CYAN), 0, len, 0);
            }
            else if (s.equals("Sports"))
            {
                ss.setSpan(new ForegroundColorSpan(Color.MAGENTA), 0, len, 0);
            }
            else if (s.equals("Technology"))
            {
                ss.setSpan(new ForegroundColorSpan(Color.rgb(98, 168, 252)), 0, len, 0);
            }
            menu.add(ss);
        }

        // Up-to-date media selections
        alm.addAll(toUpdate);

        // Items to display in drawer view
        items = new String[numItems];
        categories = new String[numItems];

        for (int i = 0; i < numItems; i++)
        {
            items[i] = ((Media) toUpdate.get(i)).getName();
            String toShow = ((Media) toUpdate.get(i)).getCategory();
            toShow = String.valueOf(toShow.charAt(0)).toUpperCase() + toShow.substring(1, toShow.length());
            categories[i] = toShow;
        }

        // Drawer customization
        da = new DrawerAdapter(this , R.layout.drawer_item, items, categories);
        binding.mainLeftDrawer.setAdapter(da);

        binding.mainLeftDrawer.setOnItemClickListener(
                (parent, view, position, id) -> selectItem(position)
        );

        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                binding.mainDrawerLayout,
                R.string.drawer_open,
                R.string.drawer_close
        );
        mDrawerToggle.setDrawerIndicatorEnabled(true);

        // Proper title display
        if (!show.isEmpty())
        {
            getSupportActionBar().setTitle(show);
        }
        else
        {
            format = appTitle + " (" + items.length + ")";
            getSupportActionBar().setTitle(format);
        }
    }



    private void selectItem(int position)
    {
        da.notifyDataSetChanged();
        getSupportActionBar().setTitle(alm.get(position).getName());
        binding.mainDrawerLayout.closeDrawer(binding.mainLeftDrawer);
        binding.mainConstraint.setBackgroundColor(Color.WHITE);

        // Need to get view pager to go to the first entry (else crashes may occur)
        binding.mainViewPager.setCurrentItem(0);

        // Fetch the latest news for the media outlet selected
        NewsAPI.fetchTopHeadlines(alm.get(position).getID(), this);
    }



    // Proper stories to be shown (need to notify adapter)
    public void setStories(ArrayList <Story> temp)
    {
        // Clear stored stories
        this.als.clear();

        if (temp != null)
        {
            // Collect stories for the specific media outlet
            this.als.addAll(temp);

            // Call adapter and bind views/data
            sa.notifyDataSetChanged();
        }
    }



    // Direct user to correct web page on click of the title, image, or preview-text
    public void goToMediaPage(View v)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(((Story)als.get(binding.mainViewPager.getCurrentItem())).getUrl()));
        startActivity(intent);
    }



    // Upon screen rotation, ensure data persists
    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        outState.putString("TITLE", getSupportActionBar().getTitle().toString());
        outState.putStringArray("ITEMS", items);
        outState.putStringArray("CATEGORIES", categories);

        // Call super last
        super.onSaveInstanceState(outState);
    }
}
package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class DrawerAdapter extends ArrayAdapter<String>
{

    private String [] items, categories;
    private int id;
    private Context context;



    public DrawerAdapter(Context context, int id , String [] items)
    {
        super(context, id, items);
        this.context = context;
        this.id = id;
        this.items = items ;
    }



    public DrawerAdapter(Context context, int id , String [] items, String [] categories)
    {
        super(context, id, items);
        this.context = context;
        this.id = id;
        this.items = items ;
        this.categories = categories;
    }



    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int pos, View v, ViewGroup vg)
    {
        View viewToReturn = v;
        if(viewToReturn == null)
        {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            viewToReturn = vi.inflate(id, null);
        }

        TextView textView = (TextView) viewToReturn.findViewById(R.id.drawerOutlets);

        // This is where the color-coding actually occurs
        if ((categories != null) && (items[pos] != null))
        {
            if ((categories[pos].equals("Business")))
            {
                textView.setTextColor(Color.GREEN);
                textView.setText(items[pos]);
            }
            if ((categories[pos].equals("Entertainment")))
            {
                textView.setTextColor(Color.rgb(250, 113, 7));
                textView.setText(items[pos]);
            }
            if ((categories[pos].equals("General")))
            {
                textView.setTextColor(Color.WHITE);
                textView.setText(items[pos]);
            }
            if ((categories[pos].equals("Health")))
            {
                textView.setTextColor(Color.rgb(250, 5, 5));
                textView.setText(items[pos]);
            }
            if ((categories[pos].equals("Science")))
            {
                textView.setTextColor(Color.CYAN);
                textView.setText(items[pos]);
            }
            if ((categories[pos].equals("Sports")))
            {
                textView.setTextColor(Color.MAGENTA);
                textView.setText(items[pos]);
            }
            if ((categories[pos].equals("Technology")))
            {
                textView.setTextColor(Color.rgb(98, 168, 252));
                textView.setText(items[pos]);
            }
        }
        return viewToReturn;
    }
}

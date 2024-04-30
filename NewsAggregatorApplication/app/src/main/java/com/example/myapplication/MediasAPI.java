package com.example.myapplication;

import android.util.Log;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MediasAPI
{

    // This class will fetch all of the media outlets to display in the drawer view

    private static String TAG = "MediasAPI",
            URL = "https://newsapi.org/v2/sources?apiKey=027c668a8d2a4cde82eb2c0d738eedea";
    private static String category = "", id = "", name = "";
    private static int len;
    private static ArrayList<Media> alm;
    private static Media m;
    private static RequestQueue queue;
    private static MainActivity mainActivity;



    public static void fetchOutlets(MainActivity mainActivity)
    {
        MediasAPI.mainActivity = mainActivity;
        queue = Volley.newRequestQueue(MediasAPI.mainActivity);

        Response.Listener <JSONObject> listener = new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                alm = new ArrayList<>();
                mainActivity.alm.clear();
                try
                {
                    JSONObject initialQuery;
                    JSONArray jsonArraySources;
                    JSONObject jsonObjectSources;

                    initialQuery = new JSONObject(response.toString());
                    jsonArraySources = initialQuery.getJSONArray("sources");

                    len = jsonArraySources.length();
                    int i = 0;
                    while (i < len)
                    {
                        jsonObjectSources = jsonArraySources.getJSONObject(i);
                        category = jsonObjectSources.getString("category");
                        id = jsonObjectSources.getString("id");
                        name = jsonObjectSources.getString("name");
                        m = new Media(category, id, name);
                        alm.add(m);
                        i++;
                    }
                    mainActivity.updateData(alm);
                }
                catch (Exception e)
                {
                    Log.d(TAG, "onResponse: CATCH block");
                }
            }
        };
        Response.ErrorListener error = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                try
                {
                    Log.d(TAG, "onErrorResponse: TRY block");
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        };

        // Provided code for proper API call
        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.GET, URL, null, listener, error)
                {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError
                    {
                        Map <String, String> headers = new HashMap<>();
                        headers.put("User-Agent", "News-App");
                        return headers;
                    }
                };

        queue.add(jsonObjectRequest);

        // Reset URL for future queries
        URL = "https://newsapi.org/v2/sources?apiKey=027c668a8d2a4cde82eb2c0d738eedea";
    }
}


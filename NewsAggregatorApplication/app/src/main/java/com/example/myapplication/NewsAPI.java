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

public class NewsAPI
{

    // This class will fetch a particular media outlet's latest news

    private static String TAG = "NewsAPI", URL = "https://newsapi.org/v2/top-headlines?sources=";
    private static ArrayList<Story> s;
    private static int pageNo, numStories;
    private static Story story;
    private static RequestQueue queue;
    private static MainActivity mainActivity;



    public static void fetchTopHeadlines(String outletID, MainActivity mainActivity)
    {
        NewsAPI.mainActivity = mainActivity;
        URL += outletID + "&apiKey=027c668a8d2a4cde82eb2c0d738eedea";
        queue = Volley.newRequestQueue(NewsAPI.mainActivity);

        Response.Listener <JSONObject> listener = new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                try
                {
                    pageNo = 1;
                    s = new ArrayList<>();
                    String author = "", date = "", title = "", preview = "",
                            page = "", image = "", url = "";

                    JSONObject initialQuery;
                    JSONArray jsonArrayStories;
                    JSONObject story;

                    initialQuery = new JSONObject(response.toString());
                    jsonArrayStories = initialQuery.getJSONArray("articles");
                    numStories = jsonArrayStories.length();

                    int i = 0;
                    while (i < numStories)
                    {
                        story = jsonArrayStories.getJSONObject(i);

                        if (story.has("author"))
                            author = story.getString("author");

                        if (story.has("publishedAt"))
                            date = story.getString("publishedAt");

                        if (story.has("title"))
                            title = story.getString("title");

                        if (story.has("description"))
                            preview = story.getString("description");

                        if (story.has("urlToImage"))
                            image = story.getString("urlToImage");

                        if (story.has("url"))
                            url = story.getString("url");

                        page = Integer.toString(NewsAPI.pageNo);
                        NewsAPI.story = new Story(author, date, title, preview, page, image, url, numStories);
                        s.add(NewsAPI.story);
                        if (pageNo == 10)
                            mainActivity.setStories(s);
                        pageNo++;
                        i++;
                    }
                    mainActivity.setStories(s);
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
                    NewsAPI.mainActivity.updateData(null);
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
                        Map<String, String> headers = new HashMap<>();
                        headers.put("User-Agent", "News-App");
                        return headers;
                    }
                };

        queue.add(jsonObjectRequest);

        // Reset URL for future queries
        URL = "https://newsapi.org/v2/top-headlines?sources=";
    }
}

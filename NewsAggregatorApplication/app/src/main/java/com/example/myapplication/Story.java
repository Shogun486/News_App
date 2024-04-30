package com.example.myapplication;

public class Story
{

    // Each story will have certain attributes (some may be missing, but we try to fetch as many as possible)

    private final String author, date, title, preview, page, image, url;
    private final int numStories;



    public Story(String author, String date, String title, String preview,
                 String page, String image, String url, int numStories)
    {
        this.author = author;
        this.date = date;
        this.title = title;
        this.preview = preview;
        this.page = page;
        this.image = image;
        this.url = url;
        this.numStories = numStories;
    }



    public String getAuthor() {
        return author;
    }
    public String getDate() {
        return date;
    }
    public String getTitle() {
        return title;
    }
    public String getPreview() {
        return preview;
    }
    public String getPage() {
        return page;
    }
    public String getImage() {
        return image;
    }
    public String getUrl() {
        return url;
    }
    public int getNumStories() {
        return numStories;
    }
}

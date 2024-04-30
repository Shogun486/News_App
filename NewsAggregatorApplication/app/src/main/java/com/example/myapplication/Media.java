package com.example.myapplication;

public class Media
{

    // Each media outlet has a category (such as "sports"), the ID given, and a name

    private String category, id, name;



    public Media(String category, String id, String name)
    {
        this.category = category;
        this.id = id;
        this.name = name;
    }



    public String getCategory() {
        return category;
    }
    public String getID() {
        return id;
    }
    public String getName() {
        return name;
    }
}

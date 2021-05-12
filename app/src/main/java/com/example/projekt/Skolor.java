package com.example.projekt;

public class Skolor {

    //Member variables
    private String ID;
    private String name;
    private String type;
    private String location;
    private String category;

    private int size;

    public String getName(String name) { return this.name; }

    public String getLocation(String location) { return this.location; }

    public String getCategory(String category) { return this.category; }

    public int getSize(String size) { return this.size; }



    @Override
    public String toString () {
        return name;
    }
}

package com.example.projekt;

public class Skolor {

    //Member variables
    private String ID;
    private String name;
    private String type;
    private String location;
    private String category;

    private int size;
/*
    public String getName() { return name; }

    public String getLocation() { return location; }

    public String getCategory() { return category; }

    public int getSize() { return size; }

 */

    @Override
    public String toString () {
        return name;
    }
}

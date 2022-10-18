package com.example.exploringsa3.Favourites;

public class FavouriteHelperClass {

    int image;
    String name;
    String title;
    String distance;
    String time;

    public FavouriteHelperClass(int image, String name, String title, String distance, String time) {
        this.image = image;
        this.name = name;
        this.title = title;
        this.distance = distance;
        this.time = time;
    }

    public int getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getName() {
        return name;
    }

    public String getDistance() {
        return distance;
    }

    public String getTime() {
        return time;
    }
}

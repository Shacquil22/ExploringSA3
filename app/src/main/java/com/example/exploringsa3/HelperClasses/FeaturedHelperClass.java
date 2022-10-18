package com.example.exploringsa3.HelperClasses;

public class FeaturedHelperClass {

    int image;
    String title;
    double rating;

    public FeaturedHelperClass(int image, String title, double rating) {
        this.image = image;
        this.title = title;
        this.rating = rating;
    }

    public int getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public double getRating(){return rating;}
}

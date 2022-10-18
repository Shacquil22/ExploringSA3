package com.example.exploringsa3.MostViewed;

public class MostVHelperClass {
    int image;
    double rating;
    String title, description;

    public MostVHelperClass(int image, double rating, String title, String description) {
        this.image = image;
        this.rating = rating;
        this.title = title;
        this.description = description;
    }

    public int getImage() {
        return image;
    }

    public double getRating() {
        return rating;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}

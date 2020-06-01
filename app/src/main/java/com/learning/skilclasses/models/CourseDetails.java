package com.learning.skilclasses.models;

public class CourseDetails {
    private String category;
    private String subCategory;
    private String price;

    public CourseDetails(String category, String subCategory, String price) {
        this.category = category;
        this.subCategory = subCategory;
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public CourseDetails() {
    }
}

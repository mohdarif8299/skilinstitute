package com.learning.skilclasses.models;

import java.util.ArrayList;

public class SubCategoriesModel {
    private String subcategory;
    private String price;
    private ArrayList<String> subjects;

    public SubCategoriesModel(String subcategory, String price,ArrayList<String> subjects) {
        this.subcategory = subcategory;
        this.price = price;
        this.subjects = subjects;
    }

    public SubCategoriesModel() {
    }

    public ArrayList<String> getSubjects() {
        return subjects;
    }

    public void setSubjects(ArrayList<String> subjects) {
        this.subjects = subjects;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}

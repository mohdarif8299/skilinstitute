package com.learning.skilclasses.models;

public class AssignmentUtils {

    private String category;
    private String subCategory;
    private String className;
    private String assignment_path;

    public AssignmentUtils(String category, String subCategory, String className,String assignment_path) {
        this.category = category;
        this.subCategory = subCategory;
        this.className = className;
        this.assignment_path=assignment_path;
    }

    public String getAssignment_path() {
        return assignment_path;
    }

    public void setAssignment_path(String assignment_path) {
        this.assignment_path = assignment_path;
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

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}

package com.learning.skilclasses.models;

public class AssignmentBean {

    private String id, a_class, category, subcategory, assignment_descp, assign_file, file_extension, date;

    public AssignmentBean() {
    }

    public AssignmentBean(String id, String a_class, String category, String subcategory, String assignment_descp, String assign_file, String file_extension, String date) {
        this.id = id;
        this.a_class = a_class;
        this.category = category;
        this.subcategory = subcategory;
        this.assignment_descp = assignment_descp;
        this.assign_file = assign_file;
        this.file_extension = file_extension;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getA_class() {
        return a_class;
    }

    public void setA_class(String a_class) {
        this.a_class = a_class;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public String getAssignment_descp() {
        return assignment_descp;
    }

    public void setAssignment_descp(String assignment_descp) {
        this.assignment_descp = assignment_descp;
    }

    public String getAssign_file() {
        return assign_file;
    }

    public void setAssign_file(String assign_file) {
        this.assign_file = assign_file;
    }

    public String getFile_extension() {
        return file_extension;
    }

    public void setFile_extension(String file_extension) {
        this.file_extension = file_extension;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

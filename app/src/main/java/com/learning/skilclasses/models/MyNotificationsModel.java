package com.learning.skilclasses.models;

public class MyNotificationsModel {

    private String categoryName;
    private String message;
    private String _class;
    private String date;

    public MyNotificationsModel(String categoryName, String message, String date, String _class) {
        this.categoryName = categoryName;
        this.message = message;
        this.date = date;
        this._class = _class;
    }

    public String get_class() {
        return _class;
    }

    public void set_class(String _class) {
        this._class = _class;
    }

    public MyNotificationsModel() {
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

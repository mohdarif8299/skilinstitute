package com.learning.skilclasses.models;

import androidx.annotation.NonNull;

public class urlLists {

    public String key;
    public String value;

    public urlLists(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @NonNull
    @Override
    public String toString() {
        return key;
    }
}

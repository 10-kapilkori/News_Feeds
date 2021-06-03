package com.task.newsapp.model;

import com.google.gson.annotations.SerializedName;

public class SourceModel {
    @SerializedName("id")
    String id;
    @SerializedName("name")
    String name;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

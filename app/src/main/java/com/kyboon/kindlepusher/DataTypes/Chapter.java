package com.kyboon.kindlepusher.DataTypes;

import com.google.gson.annotations.SerializedName;

public class Chapter {
    public String id;
    public String title;
    @SerializedName("cpContent")
    public String content;
}

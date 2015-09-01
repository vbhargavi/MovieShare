package com.bhargavi.laxmi.movieshare.service.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by laxmi on 7/8/15.
 */
public class Configuration {

    @SerializedName("images")
    private ImageConfiguaration imageConfiguaration;

    public ImageConfiguaration getImageConfiguaration() {
        return imageConfiguaration;
    }
}

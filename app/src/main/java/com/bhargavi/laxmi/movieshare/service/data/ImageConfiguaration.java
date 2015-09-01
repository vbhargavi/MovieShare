package com.bhargavi.laxmi.movieshare.service.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by laxmi on 7/8/15.
 */
public class ImageConfiguaration {

    @SerializedName("base_url")
    private String baseUrl;

    @SerializedName("secure_base_url")
    private String secureBaseUrl;

    @SerializedName("backdrop_sizes")
    private String[] backdropSizes;

    private int[] backdropWidths;

    @SerializedName("logo_sizes")
    private String[] logoSizes;

    private int[] logoWidths;

    @SerializedName("still_sizes")
    private String[] stillSizes;

    private int[] stillWidths;

    @SerializedName("profile_sizes")
    private String[] profileSizes;

    private int[] profileWidths;

    @SerializedName("poster_sizes")
    private String[] posterSizes;

    private int[] posterWidths;


    public String getBackDropImageUrl(int width) {
        if (backdropWidths == null) {
            backdropWidths = getSupportedWidthArray(backdropSizes);
        }

        String size = "original";

        for (int i = 0; i < backdropWidths.length; i++) {
            if (width <= backdropWidths[i]) {
                size = backdropSizes[i];
                break;
            }
        }

        return baseUrl + size;
    }

    public String getPosterImageUrl(int width) {
        if (posterWidths == null) {
            posterWidths = getSupportedWidthArray(posterSizes);
        }

        String size = "original";

        for (int i = 0; i < posterWidths.length; i++) {
            if (width <= posterWidths[i]) {
                size = posterSizes[i];
                break;
            }
        }

        return baseUrl + size;
    }

    public String getStillImageUrl(int width) {
        if (stillWidths == null) {
            stillWidths = getSupportedWidthArray(stillSizes);
        }

        String size = "original";

        for (int i = 0; i < stillWidths.length; i++) {
            if (width <= stillWidths[i]) {
                size = stillSizes[i];
                break;
            }
        }

        return baseUrl + size;
    }

    private int[] getSupportedWidthArray(String[] sizes) {
        int[] widthArray = new int[sizes.length];
        for (int i = 0; i < sizes.length; i++) {
            if (sizes[i].startsWith("w")) {
                widthArray[i] = Integer.parseInt(sizes[i].replace("w", ""));
            } else if (sizes[i].contains("original")) {
                widthArray[i] = -1;
            }
        }

        return widthArray;
    }
}

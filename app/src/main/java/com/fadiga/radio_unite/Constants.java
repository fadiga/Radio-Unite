package com.fadiga.radio_unite;

/**
 * Created by fad on 6/2/16.
 */
public class Constants {
    static String app_market_url = "market://details?id=com.fadiga.radio_unite";
    public static final String share = "https://play.google.com/store/apps/details?id=com.fadiga.radio_unite";
    public static final String getLogTag(String activity) {
        return String.format("UniteLog-%s", activity);
    }
}

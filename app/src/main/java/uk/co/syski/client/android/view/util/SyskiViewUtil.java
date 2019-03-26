package uk.co.syski.client.android.view.util;

public class SyskiViewUtil {

    public static String nullToUnknown(String value)
    {
        return (value.equalsIgnoreCase("null") ? "Unknown" : value);
    }

}

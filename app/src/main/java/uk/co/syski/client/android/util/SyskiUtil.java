package uk.co.syski.client.android.util;

public class SyskiUtil {

    public static String nullToUnknown(String value)
    {
        return (value.equalsIgnoreCase("null") ? "Unknown" : value);
    }

}

package uk.co.syski.client.android.model.viewmodel;

public class ModelUtil {

    public static String nullToUnknown(String value)
    {
        return (value == null || value.equalsIgnoreCase("null") ? "Unknown" : value);
    }

}

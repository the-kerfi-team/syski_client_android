package uk.co.syski.client.android.model;

public class HeadedValueModel {
    public final Integer image;
    public final String heading;
    public final String value;

    public HeadedValueModel(String headingIn, String valueIn, Integer imageIn) {
        heading = headingIn;
        value = valueIn;
        image = imageIn;
    }
}

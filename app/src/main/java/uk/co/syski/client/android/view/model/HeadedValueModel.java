package uk.co.syski.client.android.view.model;

public class HeadedValueModel {
    public final Integer image;
    public final String heading;
    public final String value;

    public HeadedValueModel(Integer imageIn, String headingIn, String valueIn) {
        image = imageIn;
        heading = headingIn;
        value = valueIn;
    }
}

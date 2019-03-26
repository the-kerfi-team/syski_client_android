package uk.co.syski.client.android.model.fragment;

public class DoubleHeadedValueModel {
    public final Integer image;
    public final String firstHeading;
    public final String firstValue;
    public final String secondHeading;
    public final String secondValue;

    public DoubleHeadedValueModel(Integer imageIn, String firstHeadingIn, String firstValueIn, String secondHeadingIn,  String secondValueIn) {
        image = imageIn;
        firstHeading = firstHeadingIn;
        firstValue = firstValueIn;
        secondHeading = secondHeadingIn;
        secondValue = secondValueIn;
    }
}

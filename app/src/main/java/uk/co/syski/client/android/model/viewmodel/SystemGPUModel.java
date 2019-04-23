package uk.co.syski.client.android.model.viewmodel;

public class SystemGPUModel {

    private String modelName;
    private String manufacturerName;

    public SystemGPUModel(String modelName, String manufacturerName) {
        this.modelName = modelName;
        this.manufacturerName = manufacturerName;
    }

    public SystemGPUModel()
    {
        this(null, null);
    }

    public String getModelName()
    {
        return ModelUtil.nullToUnknown(modelName);
    }

    public String getManufacturerName()
    {
        return ModelUtil.nullToUnknown(manufacturerName);
    }

}

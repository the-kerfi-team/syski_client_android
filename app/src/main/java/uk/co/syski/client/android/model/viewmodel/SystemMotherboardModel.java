package uk.co.syski.client.android.model.viewmodel;

public class SystemMotherboardModel {

    private String modelName;
    private String manufacturerName;
    private String version;

    public SystemMotherboardModel(String modelName, String manufacturerName, String version)
    {
        this.modelName = modelName;
        this.manufacturerName = manufacturerName;
        this.version = version;
    }

    public SystemMotherboardModel()
    {
        this(null, null, null);
    }

    public String getModelName()
    {
        return ModelUtil.nullToUnknown(modelName);
    }

    public String getManufacturerName()
    {
        return ModelUtil.nullToUnknown(manufacturerName);

    }

    public String getVersion()
    {
        return ModelUtil.nullToUnknown(version);
    }

}

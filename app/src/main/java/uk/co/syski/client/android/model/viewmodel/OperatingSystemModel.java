package uk.co.syski.client.android.model.viewmodel;

public class OperatingSystemModel {

    private String name;
    private String architectureName;
    private String version;

    public OperatingSystemModel(String name, String architectureName, String version)
    {
        this.name = name;
        this.architectureName = architectureName;
        this.version = version;
    }

    public OperatingSystemModel()
    {
        this(null, null, null);
    }

    public String getName()
    {
        return ModelUtil.nullToUnknown(name);
    }

    public String getArchitectureName()
    {
        return ModelUtil.nullToUnknown(architectureName);

    }

    public String getVersion()
    {
        return ModelUtil.nullToUnknown(version);
    }

}

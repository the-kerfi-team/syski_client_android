package uk.co.syski.client.android.model.viewmodel;

public class SystemBIOSModel
{

    private String manufacturerName;
    private String caption;
    private String version;
    private String date;

    public SystemBIOSModel(String manufacturerName, String caption, String version, String date)
    {
        this.manufacturerName = manufacturerName;
        this.caption = caption;
        this.version = version;
        this.date = date;
    }

    public SystemBIOSModel()
    {
        this(null, null, null, null);
    }

    public String getManufacturerName()
    {
        return ModelUtil.nullToUnknown(manufacturerName);
    }

    public String getCaption()
    {
        return ModelUtil.nullToUnknown(caption);
    }

    public String getVersion()
    {
        return ModelUtil.nullToUnknown(version);
    }

    public String getDate()
    {
        return ModelUtil.nullToUnknown(date);
    }

}

package uk.co.syski.client.android.model.viewmodel;

public class SystemCPUModel {

    private String modelName;
    private String manufacturerName;
    private String architectureName;
    private Integer clockSpeed;
    private Integer coreCount;
    private Integer threadCount;

    public SystemCPUModel(String modelName, String manufacturerName, String architectureName, Integer clockSpeed, Integer coreCount, Integer threadCount)
    {
        this.modelName = modelName;
        this.manufacturerName = manufacturerName;
        this.architectureName = architectureName;
        this.clockSpeed = clockSpeed;
        this.coreCount = coreCount;
        this.threadCount = threadCount;
    }

    public SystemCPUModel()
    {
        this(null, null, null, null, null, null);
    }


    public String getModelName()
    {
        return ModelUtil.nullToUnknown(modelName);
    }

    public String getManufacturerName()
    {
        return ModelUtil.nullToUnknown(manufacturerName);
    }

    public String getArchitectureName()
    {
        return ModelUtil.nullToUnknown(architectureName);
    }

    public String getClockSpeedAsString()
    {
        return ModelUtil.nullToUnknown(String.valueOf(clockSpeed));
    }

    public String getCoreCountAsString()
    {
        return ModelUtil.nullToUnknown(String.valueOf(coreCount));
    }

    public String getThreadCountAsString()
    {
        return ModelUtil.nullToUnknown(String.valueOf(threadCount));
    }

}

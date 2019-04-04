package uk.co.syski.client.android.model.viewmodel;

public class SystemRAMModel {

    private String modelName;
    private String manufacturerName;
    private String memoryTypeName;
    private Long memoryBytes;

    public SystemRAMModel(String modelName, String manufacturerName, String memoryTypeName, Long memoryBytes)
    {
        this.modelName = modelName;
        this.manufacturerName = manufacturerName;
        this.memoryTypeName = memoryTypeName;
        this.memoryBytes = memoryBytes;
    }

    public SystemRAMModel()
    {
        this(null, null, null, null);
    }

    public String getModelName()
    {
        return ModelUtil.nullToUnknown(modelName);
    }

    public String getManufacturerName()
    {
        return ModelUtil.nullToUnknown(manufacturerName);
    }

    public String getMemoryTypeName()
    {
        return ModelUtil.nullToUnknown(memoryTypeName);
    }

    public long getMemoryBytes()
    {
        return memoryBytes;
    }

    public String getMemoryBytesAsString(String ramUnits)
    {
        return ModelUtil.nullToUnknown(formatBytes(memoryBytes, ramUnits));
    }

    private String formatBytes(Long bytes, String ramUnits) {
        if (bytes != null) {
            switch (ramUnits) {
                case "TB":
                    bytes /= 1024;
                case "GB":
                    bytes /= 1024;
                case "MB":
                    bytes /= 1024;
                case "KB":
                    bytes /= 1024;
                    break;
                default:
                    return bytes + "B";
            }

            return bytes + " " + ramUnits;
        }
        else
        {
            return null;
        }
    }

}

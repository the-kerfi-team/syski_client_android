package uk.co.syski.client.android.model.viewmodel;

public class SystemStorageModel {

    private String modelName;
    private String manufacturerName;
    private String memoryTypeName;
    private Long memoryBytes;

    public SystemStorageModel(String modelName, String manufacturerName, String memoryTypeName, Long memoryBytes)
    {
        this.modelName = modelName;
        this.manufacturerName = manufacturerName;
        this.memoryTypeName = memoryTypeName;
        this.memoryBytes = memoryBytes;
    }

    public SystemStorageModel()
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

    public String getMemoryBytesAsString(String storageUnits)
    {
        return ModelUtil.nullToUnknown(formatBytes(memoryBytes, storageUnits));
    }


    private String formatBytes(Long bytes, String storageUnits) {
        if (bytes != null) {
            switch (storageUnits) {
                case "TB":
                    bytes /= 1000;
                case "GB":
                    bytes /= 1000;
                case "MB":
                    bytes /= 1000;
                case "KB":
                    bytes /= 1000;
                    break;
                default:
                    return bytes + " " + "B";
            }

            return bytes + " " + storageUnits;
        }
        else
        {
            return null;
        }
    }

}

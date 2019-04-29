package uk.co.syski.client.android.model.viewmodel;

import java.util.Date;
import java.util.UUID;

public class SystemModel
{

    private UUID id;
    private String hostName;
    private String modelName;
    private String manufacturerName;
    private Date lastUpdated;
    private boolean online;
    private double ping;

    public SystemModel(UUID id, String hostName, String modelName, String manufacturerName)
    {
        this.id = id;
        this.hostName = hostName;
        this.modelName = modelName;
        this.manufacturerName = manufacturerName;
    }

    public UUID getId()
    {
        return id;
    }

    public String getHostName()
    {
        return ModelUtil.nullToUnknown(hostName);
    }

    public String getHostNameAndPing()
    {
        String result = ModelUtil.nullToUnknown(hostName);
        if (online)
        {
            result +=  " (~" + Math.round(ping) + "ms)";
        }
        return result;
    }

    public String getModelName()
    {
        return ModelUtil.nullToUnknown(modelName);
    }

    public String getManufacturerName()
    {
        return ModelUtil.nullToUnknown(manufacturerName);
    }

    public boolean getOnline()
    {
        return online;
    }

    public void setOnline()
    {
        online = true;
    }

    public void setOffline()
    {
        online = false;
    }

    public double getPing()
    {
        return ping;
    }

    public void setPing(double ping)
    {
        this.ping = ping;
    }

}

package uk.co.syski.client.android.data.system;

import java.util.Date;
import java.util.UUID;
import android.arch.persistence.room.*;

@Entity
public class System {
    @PrimaryKey
    public UUID Id;

    public String ModelName;

    public String ManufacturerName;

    public String HostName;

    public Date LastUpdated;
}

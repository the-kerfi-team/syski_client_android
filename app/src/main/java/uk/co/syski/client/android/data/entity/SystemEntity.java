package uk.co.syski.client.android.data.entity;

        import java.util.Date;
        import java.util.UUID;
        import android.arch.persistence.room.*;
        import android.support.annotation.NonNull;

@Entity
public class SystemEntity {
    @PrimaryKey @NonNull
    public UUID Id;

    public String ModelName;

    public String ManufacturerName;

    public String HostName;

    public Date LastUpdated;
}

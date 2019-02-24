package uk.co.syski.client.android.data.entity;

        import java.util.Date;
        import java.util.UUID;
        import android.arch.persistence.room.*;
        import android.support.annotation.NonNull;

@Entity(
    foreignKeys = {
        @ForeignKey(
            entity = MotherboardEntity.class,
            parentColumns = "Id",
            childColumns = "MotherboardId"
        )
    }
)
public class SystemEntity {
    @PrimaryKey @NonNull
    public UUID Id;

    public String ModelName;

    public String ManufacturerName;

    public String HostName;

    public Date LastUpdated;

    public UUID MotherboardId;
}

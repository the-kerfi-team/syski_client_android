package uk.co.syski.client.android.model.database.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;
import java.util.UUID;

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

    public String HostName;

    public String ModelName;

    public String ManufacturerName;

    public Date LastUpdated;

    public UUID MotherboardId;
}
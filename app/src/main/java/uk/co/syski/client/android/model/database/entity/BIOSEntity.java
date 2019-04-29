package uk.co.syski.client.android.model.database.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.UUID;

@Entity
public class BIOSEntity
{
    @PrimaryKey
    @NonNull
    public UUID Id;

    public String ManufacturerName;

    public String Caption;

    public String Version;

    public String Date;
}

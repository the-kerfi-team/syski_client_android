package uk.co.syski.client.android.data.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.UUID;

@Entity
public class CPU {
    @PrimaryKey
    public UUID Id;

    public String ModelName;

    public String ManufacturerName;

    public String ArchitectureName;

    public int ClockSpeed;

    public int CoreCount;

    public int ThreadCount;
}

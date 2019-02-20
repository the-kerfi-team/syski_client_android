package uk.co.syski.client.android.data.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.UUID;

@Entity
public class OperatingSystem {
    @PrimaryKey @NonNull
    public UUID Id;

    public String Name;

    @Ignore
    public String ArchitectureName;

    @Ignore
    public String Version;
}


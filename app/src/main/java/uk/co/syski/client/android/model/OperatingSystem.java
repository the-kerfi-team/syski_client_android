package uk.co.syski.client.android.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.UUID;

@Entity
public class OperatingSystem {
    @PrimaryKey
    @NonNull
    public UUID Id;

    public String Name;

    public String ArchitectureName;

    public String Version;
}

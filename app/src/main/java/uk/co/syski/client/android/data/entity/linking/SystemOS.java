package uk.co.syski.client.android.data.entity.linking;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.support.annotation.NonNull;

import java.util.UUID;

import uk.co.syski.client.android.data.entity.OperatingSystem;
import uk.co.syski.client.android.data.entity.System;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(
    primaryKeys = {
        "SystemId",
        "OSId"
    },
    foreignKeys = {
        @ForeignKey(
            entity = System.class,
            parentColumns = "Id",
            childColumns = "SystemId",
            onDelete = CASCADE
        ),
        @ForeignKey(
            entity = OperatingSystem.class,
            parentColumns = "Id",
            childColumns = "OSId",
            onDelete = CASCADE
        )
    }
)
public class SystemOS {
    @NonNull
    public UUID SystemId;

    @NonNull
    public UUID OSId;

    public String ArchitectureName;

    public String Version;
}

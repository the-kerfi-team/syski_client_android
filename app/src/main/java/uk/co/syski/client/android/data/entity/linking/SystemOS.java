package uk.co.syski.client.android.data.entity.linking;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.support.annotation.NonNull;

import java.util.UUID;

import uk.co.syski.client.android.data.entity.OperatingSystem;
import uk.co.syski.client.android.data.entity.System;

@Entity(
    primaryKeys = {
        "SystemId",
        "OSId"
    },
    foreignKeys = {
        @ForeignKey(
            entity = System.class,
            parentColumns = "Id",
            childColumns = "SystemId"
        ),
        @ForeignKey(
            entity = OperatingSystem.class,
            parentColumns = "Id",
            childColumns = "OSId"
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

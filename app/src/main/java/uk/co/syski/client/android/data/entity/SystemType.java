package uk.co.syski.client.android.data.entity;

import java.util.UUID;
import android.arch.persistence.room.*;
import android.support.annotation.NonNull;

import uk.co.syski.client.android.data.entity.System;
import uk.co.syski.client.android.data.entity.Type;

@Entity(
    primaryKeys = {
        "SystemId",
        "TypeId"
    },
    foreignKeys = {
        @ForeignKey(
            entity = System.class,
            parentColumns = "Id",
            childColumns = "SystemId"
        ),
        @ForeignKey(
            entity = Type.class,
            parentColumns = "Id",
            childColumns = "TypeId"
        )
    }
)
public class SystemType {
    @NonNull
    public UUID SystemId;

    @NonNull
    public UUID TypeId;
}

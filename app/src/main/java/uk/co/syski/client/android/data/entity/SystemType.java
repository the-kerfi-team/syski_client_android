package uk.co.syski.client.android.data.entity;

import java.util.UUID;
import android.arch.persistence.room.*;
import android.support.annotation.NonNull;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(
    primaryKeys = {
        "SystemId",
        "TypeId"
    },
    foreignKeys = {
        @ForeignKey(
            entity = System.class,
            parentColumns = "Id",
            childColumns = "SystemId",
            onDelete = CASCADE
        ),
        @ForeignKey(
            entity = Type.class,
            parentColumns = "Id",
            childColumns = "TypeId",
            onDelete = CASCADE
        )
    }
)
public class SystemType {
    @NonNull
    public UUID SystemId;

    @NonNull
    public UUID TypeId;
}

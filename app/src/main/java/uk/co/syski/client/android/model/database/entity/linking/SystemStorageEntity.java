package uk.co.syski.client.android.model.database.entity.linking;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.support.annotation.NonNull;

import java.util.UUID;

import uk.co.syski.client.android.model.database.entity.StorageEntity;
import uk.co.syski.client.android.model.database.entity.SystemEntity;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(
    primaryKeys = {
        "SystemId",
        "StorageId",
        "Slot"
    },
    foreignKeys = {
        @ForeignKey(
            entity = SystemEntity.class,
            parentColumns = "Id",
            childColumns = "SystemId",
            onDelete = CASCADE
        ),
        @ForeignKey(
            entity = StorageEntity.class,
            parentColumns = "Id",
            childColumns = "StorageId",
            onDelete = CASCADE
        )
    }
)
public class SystemStorageEntity {
    @NonNull
    public UUID SystemId;

    @NonNull
    public UUID StorageId;

    @NonNull
    public int Slot;
}

package uk.co.syski.client.android.data.entity.linking;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.support.annotation.NonNull;

import java.util.UUID;

import uk.co.syski.client.android.data.entity.StorageEntity;
import uk.co.syski.client.android.data.entity.SystemEntity;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(
    primaryKeys = {
        "SystemId",
        "StorageId"
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
}

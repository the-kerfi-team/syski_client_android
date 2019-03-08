package uk.co.syski.client.android.data.entity.linking;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.support.annotation.NonNull;

import java.util.UUID;

import uk.co.syski.client.android.data.entity.GPUEntity;
import uk.co.syski.client.android.data.entity.SystemEntity;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(
    primaryKeys = {
        "SystemId",
        "GPUId"
    },
    foreignKeys = {
        @ForeignKey(
            entity = SystemEntity.class,
            parentColumns = "Id",
            childColumns = "SystemId",
            onDelete = CASCADE
        ),
        @ForeignKey(
            entity = GPUEntity.class,
            parentColumns = "Id",
            childColumns = "GPUId",
            onDelete = CASCADE
        )
    }
)
public class SystemGPUEntity {
    @NonNull
    public UUID SystemId;

    @NonNull
    public UUID GPUId;
}

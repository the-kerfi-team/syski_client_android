package uk.co.syski.client.android.data.entity.linking;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.support.annotation.NonNull;

import java.util.UUID;

import uk.co.syski.client.android.data.entity.MotherboardEntity;
import uk.co.syski.client.android.data.entity.SystemEntity;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(
    primaryKeys = {
        "SystemId",
        "MotherboardId"
    },
    foreignKeys = {
        @ForeignKey(
            entity = SystemEntity.class,
            parentColumns = "Id",
            childColumns = "SystemId",
            onDelete = CASCADE
        ),
        @ForeignKey(
            entity = MotherboardEntity.class,
            parentColumns = "Id",
            childColumns = "MotherboardId",
            onDelete = CASCADE
        )
    }
)
public class SystemMotherboardEntity {
    @NonNull
    public UUID SystemId;

    @NonNull
    public UUID MotherboardId;
}

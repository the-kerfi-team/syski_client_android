package uk.co.syski.client.android.model.database.entity.linking;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.support.annotation.NonNull;

import java.util.UUID;

import uk.co.syski.client.android.model.database.entity.SystemEntity;
import uk.co.syski.client.android.model.database.entity.TypeEntity;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(
    primaryKeys = {
        "SystemId",
        "TypeId"
    },
    foreignKeys = {
        @ForeignKey(
            entity = SystemEntity.class,
            parentColumns = "Id",
            childColumns = "SystemId",
            onDelete = CASCADE
        ),
        @ForeignKey(
            entity = TypeEntity.class,
            parentColumns = "Id",
            childColumns = "TypeId",
            onDelete = CASCADE
        )
    }
)
public class SystemTypeEntity {
    @NonNull
    public UUID SystemId;

    @NonNull
    public UUID TypeId;
}

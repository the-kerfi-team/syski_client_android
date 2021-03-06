package uk.co.syski.client.android.model.database.entity.linking;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.support.annotation.NonNull;

import java.util.UUID;

import uk.co.syski.client.android.model.database.entity.RAMEntity;
import uk.co.syski.client.android.model.database.entity.SystemEntity;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(
    primaryKeys = {
        "SystemId",
        "RAMId",
        "DimmSlot"
    },
    foreignKeys = {
        @ForeignKey(
            entity = SystemEntity.class,
            parentColumns = "Id",
            childColumns = "SystemId",
            onDelete = CASCADE
        ),
        @ForeignKey(
            entity = RAMEntity.class,
            parentColumns = "Id",
            childColumns = "RAMId",
            onDelete = CASCADE
        )
    }
)
public class SystemRAMEntity {
    @NonNull
    public UUID SystemId;

    @NonNull
    public UUID RAMId;

    @NonNull
    public int DimmSlot;
}

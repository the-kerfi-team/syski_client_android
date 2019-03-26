package uk.co.syski.client.android.model.database.entity.linking;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.support.annotation.NonNull;

import java.util.UUID;

import uk.co.syski.client.android.model.database.entity.OperatingSystemEntity;
import uk.co.syski.client.android.model.database.entity.SystemEntity;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(
    primaryKeys = {
        "SystemId",
        "OSId"
    },
    foreignKeys = {
        @ForeignKey(
            entity = SystemEntity.class,
            parentColumns = "Id",
            childColumns = "SystemId",
            onDelete = CASCADE
        ),
        @ForeignKey(
            entity = OperatingSystemEntity.class,
            parentColumns = "Id",
            childColumns = "OSId",
            onDelete = CASCADE
        )
    }
)
public class SystemOSEntity {
    @NonNull
    public UUID SystemId;

    @NonNull
    public UUID OSId;

    public String ArchitectureName;

    public String Version;
}

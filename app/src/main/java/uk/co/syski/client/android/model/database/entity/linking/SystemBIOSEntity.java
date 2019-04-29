package uk.co.syski.client.android.model.database.entity.linking;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.support.annotation.NonNull;

import java.util.UUID;

import uk.co.syski.client.android.model.database.entity.BIOSEntity;
import uk.co.syski.client.android.model.database.entity.SystemEntity;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(
        primaryKeys = {
                "SystemId",
        },
        foreignKeys = {
                @ForeignKey(
                        entity = SystemEntity.class,
                        parentColumns = "Id",
                        childColumns = "SystemId",
                        onDelete = CASCADE
                ),
                @ForeignKey(
                        entity = BIOSEntity.class,
                        parentColumns = "Id",
                        childColumns = "BIOSId",
                        onDelete = CASCADE
                )
        }
)
public class SystemBIOSEntity {
    @NonNull
    public UUID SystemId;

    @NonNull
    public UUID BIOSId;
}

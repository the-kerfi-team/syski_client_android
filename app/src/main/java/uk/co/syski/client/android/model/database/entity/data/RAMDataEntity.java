package uk.co.syski.client.android.model.database.entity.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.support.annotation.NonNull;

import java.util.Date;
import java.util.UUID;

import uk.co.syski.client.android.model.database.entity.SystemEntity;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(
        primaryKeys = {
                "SystemId",
                "CollectionDateTime"
        },
        foreignKeys = {
                @ForeignKey(
                        entity = SystemEntity.class,
                        parentColumns = "Id",
                        childColumns = "SystemId",
                        onDelete = CASCADE
                ),
        }
)
public class RAMDataEntity {
    @NonNull
    public UUID SystemId;

    @NonNull
    public int Free;

    @NonNull
    public Date CollectionDateTime;
}

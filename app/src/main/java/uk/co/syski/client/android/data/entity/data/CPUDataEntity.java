package uk.co.syski.client.android.data.entity.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.support.annotation.NonNull;

import java.util.Date;
import java.util.UUID;

import uk.co.syski.client.android.data.entity.CPUEntity;
import uk.co.syski.client.android.data.entity.SystemEntity;

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
public class CPUDataEntity {
    @NonNull
    public UUID SystemId;

    @NonNull
    public Float Load;

    @NonNull
    public Float Processes;

    @NonNull
    public Date CollectionDateTime;
}

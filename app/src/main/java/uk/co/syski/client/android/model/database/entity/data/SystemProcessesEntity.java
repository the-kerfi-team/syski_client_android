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
public class SystemProcessesEntity {

    @NonNull
    public UUID SystemId;

    @NonNull
    public String Name;

    @NonNull
    public long MemSize;

    @NonNull
    public long KernelTime;

    @NonNull
    public String Path;

    @NonNull
    public int Threads;

    @NonNull
    public long UpTime;

    @NonNull
    public int ParentId;

    @NonNull
    public Date CollectionDateTime;
}

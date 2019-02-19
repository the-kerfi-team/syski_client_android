package uk.co.syski.client.android.data.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;

import java.util.UUID;

@Entity(
    primaryKeys = {
            "SystemId",
            "CPUId"
    },
    foreignKeys = {
        @ForeignKey(
            entity = System.class,
            parentColumns = "Id",
            childColumns = "SystemId"
        ),
        @ForeignKey(
            entity = CPU.class,
            parentColumns = "Id",
            childColumns = "CPUId"
        )
    }
)
public class SystemCPU {
    public UUID SystemId;

    public UUID CPUId;
}

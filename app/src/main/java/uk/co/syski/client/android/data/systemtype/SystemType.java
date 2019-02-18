package uk.co.syski.client.android.data.systemtype;

import java.util.UUID;
import android.arch.persistence.room.*;
import uk.co.syski.client.android.data.system.System;
import uk.co.syski.client.android.data.type.Type;

@Entity(
    primaryKeys = {
        "SystemId",
        "TypeId"
    },
    foreignKeys = {
        @ForeignKey(
            entity = System.class,
            parentColumns = "Id",
            childColumns = "SystemId"
        ),
        @ForeignKey(
            entity = Type.class,
            parentColumns = "Id",
            childColumns = "TypeId"
        )
    }
)
public class SystemType {
    public UUID SystemId;
    public UUID TypeId;
}

package uk.co.syski.client.android.model.database.entity;

import java.util.UUID;
import android.arch.persistence.room.*;
import android.support.annotation.NonNull;

@Entity
public class TypeEntity {
    @PrimaryKey  @NonNull
    public UUID Id;

    public String Name;
}

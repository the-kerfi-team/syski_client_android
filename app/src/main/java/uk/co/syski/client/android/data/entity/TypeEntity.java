package uk.co.syski.client.android.data.entity;

import java.util.UUID;
import android.arch.persistence.room.*;
import android.support.annotation.NonNull;

@Entity
public class TypeEntity {
    @PrimaryKey  @NonNull
    public UUID Id;

    public String Name;
}

package uk.co.syski.client.android.data.type;

import java.util.UUID;
import android.arch.persistence.room.*;
import android.support.annotation.NonNull;

@Entity
public class Type {
    @PrimaryKey  @NonNull
    public UUID Id;

    public String Name;
}

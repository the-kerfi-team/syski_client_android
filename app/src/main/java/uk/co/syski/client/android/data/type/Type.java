package uk.co.syski.client.android.data.type;

import java.util.UUID;
import android.arch.persistence.room.*;

@Entity
public class Type {
    @PrimaryKey
    public UUID Id;

    public String Name;
}
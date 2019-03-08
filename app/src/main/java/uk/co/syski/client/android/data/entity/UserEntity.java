package uk.co.syski.client.android.data.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;
import java.util.UUID;

@Entity
public class UserEntity {
    @PrimaryKey
    @NonNull
    public UUID Id;

    public String Email;

    public String RefreshToken;

    public String AccessToken;

    public Date TokenExpiry;
}

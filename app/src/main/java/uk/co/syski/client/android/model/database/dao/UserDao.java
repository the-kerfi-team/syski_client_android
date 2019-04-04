package uk.co.syski.client.android.model.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;
import java.util.UUID;

import uk.co.syski.client.android.model.database.entity.UserEntity;

@Dao
public interface UserDao {

    @Insert
    void insert(UserEntity UserEntity);

    @Insert
    void insert(UserEntity... userEntities);

    @Query("SELECT * FROM UserEntity")
    List<UserEntity> get();

    @Query("SELECT * FROM UserEntity WHERE Id == :Id")
    UserEntity get(UUID Id);

    @Query("SELECT * FROM UserEntity WHERE Id in (:Ids)")
    List<UserEntity> get(UUID... Ids);

    @Update
    void update (UserEntity UserEntity);

    @Update
    void update (UserEntity... userEntities);

    @Delete
    void delete(UserEntity UserEntity);

    @Delete
    void delete(UserEntity... userEntities);
}

package uk.co.syski.client.android.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import uk.co.syski.client.android.data.entity.UserEntity;

@Dao
public interface UserDao {

    @Query("SELECT * FROM UserEntity LIMIT 1")
    UserEntity getUser();

    @Query("SELECT * FROM UserEntity")
    List<UserEntity> getUsers();

    @Query("SELECT COUNT(*) FROM UserEntity")
    int getUsersCount();

    @Query("SELECT AccessToken FROM UserEntity LIMIT 1")
    String getAccessToken();

    @Query("UPDATE UserEntity SET AccessToken=:accessToken WHERE Id = :Id")
    void setAccessToken(UUID Id, String accessToken);

    @Query("SELECT RefreshToken FROM UserEntity LIMIT 1")
    String getRefreshToken();

    @Query("UPDATE UserEntity SET RefreshToken=:refreshToken WHERE Id = :Id")
    void setRefreshToken(UUID Id, String refreshToken);

    @Query("SELECT TokenExpiry FROM UserEntity LIMIT 1")
    Date getTokenExpirey();

    @Query("UPDATE UserEntity SET TokenExpiry=:date WHERE Id = :Id")
    void setTokenExpirey(UUID Id, Date date);

    @Query("UPDATE UserEntity SET AccessToken=:accessToken, RefreshToken=:refreshToken, TokenExpiry=:date WHERE Id = :Id")
    void setToken(UUID Id, String accessToken, String refreshToken, Date date);

    @Insert
    void InsertAll(UserEntity... userEntities);

}

package uk.co.syski.client.android.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import uk.co.syski.client.android.data.entity.User;

@Dao
public interface UserDao {

    @Query("SELECT COUNT(*) FROM User")
    int UserCount();

    @Insert
    void InsertAll(User... Users);

}

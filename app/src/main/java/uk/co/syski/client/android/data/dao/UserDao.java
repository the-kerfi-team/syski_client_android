package uk.co.syski.client.android.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;

import uk.co.syski.client.android.data.entity.User;

@Dao
public interface UserDao {

    @Insert
    void InsertAll(User... Users);

}

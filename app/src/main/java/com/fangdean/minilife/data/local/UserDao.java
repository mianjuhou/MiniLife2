package com.fangdean.minilife.data.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.fangdean.minilife.model.User;

import java.util.List;

@Dao
public interface UserDao {

    @Query("select * from user")
    List<User> getAllLocalUser();

    @Query("select * from user where login_state = 1")
    List<User> getLoginedUser();

    @Query("select * from user where login_state = 0")
    List<User> getLogoutUser();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(User user);

    @Query("update user set login_state = 0")
    void updateLoginState();
}

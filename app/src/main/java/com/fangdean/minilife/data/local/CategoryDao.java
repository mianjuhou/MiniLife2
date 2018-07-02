package com.fangdean.minilife.data.local;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.fangdean.minilife.model.Category;

import java.util.List;

@Dao
public interface CategoryDao {

    @Insert
    Long insert(Category category);

    @Delete
    int delete(Category category);

    @Query("delete from category")
    void deleteAll();

    @Query("select * from category where parent_id = 0 and user_id =(:userId) ")
    LiveData<List<Category>> queryTopCategory(Long userId);

    @Query("select * from category where parent_id = 0 and user_id =(:userId) ")
    List<Category> getTopCategory(Long userId);

    @Query("select * from category where id = (:id)")
    Category queryById(Long id);

    @Query("select * from category where parent_id = (:id)")
    LiveData<List<Category>> querySubCategory(Long id);

    @Query("select * from category where parent_id = (:id)")
    List<Category> getSubCategory(Long id);

    @Query("select max(id) from category")
    long getMaxId();

    @Query("select * from category where id = (:id) and user_id = (:userId)")
    Category queryCategory(Long id, Long userId);

    @Update
    int update(Category category);
}

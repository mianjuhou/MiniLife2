package com.fangdean.minilife.data.local;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.fangdean.minilife.model.Goods;

import java.util.List;

@Dao
public interface GoodsDao {

    @Query("select * from goods where category_id = (:categoryId)")
    LiveData<List<Goods>> getGoodsByCategory(Long categoryId);

    @Insert
    Long insert(Goods goods);

    @Delete
    int delete(Goods goods);

    @Update
    int update(Goods goods);

    @Query("delete from goods where category_id = (:categoryId)")
    int deleteGoodsByCategory(Long categoryId);
}

package com.fangdean.minilife.data.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.fangdean.minilife.model.Category;
import com.fangdean.minilife.model.Goods;
import com.fangdean.minilife.model.User;

@Database(entities = {Category.class, Goods.class, User.class}, version = 8, exportSchema = false)
public abstract class MiniLifeDB extends RoomDatabase {

    public abstract CategoryDao categoryDao();


    public abstract GoodsDao goodsDao();


    public abstract UserDao userDao();
}

package com.fangdean.minilife.app;

import android.app.Application;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import com.fangdean.minilife.data.local.MiniLifeDB;
import com.fangdean.minilife.model.User;

public class App extends Application {

    public static MiniLifeDB dbMiniLife;

    public static User loginedUser;

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        initRoom();
    }

    private void initRoom() {
        dbMiniLife = Room.databaseBuilder(getApplicationContext(), MiniLifeDB.class, "db_minilife")
                .addCallback(new RoomDatabase.Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                    }

                    @Override
                    public void onOpen(@NonNull SupportSQLiteDatabase db) {
                        super.onOpen(db);
                    }
                })
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
    }

    public static Context getContext() {
        return context;
    }
}

package com.openclassrooms.realestatemanager.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.openclassrooms.realestatemanager.model.RealEstate;

@Database(entities = {RealEstate.class}, version = 1)
public abstract class RoomDB extends RoomDatabase {
    private static final String DB_NAME = "R_ESTATE";
    private static RoomDB instance = null;

    public static RoomDB getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    RoomDB.class,
                    DB_NAME
            )
                    .build();
        }
        return instance;
    }

    public abstract RealEstateDao getDao();
}

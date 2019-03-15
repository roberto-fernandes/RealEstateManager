package com.openclassrooms.realestatemanager.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.openclassrooms.realestatemanager.model.RealEstateListing;

@Database(entities = {RealEstateListing.class}, version = 1)
public abstract class RoomDB extends RoomDatabase {
    private static final String DB_NAME = "REAL_ESTATE_DB";
    private static RoomDB instance = null;

    public static RoomDB getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    RoomDB.class,
                    DB_NAME
            ).build();
        }
        return instance;
    }

    public abstract ListingDao getDao();
}

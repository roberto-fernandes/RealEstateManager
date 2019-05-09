package com.openclassrooms.realestatemanager.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;

import com.openclassrooms.realestatemanager.model.RealEstate;

@Database(entities = {RealEstate.class}, version = 4)
public abstract class RoomDB extends RoomDatabase {
    private static final String DB_NAME = "REAL_ESTATE_DB";
    private static RoomDB instance = null;

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE realEstateListings"
                    + " ADD COLUMN longDescription TEXT");
        }
    };

    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE realEstateListings"
                    + " ADD COLUMN price TEXT");
        }
    };


    static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE realEstateListings"
                   + " ADD COLUMN numberOfBedrooms TEXT");
        }
    };

    public static RoomDB getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    RoomDB.class,
                    DB_NAME
            ).addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4 ).
                    build();
        }
        return instance;
    }

    public abstract RealEstateDao getDao();
}

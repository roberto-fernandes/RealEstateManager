package com.openclassrooms.realestatemanager.db;

import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import android.content.Context;
import androidx.annotation.NonNull;

import com.openclassrooms.realestatemanager.model.RealEstate;

@Database(entities = {RealEstate.class}, version = 4)
public abstract class RoomDB extends RoomDatabase {
    private static final String DB_NAME = "REAL_ESTATE_DB";
    private static RoomDB instance = null;

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE realEstateListings"
                    + " ADD COLUMN longDescription TEXT");
        }
    };

    private static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE realEstateListings"
                    + " ADD COLUMN price TEXT");
        }
    };


    private static final Migration MIGRATION_3_4 = new Migration(3, 4) {
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

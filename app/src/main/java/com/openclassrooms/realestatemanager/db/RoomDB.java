package com.openclassrooms.realestatemanager.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.openclassrooms.realestatemanager.model.RealEstateListing;

@Database(entities = {RealEstateListing.class}, version = 1)
public abstract class RoomDB  extends RoomDatabase {

}

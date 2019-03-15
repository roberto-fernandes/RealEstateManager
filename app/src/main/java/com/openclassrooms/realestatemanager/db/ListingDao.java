package com.openclassrooms.realestatemanager.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.openclassrooms.realestatemanager.model.RealEstateListing;

import java.util.List;

@Dao
public interface ListingDao {

    //return can be void but the long[] is the rows data were inserted
    @Insert
    long[] intertListing(RealEstateListing... realEstateListings);

    //return can be void but the int is how many rows were updated
    @Update
    int updateListing(RealEstateListing... realEstateListings);

    //return can be void but the int is how many rows were deleted
    @Delete
    int deleteListing(RealEstateListing... realEstateListings);

    @Query("SELECT * FROM realEstateListings")
    LiveData<List<RealEstateListing>> getAllListings();

    @Query("SELECT * FROM realEstateListings WHERE status = :status")
    LiveData<List<RealEstateListing>> getListingByStatus(String status);

}

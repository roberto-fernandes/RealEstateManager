package com.openclassrooms.realestatemanager.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.openclassrooms.realestatemanager.model.RealEstate;

import java.util.List;

@SuppressWarnings("UnusedReturnValue")
@Dao
public interface RealEstateDao {

    //return can be void but the long[] is the rows data were inserted
    @Insert
    long[] insertRealEstate(RealEstate... realEstates);

    //return can be void but the int is how many rows were updated
    @Update
    int updateRealEstate(RealEstate... realEstates);

    //return can be void but the int is how many rows were deleted
    @Delete
    int deleteRealEstate(RealEstate... realEstates);

    @Query("SELECT * FROM realEstateListings")
    LiveData<List<RealEstate>> getAllListings();

    //priceInDollars is NumOfBedrooms
    @Query("SELECT * FROM realEstateListings WHERE surfaceArea BETWEEN :minSurface  AND :maxSurface"
            + " AND numberOfRooms BETWEEN  :minNumOfRooms AND :maxNumOfRooms "
            + "AND priceInDollars BETWEEN :minNumOfBedrooms AND :maxNumOfBedrooms "
            + "AND (status LIKE :sold OR status LIKE :available)"
    )
    LiveData<List<RealEstate>> getFilteredListing(
            String minSurface, String maxSurface
            , String minNumOfRooms, String maxNumOfRooms
            , String minNumOfBedrooms, String maxNumOfBedrooms
            , String sold, String available
    );

    @Query("SELECT * FROM realEstateListings WHERE (address LIKE '%' || :term || '%' " +
            "OR description LIKE '%' || :term || '%' " +
            "OR type LIKE '%' || :term || '%')")
    LiveData<List<RealEstate>> getSearchedListing(
            String term
    );

}

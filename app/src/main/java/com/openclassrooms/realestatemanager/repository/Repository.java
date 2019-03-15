package com.openclassrooms.realestatemanager.repository;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.openclassrooms.realestatemanager.db.ListingDao;
import com.openclassrooms.realestatemanager.db.RoomDB;
import com.openclassrooms.realestatemanager.model.RealEstateListing;

import java.util.List;

public class Repository {
   private ListingDao dao;

    public Repository(Context context) {
        dao = RoomDB.getInstance(context).getDao();
    }

    public void insertListing(RealEstateListing listing) {

    }

    public void updateListing(RealEstateListing listing) {

    }

    public void deleteListing(RealEstateListing listing) {

    }

    public LiveData<List<RealEstateListing>> getAllListings() {
        return dao.getAllListings();
    }

    public LiveData<List<RealEstateListing>> getAllListingsByStatus (String status) {
        return dao.getListingByStatus(status);
    }
}

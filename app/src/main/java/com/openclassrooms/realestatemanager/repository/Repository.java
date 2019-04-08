package com.openclassrooms.realestatemanager.repository;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.openclassrooms.realestatemanager.async.AsyncDBDelete;
import com.openclassrooms.realestatemanager.async.AsyncDBInsert;
import com.openclassrooms.realestatemanager.async.AsyncDBUpdate;
import com.openclassrooms.realestatemanager.db.RealEstateDao;
import com.openclassrooms.realestatemanager.db.RoomDB;
import com.openclassrooms.realestatemanager.model.RealEstate;

import java.util.List;

public class Repository {
   private RealEstateDao dao;

    public Repository(Context context) {
        dao = RoomDB.getInstance(context).getDao();
    }

    public void insertListing(RealEstate realEstate) {
        new AsyncDBInsert(dao).execute(realEstate);
    }

    public void updateListing(RealEstate realEstate) {
        new AsyncDBUpdate(dao).execute(realEstate);
    }

    public void deleteListing(RealEstate realEstate) {
        new AsyncDBDelete(dao).execute(realEstate);
    }

    public LiveData<List<RealEstate>> getAllListings() {
        return dao.getAllListings();
    }

    public LiveData<List<RealEstate>> getAllListingsByStatus (String status) {
        return dao.getListingByStatus(status);
    }
}

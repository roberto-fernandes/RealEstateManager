package com.openclassrooms.realestatemanager.repository;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.openclassrooms.realestatemanager.async.AsyncDBDelete;
import com.openclassrooms.realestatemanager.async.AsyncDBInsert;
import com.openclassrooms.realestatemanager.async.AsyncDBUpdate;
import com.openclassrooms.realestatemanager.db.RealEstateDao;
import com.openclassrooms.realestatemanager.db.RoomDB;
import com.openclassrooms.realestatemanager.model.FilterParams;
import com.openclassrooms.realestatemanager.model.RealEstate;

import java.util.List;

import static com.openclassrooms.realestatemanager.utils.Constants.Status.AVAILABLE;
import static com.openclassrooms.realestatemanager.utils.Constants.Status.SOLD;

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

    public LiveData<List<RealEstate>> filterList(FilterParams filterParamse) {
        String soldTerm = "";
        String availableTerm = "";
        if (filterParamse.isSold()) {
            soldTerm = SOLD;
        }
        if (filterParamse.isAvailable()) {
            availableTerm = AVAILABLE;
        }
        return dao.getFilteredListing(
                filterParamse.getMinSurfaceArea(),
                filterParamse.getMaxSurfaceArea()
                , filterParamse.getMinNumOfRooms()
                , filterParamse.getMaxNumOfRooms()
                , filterParamse.getMinNumOfBedRooms()
                , filterParamse.getMaxNumOfBedRooms()
                , soldTerm
                , availableTerm
        );
    }
}

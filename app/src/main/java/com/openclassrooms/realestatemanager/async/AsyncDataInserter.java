package com.openclassrooms.realestatemanager.async;

import android.os.AsyncTask;

import com.openclassrooms.realestatemanager.db.ListingDao;
import com.openclassrooms.realestatemanager.model.RealEstateListing;

public class AsyncDataInserter extends AsyncTask<RealEstateListing, Void, Void> {

    private ListingDao dao;

    public AsyncDataInserter(ListingDao dao) {
        this.dao = dao;
    }

    @Override
    protected Void doInBackground(RealEstateListing... realEstateListings) {
        dao.intertListing(realEstateListings);
        return null;
    }
}

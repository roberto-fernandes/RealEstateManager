package com.openclassrooms.realestatemanager.async;

import android.os.AsyncTask;

import com.openclassrooms.realestatemanager.db.RealEstateDao;
import com.openclassrooms.realestatemanager.model.RealEstate;

public class AsyncDBDelete extends AsyncTask<RealEstate, Void, Void> {

    private RealEstateDao dao;

    public AsyncDBDelete(RealEstateDao dao) {
        this.dao = dao;
    }

    @Override
    protected Void doInBackground(RealEstate... realEstates) {
        dao.deleteRealEstate(realEstates);
        return null;
    }
}

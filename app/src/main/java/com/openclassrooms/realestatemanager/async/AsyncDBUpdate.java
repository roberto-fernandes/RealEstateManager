package com.openclassrooms.realestatemanager.async;

import android.os.AsyncTask;

import com.openclassrooms.realestatemanager.db.RealEstateDao;
import com.openclassrooms.realestatemanager.model.RealEstate;

public class AsyncDBUpdate extends AsyncTask<RealEstate, Void, Void> {

    private RealEstateDao dao;

    public AsyncDBUpdate(RealEstateDao dao) {
        this.dao = dao;
    }

    @Override
    protected Void doInBackground(RealEstate... realEstates) {
        dao.updateRealEstate(realEstates);
        return null;
    }
}

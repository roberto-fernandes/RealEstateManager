package com.openclassrooms.realestatemanager.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;

import com.openclassrooms.realestatemanager.utils.ListTypeConverters;

import java.util.List;

@Entity(tableName = "realEstateListings")
@TypeConverters(ListTypeConverters.class)
public class RealEstate implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int id = 0;

    private String type;
    private int priceInDollars;
    private int surfaceArea;
    private int numberOfRooms;
    private String description;
    private String longDescription;
    private List<String> photos = null;
    private String address;
    private List<String> pointsOfInterest = null;
    private String status;
    private long datePutInMarket;
    private long saleData;
    private String agentID;
    private String price;
    private String numberOfBedrooms;

    public RealEstate() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPriceInDollars() {
        return priceInDollars;
    }

    public void setPriceInDollars(int priceInDollars) {
        this.priceInDollars = priceInDollars;
    }

    public int getSurfaceArea() {
        return surfaceArea;
    }

    public void setSurfaceArea(int surfaceArea) {
        this.surfaceArea = surfaceArea;
    }

    public int getNumberOfRooms() {
        return numberOfRooms;
    }

    public void setNumberOfRooms(int numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<String> getPointsOfInterest() {
        return pointsOfInterest;
    }

    public void setPointsOfInterest(List<String> pointsOfInterest) {
        this.pointsOfInterest = pointsOfInterest;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getDatePutInMarket() {
        return datePutInMarket;
    }

    public void setDatePutInMarket(long datePutInMarket) {
        this.datePutInMarket = datePutInMarket;
    }

    public long getSaleData() {
        return saleData;
    }

    public void setSaleData(long saleData) {
        this.saleData = saleData;
    }

    public String getAgentID() {
        return agentID;
    }

    public void setAgentID(String agentID) {
        this.agentID = agentID;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNumberOfBedrooms() {
        return numberOfBedrooms;
    }

    public void setNumberOfBedrooms(String numberOfBedrooms) {
        this.numberOfBedrooms = numberOfBedrooms;
    }

    protected RealEstate(Parcel in) {
        id = in.readInt();
        type = in.readString();
        priceInDollars = in.readInt();
        surfaceArea = in.readInt();
        numberOfRooms = in.readInt();
        description = in.readString();
        longDescription = in.readString();
        photos = in.createStringArrayList();
        address = in.readString();
        pointsOfInterest = in.createStringArrayList();
        status = in.readString();
        datePutInMarket = in.readLong();
        saleData = in.readLong();
        agentID = in.readString();
        price = in.readString();
        numberOfBedrooms = in.readString();
    }

    public static final Creator<RealEstate> CREATOR = new Creator<RealEstate>() {
        @Override
        public RealEstate createFromParcel(Parcel in) {
            return new RealEstate(in);
        }

        @Override
        public RealEstate[] newArray(int size) {
            return new RealEstate[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(type);
        dest.writeInt(priceInDollars);
        dest.writeInt(surfaceArea);
        dest.writeInt(numberOfRooms);
        dest.writeString(description);
        dest.writeString(longDescription);
        dest.writeStringList(photos);
        dest.writeString(address);
        dest.writeStringList(pointsOfInterest);
        dest.writeString(status);
        dest.writeLong(datePutInMarket);
        dest.writeLong(saleData);
        dest.writeString(agentID);
        dest.writeString(price);
        dest.writeString(numberOfBedrooms);
    }
}

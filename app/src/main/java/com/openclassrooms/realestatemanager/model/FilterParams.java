package com.openclassrooms.realestatemanager.model;

import android.os.Parcel;
import android.os.Parcelable;

public class FilterParams implements Parcelable {
    private String minNumOfRooms = String.valueOf(Integer.MIN_VALUE);
    private String maxNumOfRooms = String.valueOf(Integer.MAX_VALUE);
    private String minNumOfBedRooms = String.valueOf(Integer.MIN_VALUE);
    private String maxNumOfBedRooms = String.valueOf(Integer.MAX_VALUE);
    private String minSurfaceArea = String.valueOf(Integer.MIN_VALUE);
    private String maxSurfaceArea = String.valueOf(Integer.MAX_VALUE);
    private boolean sold;
    private boolean available;

    public FilterParams() {
    }


    protected FilterParams(Parcel in) {
        minNumOfRooms = in.readString();
        maxNumOfRooms = in.readString();
        minNumOfBedRooms = in.readString();
        maxNumOfBedRooms = in.readString();
        minSurfaceArea = in.readString();
        maxSurfaceArea = in.readString();
        sold = in.readByte() != 0;
        available = in.readByte() != 0;
    }

    public static final Creator<FilterParams> CREATOR = new Creator<FilterParams>() {
        @Override
        public FilterParams createFromParcel(Parcel in) {
            return new FilterParams(in);
        }

        @Override
        public FilterParams[] newArray(int size) {
            return new FilterParams[size];
        }
    };

    public String getMinNumOfRooms() {
        return minNumOfRooms;
    }

    public void setMinNumOfRooms(String minNumOfRooms) {
        this.minNumOfRooms = minNumOfRooms;
    }

    public String getMaxNumOfRooms() {
        return maxNumOfRooms;
    }

    public void setMaxNumOfRooms(String maxNumOfRooms) {
        this.maxNumOfRooms = maxNumOfRooms;
    }

    public String getMinNumOfBedRooms() {
        return minNumOfBedRooms;
    }

    public void setMinNumOfBedRooms(String minNumOfBedRooms) {
        this.minNumOfBedRooms = minNumOfBedRooms;
    }

    public String getMaxNumOfBedRooms() {
        return maxNumOfBedRooms;
    }

    public void setMaxNumOfBedRooms(String maxNumOfBedRooms) {
        this.maxNumOfBedRooms = maxNumOfBedRooms;
    }

    public String getMinSurfaceArea() {
        return minSurfaceArea;
    }

    public void setMinSurfaceArea(String minSurfaceArea) {
        this.minSurfaceArea = minSurfaceArea;
    }

    public String getMaxSurfaceArea() {
        return maxSurfaceArea;
    }

    public void setMaxSurfaceArea(String maxSurfaceArea) {
        this.maxSurfaceArea = maxSurfaceArea;
    }

    public boolean isSold() {
        return sold;
    }

    public void setSold(boolean sold) {
        this.sold = sold;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(minNumOfRooms);
        dest.writeString(maxNumOfRooms);
        dest.writeString(minNumOfBedRooms);
        dest.writeString(maxNumOfBedRooms);
        dest.writeString(minSurfaceArea);
        dest.writeString(maxSurfaceArea);
        dest.writeByte((byte) (sold ? 1 : 0));
        dest.writeByte((byte) (available ? 1 : 0));
    }
}
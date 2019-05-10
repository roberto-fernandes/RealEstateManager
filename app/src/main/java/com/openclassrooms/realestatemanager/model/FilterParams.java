package com.openclassrooms.realestatemanager.model;

import android.os.Parcel;
import android.os.Parcelable;

public class FilterParams implements Parcelable {
    private String startNumOfRooms = String.valueOf(Integer.MIN_VALUE);
    private String endNumOfRooms = String.valueOf(Integer.MAX_VALUE);
    private String startNumOfBedRooms = String.valueOf(Integer.MIN_VALUE);
    private String endNumOfBedRooms = String.valueOf(Integer.MAX_VALUE);
    private String startSurfaceArea = String.valueOf(Integer.MIN_VALUE);
    private String endSurfaceArea = String.valueOf(Integer.MAX_VALUE);
    private boolean sold;
    private boolean available;

    public FilterParams() {
    }


    protected FilterParams(Parcel in) {
        startNumOfRooms = in.readString();
        endNumOfRooms = in.readString();
        startNumOfBedRooms = in.readString();
        endNumOfBedRooms = in.readString();
        startSurfaceArea = in.readString();
        endSurfaceArea = in.readString();
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

    public String getStartNumOfRooms() {
        return startNumOfRooms;
    }

    public void setStartNumOfRooms(String startNumOfRooms) {
        this.startNumOfRooms = startNumOfRooms;
    }

    public String getEndNumOfRooms() {
        return endNumOfRooms;
    }

    public void setEndNumOfRooms(String endNumOfRooms) {
        this.endNumOfRooms = endNumOfRooms;
    }

    public String getStartNumOfBedRooms() {
        return startNumOfBedRooms;
    }

    public void setStartNumOfBedRooms(String startNumOfBedRooms) {
        this.startNumOfBedRooms = startNumOfBedRooms;
    }

    public String getEndNumOfBedRooms() {
        return endNumOfBedRooms;
    }

    public void setEndNumOfBedRooms(String endNumOfBedRooms) {
        this.endNumOfBedRooms = endNumOfBedRooms;
    }

    public String getStartSurfaceArea() {
        return startSurfaceArea;
    }

    public void setStartSurfaceArea(String startSurfaceArea) {
        this.startSurfaceArea = startSurfaceArea;
    }

    public String getEndSurfaceArea() {
        return endSurfaceArea;
    }

    public void setEndSurfaceArea(String endSurfaceArea) {
        this.endSurfaceArea = endSurfaceArea;
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
        dest.writeString(startNumOfRooms);
        dest.writeString(endNumOfRooms);
        dest.writeString(startNumOfBedRooms);
        dest.writeString(endNumOfBedRooms);
        dest.writeString(startSurfaceArea);
        dest.writeString(endSurfaceArea);
        dest.writeByte((byte) (sold ? 1 : 0));
        dest.writeByte((byte) (available ? 1 : 0));
    }
}
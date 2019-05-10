package com.openclassrooms.realestatemanager.model;

import android.os.Parcel;
import android.os.Parcelable;

public class FilterParams implements Parcelable {
    private int startNumOfRooms = Integer.MIN_VALUE;
    private int endNumOfRooms = Integer.MAX_VALUE;
    private int startNumOfBedRooms = Integer.MIN_VALUE;
    private int endNumOfBedRooms = Integer.MAX_VALUE;
    private int startSurfaceArea = Integer.MIN_VALUE;
    private int endSurfaceArea = Integer.MAX_VALUE;
    private boolean sold;
    private boolean available;

    protected FilterParams(Parcel in) {
        startNumOfRooms = in.readInt();
        endNumOfRooms = in.readInt();
        startNumOfBedRooms = in.readInt();
        endNumOfBedRooms = in.readInt();
        startSurfaceArea = in.readInt();
        endSurfaceArea = in.readInt();
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

    public int getStartNumOfRooms() {
        return startNumOfRooms;
    }

    public void setStartNumOfRooms(int startNumOfRooms) {
        this.startNumOfRooms = startNumOfRooms;
    }

    public int getEndNumOfRooms() {
        return endNumOfRooms;
    }

    public void setEndNumOfRooms(int endNumOfRooms) {
        this.endNumOfRooms = endNumOfRooms;
    }

    public int getStartNumOfBedRooms() {
        return startNumOfBedRooms;
    }

    public void setStartNumOfBedRooms(int startNumOfBedRooms) {
        this.startNumOfBedRooms = startNumOfBedRooms;
    }

    public int getEndNumOfBedRooms() {
        return endNumOfBedRooms;
    }

    public void setEndNumOfBedRooms(int endNumOfBedRooms) {
        this.endNumOfBedRooms = endNumOfBedRooms;
    }

    public int getStartSurfaceArea() {
        return startSurfaceArea;
    }

    public void setStartSurfaceArea(int startSurfaceArea) {
        this.startSurfaceArea = startSurfaceArea;
    }

    public int getEndSurfaceArea() {
        return endSurfaceArea;
    }

    public void setEndSurfaceArea(int endSurfaceArea) {
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
        dest.writeInt(startNumOfRooms);
        dest.writeInt(endNumOfRooms);
        dest.writeInt(startNumOfBedRooms);
        dest.writeInt(endNumOfBedRooms);
        dest.writeInt(startSurfaceArea);
        dest.writeInt(endSurfaceArea);
        dest.writeByte((byte) (sold ? 1 : 0));
        dest.writeByte((byte) (available ? 1 : 0));
    }
}

package models;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by rutvik on 1/24/2017 at 12:17 PM.
 */

public class BookedTrip extends RealmObject implements Parcelable
{
    public static final Parcelable.Creator<BookedTrip> CREATOR = new Parcelable.Creator<BookedTrip>()
    {
        @Override
        public BookedTrip createFromParcel(Parcel source)
        {
            return new BookedTrip(source);
        }

        @Override
        public BookedTrip[] newArray(int size)
        {
            return new BookedTrip[size];
        }
    };
    @PrimaryKey
    int customerTripId;
    String tripFrom;
    String tripTo;
    String tripCost = "N/A";
    String tripVehicle = "N/A";
    int status = 0;
    String driverName;
    String driverTripId;
    String driverDeviceToken;

    String tripNumber = "";

    String tripId;

    public BookedTrip()
    {

    }

    public BookedTrip(int customerTripId)
    {
        this.customerTripId = customerTripId;
    }

    public BookedTrip(int customerTripId, String tripFrom, String tripTo, String tripCost, String tripVehicle)
    {
        this.customerTripId = customerTripId;
        this.tripFrom = tripFrom;
        this.tripTo = tripTo;
        this.tripCost = tripCost;
        this.tripVehicle = tripVehicle;
    }

    protected BookedTrip(Parcel in)
    {
        this.customerTripId = in.readInt();
        this.tripFrom = in.readString();
        this.tripTo = in.readString();
        this.tripCost = in.readString();
        this.tripVehicle = in.readString();
        this.status = in.readInt();
        this.driverName = in.readString();
        this.driverTripId = in.readString();
        this.driverDeviceToken = in.readString();
        this.tripNumber = in.readString();
        this.tripId = in.readString();
    }

    public int getCustomerTripId()
    {
        return customerTripId;
    }

    public String getTripFrom()
    {
        return tripFrom;
    }

    public String getTripTo()
    {
        return tripTo;
    }

    public String getTripCost()
    {
        return tripCost;
    }

    public void setTripCost(String tripCost)
    {
        this.tripCost = tripCost;
    }

    public String getTripVehicle()
    {
        return tripVehicle;
    }

    public void setTripVehicle(String tripVehicle)
    {
        this.tripVehicle = tripVehicle;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public String getDriverName()
    {
        return driverName;
    }

    public void setDriverName(String driverName)
    {
        this.driverName = driverName;
    }

    public String getDriverTripId()
    {
        return driverTripId;
    }

    public void setDriverTripId(String driverTripId)
    {
        this.driverTripId = driverTripId;
    }

    public String getDriverDeviceToken()
    {
        return driverDeviceToken;
    }

    public void setDriverDeviceToken(String driverDeviceToken)
    {
        this.driverDeviceToken = driverDeviceToken;
    }

    public String getTripNumber()
    {
        return tripNumber;
    }

    public void setTripNumber(String tripNumber)
    {
        this.tripNumber = tripNumber;
    }

    public String getTripId()
    {
        return tripId;
    }

    public void setTripId(String tripId)
    {
        this.tripId = tripId;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(this.customerTripId);
        dest.writeString(this.tripFrom);
        dest.writeString(this.tripTo);
        dest.writeString(this.tripCost);
        dest.writeString(this.tripVehicle);
        dest.writeInt(this.status);
        dest.writeString(this.driverName);
        dest.writeString(this.driverTripId);
        dest.writeString(this.driverDeviceToken);
        dest.writeString(this.tripNumber);
        dest.writeString(this.tripId);
    }
}


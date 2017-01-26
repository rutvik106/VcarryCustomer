package models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by rutvik on 1/24/2017 at 12:17 PM.
 */

public class BookedTrip extends RealmObject
{
    @PrimaryKey
    int tripId;

    String tripFrom;

    String tripTo;

    String tripCost = "N/A";

    String tripVehicle = "N/A";

    int status = 0;

    String driverName;

    String driverTripId;

    String driverDeviceToken;

    public BookedTrip()
    {

    }

    public BookedTrip(int tripId)
    {
        this.tripId = tripId;
    }

    public BookedTrip(int tripId, String tripFrom, String tripTo, String tripCost, String tripVehicle)
    {
        this.tripId = tripId;
        this.tripFrom = tripFrom;
        this.tripTo = tripTo;
        this.tripCost = tripCost;
        this.tripVehicle = tripVehicle;
    }

    public int getTripId()
    {
        return tripId;
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
}


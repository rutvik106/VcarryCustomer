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

    String tripCost;

    String tripVehicle;

    int status = 0;

    public BookedTrip()
    {

    }

    public BookedTrip(int tripId)
    {
        this.tripId = tripId;
    }

    public BookedTrip(int tripId, String tripFrom, String tripTo)
    {
        this.tripId = tripId;
        this.tripFrom = tripFrom;
        this.tripTo = tripTo;
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
}


package models;

import apimodels.TripByCustomerId;
import io.fusionbit.vcarrycustomer.Constants;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by rutvik on 4/18/2017 at 1:10 PM.
 */

public class BookedTrip extends RealmObject
{

    @PrimaryKey
    String customerTripId;
    String tripId;
    String tripFrom;
    String tripTo;
    String vehicleType;
    String driverName;
    String driverContactNo;
    String driverDeviceToken;
    String tripFare;
    String tripNo;
    String tripStatus;
    TripByCustomerId tripDetails;
    long countDownTime;

    public BookedTrip()
    {

    }

    public BookedTrip(String customerTripId, String tripFrom, String tripTo, String tripFare, String vehicleType)
    {
        this.customerTripId = customerTripId;
        this.tripFrom = tripFrom;
        this.tripTo = tripTo;
        this.tripFare = tripFare;
        this.vehicleType = vehicleType;
        tripStatus = Constants.TRIP_STATUS_PENDING;
    }

    public static TripByCustomerId bakePendingTrip(BookedTrip bookedTrip)
    {
        final TripByCustomerId pendingTrip = new TripByCustomerId();
        pendingTrip.setFare(bookedTrip.getTripFare());
        pendingTrip.setBookedFromLocation(bookedTrip.getTripFrom());
        pendingTrip.setBookedToLocation(bookedTrip.getTripTo());
        pendingTrip.setVehicleType(bookedTrip.getVehicleType());
        pendingTrip.setStatus(bookedTrip.getTripStatus());
        pendingTrip.setCountDownTime(bookedTrip.getCountDownTime());
        return pendingTrip;
    }

    public long getCountDownTime()
    {
        return countDownTime;
    }

    public void setCountDownTime(long countDownTime)
    {
        this.countDownTime = countDownTime;
    }

    public String getCustomerTripId()
    {
        return customerTripId;
    }

    public void setCustomerTripId(TripByCustomerId tripDetails)
    {
        this.tripDetails = tripDetails;
    }

    public String getTripId()
    {
        return tripId;
    }

    public void setTripId(String tripId)
    {
        this.tripId = tripId;
    }

    public String getTripFrom()
    {
        return tripFrom;
    }

    public void setTripFrom(String tripFrom)
    {
        this.tripFrom = tripFrom;
    }

    public String getTripTo()
    {
        return tripTo;
    }

    public void setTripTo(String tripTo)
    {
        this.tripTo = tripTo;
    }

    public String getVehicleType()
    {
        return vehicleType;
    }

    public String getDriverName()
    {
        return driverName;
    }

    public void setDriverName(String driverName)
    {
        this.driverName = driverName;
    }

    public String getDriverContactNo()
    {
        return driverContactNo;
    }

    public void setDriverContactNo(String driverContactNo)
    {
        this.driverContactNo = driverContactNo;
    }

    public String getDriverDeviceToken()
    {
        return driverDeviceToken;
    }

    public void setDriverDeviceToken(String driverDeviceToken)
    {
        this.driverDeviceToken = driverDeviceToken;
    }

    public String getTripFare()
    {
        return tripFare;
    }

    public void setTripFare(String tripFare)
    {
        this.tripFare = tripFare;
    }

    public String getTripNo()
    {
        return tripNo;
    }

    public void setTripNo(String tripNo)
    {
        this.tripNo = tripNo;
    }

    public String getTripStatus()
    {
        return tripStatus;
    }

    public void setTripStatus(String tripStatus)
    {
        this.tripStatus = tripStatus;
    }

    public TripByCustomerId getTripDetails()
    {
        return tripDetails;
    }
}

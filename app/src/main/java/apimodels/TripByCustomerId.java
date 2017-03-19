package apimodels;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.UnsupportedEncodingException;

import io.fusionbit.vcarrycustomer.Constants;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import models.BookedTrip;

/**
 * Created by rutvik on 11/27/2016 at 3:54 PM.
 */

public class TripByCustomerId extends RealmObject implements Comparable<TripByCustomerId>, Parcelable
{


    public static final Creator<TripByCustomerId> CREATOR = new Creator<TripByCustomerId>()
    {
        @Override
        public TripByCustomerId createFromParcel(Parcel source)
        {
            return new TripByCustomerId(source);
        }

        @Override
        public TripByCustomerId[] newArray(int size)
        {
            return new TripByCustomerId[size];
        }
    };
    BookedTrip bookedTrip;
    /**
     * trip_id : 2
     * trip_datetime : 2016-09-07 16:17:18
     * from_shipping_location_id : 15
     * from_shipping_location : Jeet Patel
     * from_city_id : 2
     * from_area_id : 566
     * to_shipping_location_id : 16
     * to_shipping_location : shankar chemicals
     * to_city_id : 2
     * to_area_id : 569
     * vehicle_type_id : 2
     * driver_id : 2
     * fare : 250
     * created_by : 27
     * last_modified_by : 27
     * date_added : 2016-09-07 16:17:25
     * date_modified : 2016-09-07 16:17:25
     * trip_status : 1
     * customer_id : 1663
     * status : New
     * customer_name : Jeet Patel
     */

    @PrimaryKey
    @SerializedName("trip_id")
    private String tripId;
    @SerializedName("trip_datetime")
    private String tripDatetime;
    @SerializedName("from_shipping_location_id")
    private String fromShippingLocationId;
    @SerializedName("from_shipping_location")
    private String fromShippingLocation;
    @SerializedName("from_city_id")
    private String fromCityId;
    @SerializedName("from_area_id")
    private String fromAreaId;
    @SerializedName("to_shipping_location_id")
    private String toShippingLocationId;
    @SerializedName("to_shipping_location")
    private String toShippingLocation;
    @SerializedName("to_city_id")
    private String toCityId;
    @SerializedName("to_area_id")
    private String toAreaId;
    @SerializedName("vehicle_type_id")
    private String vehicleTypeId;
    @SerializedName("driver_id")
    private String driverId;
    @SerializedName("fare")
    private String fare;
    @SerializedName("created_by")
    private String createdBy;
    @SerializedName("last_modified_by")
    private String lastModifiedBy;
    @SerializedName("date_added")
    private String dateAdded;
    @SerializedName("date_modified")
    private String dateModified;
    @SerializedName("trip_status")
    private String tripStatus;
    @SerializedName("customer_id")
    private String customerId;
    @SerializedName("status")
    private String status;
    @SerializedName("customer_name")
    private String customerName;
    /**
     * trip_datetime_dmy : 05/02/2017 11:36:56 AM
     * from_address_line1 : 54, Rangin Park Soc, S.G.Highway. Bodakdev
     * from_address_line2 : Rangin Park Soc
     * from_city_name : Ahmedabad
     * from_area_name : S g highway
     * to_address_line1 : Jodhpur park society
     * to_address_line2 : Jodhpur park society Near Ramdev Nagar BRTS Bus stop
     * to_city_name : Ahmedabad
     * to_area_name : Kankriya
     * customer_contact_no : 9409210477
     * trip_no : 050220170000019
     */

    @SerializedName("trip_datetime_dmy")
    private String tripDatetimeDmy;
    @SerializedName("from_address_line1")
    private String fromAddressLine1;
    @SerializedName("from_address_line2")
    private String fromAddressLine2;
    @SerializedName("from_city_name")
    private String fromCityName;
    @SerializedName("from_area_name")
    private String fromAreaName;
    @SerializedName("to_address_line1")
    private String toAddressLine1;
    @SerializedName("to_address_line2")
    private String toAddressLine2;
    @SerializedName("to_city_name")
    private String toCityName;
    @SerializedName("to_area_name")
    private String toAreaName;
    @SerializedName("customer_contact_no")
    private String customerContactNo;
    @SerializedName("trip_no")
    private String tripNo;
    /**
     * from_gujarati_address : આ એક કસોટી છે
     * to_gujarati_address : આ એક કસોટી છે
     */

    @SerializedName("from_gujarati_address")
    private String fromGujaratiAddress;
    @SerializedName("to_gujarati_address")
    private String toGujaratiAddress;
    private boolean returnGujaratiAddress;

    @SerializedName("vehicle_type")
    private String vehicleType;

    public TripByCustomerId()
    {
    }

    protected TripByCustomerId(Parcel in)
    {
        this.tripId = in.readString();
        this.tripDatetime = in.readString();
        this.fromShippingLocationId = in.readString();
        this.fromShippingLocation = in.readString();
        this.fromCityId = in.readString();
        this.fromAreaId = in.readString();
        this.toShippingLocationId = in.readString();
        this.toShippingLocation = in.readString();
        this.toCityId = in.readString();
        this.toAreaId = in.readString();
        this.vehicleTypeId = in.readString();
        this.driverId = in.readString();
        this.fare = in.readString();
        this.createdBy = in.readString();
        this.lastModifiedBy = in.readString();
        this.dateAdded = in.readString();
        this.dateModified = in.readString();
        this.tripStatus = in.readString();
        this.customerId = in.readString();
        this.status = in.readString();
        this.customerName = in.readString();
        this.tripDatetimeDmy = in.readString();
        this.fromAddressLine1 = in.readString();
        this.fromAddressLine2 = in.readString();
        this.fromCityName = in.readString();
        this.fromAreaName = in.readString();
        this.toAddressLine1 = in.readString();
        this.toAddressLine2 = in.readString();
        this.toCityName = in.readString();
        this.toAreaName = in.readString();
        this.customerContactNo = in.readString();
        this.tripNo = in.readString();
        this.fromGujaratiAddress = in.readString();
        this.toGujaratiAddress = in.readString();
        this.vehicleType = in.readString();
    }

    public BookedTrip getBookedTrip()
    {
        return bookedTrip;
    }

    public void setBookedTrip(BookedTrip bookedTrip)
    {
        this.bookedTrip = bookedTrip;
    }

    public boolean isReturnGujaratiAddress()
    {
        return returnGujaratiAddress;
    }

    public void setReturnGujaratiAddress(boolean returnGujaratiAddress)
    {
        this.returnGujaratiAddress = returnGujaratiAddress;
    }

    public String getTripId()
    {
        return tripId;
    }

    public void setTripId(String tripId)
    {
        this.tripId = tripId;
    }

    public String getTripDatetime()
    {
        return tripDatetime;
    }

    public void setTripDatetime(String tripDatetime)
    {
        this.tripDatetime = tripDatetime;
    }

    public String getFromShippingLocationId()
    {
        return fromShippingLocationId;
    }

    public void setFromShippingLocationId(String fromShippingLocationId)
    {
        this.fromShippingLocationId = fromShippingLocationId;
    }

    public String getFromShippingLocation()
    {
        return getFromAddressLine1() + ", " +
                getFromAddressLine2() + ", " +
                getFromAreaName() + ", " + getFromCityName();
    }

    public void setFromShippingLocation(String fromShippingLocation)
    {
        this.fromShippingLocation = fromShippingLocation;
    }

    public String getFromCityId()
    {
        return fromCityId;
    }

    public void setFromCityId(String fromCityId)
    {
        this.fromCityId = fromCityId;
    }

    public String getFromAreaId()
    {
        return fromAreaId;
    }

    public void setFromAreaId(String fromAreaId)
    {
        this.fromAreaId = fromAreaId;
    }

    public String getToShippingLocationId()
    {
        return toShippingLocationId;
    }

    public void setToShippingLocationId(String toShippingLocationId)
    {
        this.toShippingLocationId = toShippingLocationId;
    }

    public String getToShippingLocation()
    {
        return getToAddressLine1() + ", " +
                getToAddressLine2() + ", " +
                getToAreaName() + ", " + getToCityName();
    }

    public void setToShippingLocation(String toShippingLocation)
    {
        this.toShippingLocation = toShippingLocation;
    }

    public String getToCityId()
    {
        return toCityId;
    }

    public void setToCityId(String toCityId)
    {
        this.toCityId = toCityId;
    }

    public String getToAreaId()
    {
        return toAreaId;
    }

    public void setToAreaId(String toAreaId)
    {
        this.toAreaId = toAreaId;
    }

    public String getVehicleTypeId()
    {
        return vehicleTypeId;
    }

    public void setVehicleTypeId(String vehicleTypeId)
    {
        this.vehicleTypeId = vehicleTypeId;
    }

    public String getDriverId()
    {
        return driverId;
    }

    public void setDriverId(String driverId)
    {
        this.driverId = driverId;
    }

    public String getFare()
    {
        return fare;
    }

    public void setFare(String fare)
    {
        this.fare = fare;
    }

    public String getCreatedBy()
    {
        return createdBy;
    }

    public void setCreatedBy(String createdBy)
    {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy()
    {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy)
    {
        this.lastModifiedBy = lastModifiedBy;
    }

    public String getDateAdded()
    {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded)
    {
        this.dateAdded = dateAdded;
    }

    public String getDateModified()
    {
        return dateModified;
    }

    public void setDateModified(String dateModified)
    {
        this.dateModified = dateModified;
    }

    public String getTripStatus()
    {
        return tripStatus;
    }

    public void setTripStatus(String tripStatus)
    {
        this.tripStatus = tripStatus;
    }

    public String getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId(String customerId)
    {
        this.customerId = customerId;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    /*@Override
    public boolean equals(Object c)
    {
        if (!(c instanceof TripsByDriverMail))
        {
            return false;
        }

        TripsByDriverMail that = (TripsByDriverMail) c;
        return this.getCustomerTripId().equals(that.getCustomerTripId());
    }*/

    public String getCustomerName()
    {
        return customerName;
    }

    public void setCustomerName(String customerName)
    {
        this.customerName = customerName;
    }

    @Override
    public int compareTo(@NonNull TripByCustomerId tripDetails)
    {
        int status = Integer.valueOf(tripDetails.getTripStatus());
        if (status <= Integer.valueOf(Constants.TRIP_STATUS_TRIP_STARTED))
        {
            return 1;
        } else
        {
            return -1;
        }
    }

    public String getTripDatetimeDmy()
    {
        return tripDatetimeDmy;
    }

    public void setTripDatetimeDmy(String tripDatetimeDmy)
    {
        this.tripDatetimeDmy = tripDatetimeDmy;
    }

    public String getFromAddressLine1()
    {
        return fromAddressLine1;
    }

    public void setFromAddressLine1(String fromAddressLine1)
    {
        this.fromAddressLine1 = fromAddressLine1;
    }

    public String getFromAddressLine2()
    {
        return fromAddressLine2;
    }

    public void setFromAddressLine2(String fromAddressLine2)
    {
        this.fromAddressLine2 = fromAddressLine2;
    }

    public String getFromCityName()
    {
        return fromCityName;
    }

    public void setFromCityName(String fromCityName)
    {
        this.fromCityName = fromCityName;
    }

    public String getFromAreaName()
    {
        return fromAreaName;
    }

    public void setFromAreaName(String fromAreaName)
    {
        this.fromAreaName = fromAreaName;
    }

    public String getToAddressLine1()
    {
        return toAddressLine1;
    }

    public void setToAddressLine1(String toAddressLine1)
    {
        this.toAddressLine1 = toAddressLine1;
    }

    public String getToAddressLine2()
    {
        return toAddressLine2;
    }

    public void setToAddressLine2(String toAddressLine2)
    {
        this.toAddressLine2 = toAddressLine2;
    }

    public String getToCityName()
    {
        return toCityName;
    }

    public void setToCityName(String toCityName)
    {
        this.toCityName = toCityName;
    }

    public String getToAreaName()
    {
        return toAreaName;
    }

    public void setToAreaName(String toAreaName)
    {
        this.toAreaName = toAreaName;
    }

    public String getCustomerContactNo()
    {
        return customerContactNo;
    }

    public void setCustomerContactNo(String customerContactNo)
    {
        this.customerContactNo = customerContactNo;
    }

    public String getTripNo()
    {
        return tripNo;
    }

    public void setTripNo(String tripNo)
    {
        this.tripNo = tripNo;
    }

    public String getVehicleType()
    {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType)
    {
        this.vehicleType = vehicleType;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(this.tripId);
        dest.writeString(this.tripDatetime);
        dest.writeString(this.fromShippingLocationId);
        dest.writeString(this.fromShippingLocation);
        dest.writeString(this.fromCityId);
        dest.writeString(this.fromAreaId);
        dest.writeString(this.toShippingLocationId);
        dest.writeString(this.toShippingLocation);
        dest.writeString(this.toCityId);
        dest.writeString(this.toAreaId);
        dest.writeString(this.vehicleTypeId);
        dest.writeString(this.driverId);
        dest.writeString(this.fare);
        dest.writeString(this.createdBy);
        dest.writeString(this.lastModifiedBy);
        dest.writeString(this.dateAdded);
        dest.writeString(this.dateModified);
        dest.writeString(this.tripStatus);
        dest.writeString(this.customerId);
        dest.writeString(this.status);
        dest.writeString(this.customerName);
        dest.writeString(this.tripDatetimeDmy);
        dest.writeString(this.fromAddressLine1);
        dest.writeString(this.fromAddressLine2);
        dest.writeString(this.fromCityName);
        dest.writeString(this.fromAreaName);
        dest.writeString(this.toAddressLine1);
        dest.writeString(this.toAddressLine2);
        dest.writeString(this.toCityName);
        dest.writeString(this.toAreaName);
        dest.writeString(this.customerContactNo);
        dest.writeString(this.tripNo);
        dest.writeString(this.fromGujaratiAddress);
        dest.writeString(this.toGujaratiAddress);
        dest.writeString(this.vehicleType);
    }

    public String getFromGujaratiAddress()
    {
        try
        {
            if (fromGujaratiAddress != null)
            {
                if (!fromGujaratiAddress.isEmpty())
                {
                    // Convert from Unicode to UTF-8
                    String string = fromGujaratiAddress;
                    byte[] utf8 = string.getBytes("UTF-8");

                    // Convert from UTF-8 to Unicode
                    return new String(utf8, "UTF-8");
                } else
                {
                    return getFromAddressLine1() + ", " +
                            getFromAddressLine2() + ", " +
                            getFromAreaName() + ", " +
                            getFromCityName();
                }
            } else
            {
                return getFromAddressLine1() + ", " +
                        getFromAddressLine2() + ", " +
                        getFromAreaName() + ", " +
                        getFromCityName();
            }
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
            return getFromAddressLine1() + ", " +
                    getFromAddressLine2() + ", " +
                    getFromAreaName() + ", " +
                    getFromCityName();
        }
    }

    public void setFromGujaratiAddress(String fromGujaratiAddress)
    {
        this.fromGujaratiAddress = fromGujaratiAddress;
    }

    public String getToGujaratiAddress()
    {
        try
        {
            if (toGujaratiAddress != null)
            {
                if (!toGujaratiAddress.isEmpty())
                {
                    // Convert from Unicode to UTF-8
                    String string = toGujaratiAddress;
                    byte[] utf8 = string.getBytes("UTF-8");

                    // Convert from UTF-8 to Unicode
                    return new String(utf8, "UTF-8");
                } else
                {
                    return getToAddressLine1() + ", " +
                            getToAddressLine2() + ", " +
                            getToAreaName() + ", " +
                            getToCityName();
                }
            } else
            {
                return getToAddressLine1() + ", " +
                        getToAddressLine2() + ", " +
                        getToAreaName() + ", " +
                        getToCityName();
            }
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
            return getToAddressLine1() + ", " +
                    getToAddressLine2() + ", " +
                    getToAreaName() + ", " +
                    getToCityName();
        }
    }

    public void setToGujaratiAddress(String toGujaratiAddress)
    {
        this.toGujaratiAddress = toGujaratiAddress;
    }
}

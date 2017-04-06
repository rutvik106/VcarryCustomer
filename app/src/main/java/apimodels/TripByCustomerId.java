package apimodels;

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

public class TripByCustomerId extends RealmObject implements Comparable<TripByCustomerId>
{


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
    /**
     * 0 : 422
     * 1 : 2017-02-09 14:00:00
     * 2 : 09/02/2017 02:00:00 PM
     * 3 : 29
     * 4 : 30
     * 5 : Pioneer Hydraulics
     * 6 : 21,Shayam Ind.Estate
     * 7 : Near Soni Ni Chali BRTS, NH.No - 8
     * 8 :  ૨૧ ,શયમ  ઇન્ડ .એસ્ટેટ  નેર  સોની  ની ચાલી  BRTS , NH.નો  - ૮
     * 9 :
     * from_gujarati_name :
     * 10 : 2
     * 11 : Ahmedabad
     * 12 : 580
     * 13 : Soni ani chali
     * 14 : Perfect Honing
     * 15 : 36 Gopinath Estate II,
     * 16 : Near Soni Ni Chali BRTS, NH.No - 8
     * 17 : ૩૬  ગોપીનાથ  એસ્ટેટ  II,   Near સોની  ની  ચાલી  BRTS, NH.No - 8
     * 18 :
     * to_gujarati_name :
     * 19 : 2
     * 20 : Ahmedabad
     * 21 : 580
     * 22 : Soni ani chali
     * 23 : 2
     * 24 : Atul Loading
     * 25 : 9
     * 26 : Desai Govind D
     * driver_name : Desai Govind D
     * 27 :
     * vehicle_reg_no :
     * 28 :
     * licence_no :
     * 29 : 50
     * 30 : 27
     * 31 : 27
     * 32 : 2017-02-09 13:48:41
     * 33 : 2017-02-09 13:49:46
     * 34 : 6
     * 35 : 1667
     * 36 : Finished
     * 37 : Pioneer Hydraulics
     * 38 : 9879017267
     * 39 : 090220170000063
     * 40 :
     * cancel_desc :
     * 41 : 0
     * weight : 0
     * 42 :
     * dimensions :
     */

    @SerializedName("from_gujarati_name")
    private String fromGujaratiName;
    @SerializedName("to_gujarati_name")
    private String toGujaratiName;
    @SerializedName("driver_name")
    private String driverName;
    @SerializedName("vehicle_reg_no")
    private String vehicleRegNo;
    @SerializedName("licence_no")
    private String licenceNo;
    @SerializedName("weight")
    private String weight;
    @SerializedName("dimensions")
    private String dimensions;


    public TripByCustomerId()
    {
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

    public String getFromCompanyName()
    {
        return fromShippingLocation;
    }

    public String getToCompantName()
    {
        return toShippingLocation;
    }

    public String getFromGujaratiName()
    {
        return fromGujaratiName != null ? !fromGujaratiName.isEmpty() ? fromGujaratiName :
                getFromCompanyName() : getFromCompanyName();
    }

    public void setFromGujaratiName(String fromGujaratiName)
    {
        this.fromGujaratiName = fromGujaratiName;
    }

    public String getToGujaratiName()
    {
        if (toGujaratiName != null)
        {
            if (!toGujaratiName.isEmpty())
            {
                return toGujaratiName;
            } else
            {
                return getToCompantName();
            }
        } else
        {
            return getToCompantName();
        }
    }

    public void setToGujaratiName(String toGujaratiName)
    {
        this.toGujaratiName = toGujaratiName;
    }

    public String getDriverName()
    {
        return driverName;
    }

    public void setDriverName(String driverName)
    {
        this.driverName = driverName;
    }

    public String getVehicleRegNo()
    {
        return vehicleRegNo;
    }

    public void setVehicleRegNo(String vehicleRegNo)
    {
        this.vehicleRegNo = vehicleRegNo;
    }

    public String getLicenceNo()
    {
        return licenceNo;
    }

    public void setLicenceNo(String licenceNo)
    {
        this.licenceNo = licenceNo;
    }

    public String getWeight()
    {
        return weight;
    }

    public void setWeight(String weight)
    {
        this.weight = weight;
    }

    public String getDimensions()
    {
        return dimensions;
    }

    public void setDimensions(String dimensions)
    {
        this.dimensions = dimensions;
    }
}

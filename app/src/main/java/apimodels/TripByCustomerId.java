package apimodels;

import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

import java.io.UnsupportedEncodingException;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

import static io.fusionbit.vcarrycustomer.Constants.TRIP_STATUS_PENDING;

/**
 * Created by rutvik on 11/27/2016 at 3:54 PM.
 */

public class TripByCustomerId extends RealmObject implements Comparable<TripByCustomerId>
{


    //BookedTrip bookedTrip;
    String bookedFromLocation;
    String bookedToLocation;
    String driverDeviceToken;
    String driverNumber;
    String customerTripId;
    long countDownTime;
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
    private double driverLastKnownLat = 0, driverLastKnownLng = 0;
    /**
     * 0 : 1019
     * 1 : 2017-04-18 15:18:52
     * 2 : 18/04/2017 03:18:52 PM
     * 3 : 25
     * 4 : Amishi Mehta
     * 5 : Jodhpur park society
     * 6 : Jodhpur park society Near Ramdev Nagar BRTS Bus stop
     * 7 :
     * 8 : 2
     * 9 : Ahmedabad
     * 10 : 567
     * 11 : Kankriya
     * 12 : 35
     * 13 : jeet patel
     * 14 : 54, Rangin Park Soc, S.G.Highway. Bodakdev
     * 15 : Rangin Park Soc
     * 16 :
     * 17 : 2
     * 18 : Ahmedabad
     * 19 : 566
     * 20 : S g highway
     * 21 : 3
     * 22 : Pickup Bolero
     * 23 : 2
     * 24 : Rutvik Mehta
     * 25 : 9824143009
     * contact_no_1 : 9824143009
     * 26 : f5YxGZ2qyGI:APA91bE71ZjUy02hHUVYF4yOZgwJFLafcpoy0im6dJetGXRFcxLixpVCYHAGIPixicZWRL7i3ywNW_Rl4Z8dqZulF5hJ0nCF0AOOU-ZNROPeUwJN8f4WdjAaMSZp4rQxvQU-g5cAE2W8
     * device_token : f5YxGZ2qyGI:APA91bE71ZjUy02hHUVYF4yOZgwJFLafcpoy0im6dJetGXRFcxLixpVCYHAGIPixicZWRL7i3ywNW_Rl4Z8dqZulF5hJ0nCF0AOOU-ZNROPeUwJN8f4WdjAaMSZp4rQxvQU-g5cAE2W8
     * 27 : 235
     * 28 : 27
     * 29 : 27
     * 30 : 2017-04-18 15:19:12
     * 31 : 2017-04-18 15:25:44
     * 32 : 2
     * 33 : 1664
     * 34 : Motorist Allocated
     * 35 : Amishi Mehta
     * 36 : 9409210477
     * 37 : 201704180000660
     * 38 : null
     * trip_start_time : null
     * 39 : null
     * trip_stop_time : null
     * 40 : null
     * trip_start_latlong : null
     * 41 : null
     * trip_stop_latlong : null
     * 42 : null
     * trip_distance : null
     * 43 : null
     * memo_amount : null
     * 44 : null
     * labour_amount : null
     * 45 : null
     * cash_received : null
     * 46 : 23.0265273,72.5147981
     * lat_long : 23.0333222,72.5104536
     * 47 : 23.0333222,72.5104536
     * 48 : 0
     * 49 :
     */

    @SerializedName("contact_no_1")
    private String contactNo1;
    @SerializedName("device_token")
    private String deviceToken;
    @SerializedName("trip_start_time")
    private String tripStartTime;
    @SerializedName("trip_stop_time")
    private String tripStopTime;
    @SerializedName("trip_start_latlong")
    private String tripStartLatlong;
    @SerializedName("trip_stop_latlong")
    private String tripStopLatlong;
    @SerializedName("trip_distance")
    private String tripDistance;
    @SerializedName("memo_amount")
    private String memoAmount;
    @SerializedName("labour_amount")
    private String labourAmount;
    @SerializedName("cash_received")
    private String cashReceived;
    @SerializedName("lat_long")
    private String latLong;
    private long driverLocationLastAccess = 0;
    /**
     * 0 : 1108
     * 1 : 2017-04-27 00:31:02
     * 2 : 27/04/2017 12:31:02 AM
     * 3 : 35
     * 4 : 44
     * 5 : jeet patel
     * 6 : 54, Rangin Park Soc, S.G.Highway. Bodakdev
     * 7 : Rangin Park Soc
     * 8 :
     * 9 :
     * 10 : 2
     * 11 : Ahmedabad
     * 12 : 566
     * 13 : S g highway
     * 14 : 23.0333222,72.5104536
     * from_lat_long : 23.0333222,72.5104536
     * 15 : address 2
     * 16 : 109, MB Kadri Road, Raikhad, Ahmedabad, Gujarat 380001
     * 17 :
     * 18 :
     * 19 :
     * 20 : 2
     * 21 : Ahmedabad
     * 22 : 570
     * 23 : Odhav
     * 24 :
     * to_lat_long :
     * 25 : 3
     * 26 : Pickup Bolero
     * 27 : null
     * driver_id : null
     * 28 : null
     * driver_name : null
     * 29 : null
     * vehicle_reg_no : null
     * 30 : null
     * licence_no : null
     * 31 : 1
     * 32 : 27
     * 33 : 27
     * 34 : 2017-04-27 00:31:05
     * 35 : 2017-04-27 00:31:05
     * 36 : 1
     * 37 : 1664
     * 38 : New
     * 39 : Amishi Mehta
     * 40 : 9409210477
     * 41 : 201704270000749
     * 42 :
     * cancel_desc :
     * 43 : 0
     * 44 :
     * 45 : 1
     * driver_fare : 1
     * 46 : null
     * driver_image : null
     * 47 : 9824143009
     * from_contact_no : 9824143009
     * 48 : 9558883184
     * to_contact_no : 9558883184
     */

    @SerializedName("from_lat_long")
    private String fromLatLong;
    @SerializedName("to_lat_long")
    private String toLatLong;
    @SerializedName("driver_image")
    private String driverImage;
    @SerializedName("from_contact_no")
    private String fromContactNo;
    @SerializedName("to_contact_no")
    private String toContactNo;
    public TripByCustomerId()
    {
    }
    public TripByCustomerId(String customerTripId, String tripFrom, String tripTo, String tripCost, String tripVehicle)
    {
        this.customerTripId = customerTripId;
        this.bookedFromLocation = tripFrom;
        this.bookedToLocation = tripTo;
        this.fare = tripCost;
        this.vehicleType = tripVehicle;
    }

    public long getCountDownTime()
    {
        return countDownTime;
    }

    public void setCountDownTime(long countDownTime)
    {
        this.countDownTime = countDownTime;
    }

    public long getDriverLocationLastAccess()
    {
        return driverLocationLastAccess;
    }

    public String getBookedFromLocation()
    {
        return bookedFromLocation;
    }

    public void setBookedFromLocation(String bookedFromLocation)
    {
        this.bookedFromLocation = bookedFromLocation;
    }

    public String getBookedToLocation()
    {
        return bookedToLocation;
    }

    public void setBookedToLocation(String bookedToLocation)
    {
        this.bookedToLocation = bookedToLocation;
    }

    public String getCustomerTripId()
    {
        return customerTripId;
    }

    /*public BookedTrip getBookedTrip()
    {
        return bookedTrip;
    }

    public void setBookedTrip(BookedTrip bookedTrip)
    {
        this.bookedTrip = bookedTrip;
    }*/

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
        return tripStatus == null ? TRIP_STATUS_PENDING : tripStatus;
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
        if (status == Integer.valueOf(getTripStatus()))
        {
            return 0;
        } else if (status < Integer.valueOf(getTripStatus()))
        {
            return 1;
        } else
        {
            return -1;
        }

        //Integer.compare(status,Integer.valueOf(Constants.TRIP_STATUS_TRIP_STARTED));

        /*Date date1 = Utils.convertToDate(tripDetails.getTripDatetime());
        Date date2 = Utils.convertToDate(getTripDatetime());
        return date1.compareTo(date2);*/
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

    public String getToCompanyName()
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
                return getToCompanyName();
            }
        } else
        {
            return getToCompanyName();
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


    public String getDriverDeviceToken()
    {
        return driverDeviceToken;
    }

    public void setDriverDeviceToken(String driverDeviceToken)
    {
        this.driverDeviceToken = driverDeviceToken;
    }

    public String getDriverNumber()
    {
        return driverNumber == null ? contactNo1 : driverNumber;
    }

    public void setDriverNumber(String driverNumber)
    {
        this.driverNumber = driverNumber;
    }


    public void setLastKnownDriverLocation(double latitude, double longitude)
    {
        driverLastKnownLat = latitude;
        driverLastKnownLng = longitude;
    }


    public LatLng getDriverLastKnownLocation()
    {
        if (driverLastKnownLat > 0 && driverLastKnownLng > 0)
        {
            return new LatLng(driverLastKnownLat, driverLastKnownLng);
        } else
        {
            return null;
        }
    }

    public void setDriverLocationLastAccessTime(long mills)
    {
        driverLocationLastAccess = mills;
    }

    public String getContactNo1()
    {
        return contactNo1;
    }

    public void setContactNo1(String contactNo1)
    {
        this.contactNo1 = contactNo1;
    }

    public String getDeviceToken()
    {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken)
    {
        this.deviceToken = deviceToken;
    }

    public String getTripStartTime()
    {
        return tripStartTime;
    }

    public void setTripStartTime(String tripStartTime)
    {
        this.tripStartTime = tripStartTime;
    }

    public String getTripStopTime()
    {
        return tripStopTime;
    }

    public void setTripStopTime(String tripStopTime)
    {
        this.tripStopTime = tripStopTime;
    }

    public String getTripStartLatlong()
    {
        return tripStartLatlong;
    }

    public void setTripStartLatlong(String tripStartLatlong)
    {
        this.tripStartLatlong = tripStartLatlong;
    }

    public String getTripStopLatlong()
    {
        return tripStopLatlong;
    }

    public void setTripStopLatlong(String tripStopLatlong)
    {
        this.tripStopLatlong = tripStopLatlong;
    }

    public String getTripDistance()
    {
        return tripDistance;
    }

    public void setTripDistance(String tripDistance)
    {
        this.tripDistance = tripDistance;
    }

    public String getMemoAmount()
    {
        return memoAmount;
    }

    public void setMemoAmount(String memoAmount)
    {
        this.memoAmount = memoAmount;
    }

    public String getLabourAmount()
    {
        return labourAmount;
    }

    public void setLabourAmount(String labourAmount)
    {
        this.labourAmount = labourAmount;
    }

    public String getCashReceived()
    {
        return cashReceived;
    }

    public void setCashReceived(String cashReceived)
    {
        this.cashReceived = cashReceived;
    }

    public String getLatLong()
    {
        return latLong;
    }

    public void setLatLong(String latLong)
    {
        this.latLong = latLong;
    }

    public String getFromLatLong()
    {
        return fromLatLong;
    }

    public void setFromLatLong(String fromLatLong)
    {
        this.fromLatLong = fromLatLong;
    }

    public String getToLatLong()
    {
        return toLatLong;
    }

    public void setToLatLong(String toLatLong)
    {
        this.toLatLong = toLatLong;
    }

    public String getDriverImage()
    {
        return driverImage;
    }

    public void setDriverImage(String driverImage)
    {
        this.driverImage = driverImage;
    }

    public String getFromContactNo()
    {
        return fromContactNo;
    }

    public void setFromContactNo(String fromContactNo)
    {
        this.fromContactNo = fromContactNo;
    }

    public String getToContactNo()
    {
        return toContactNo;
    }

    public void setToContactNo(String toContactNo)
    {
        this.toContactNo = toContactNo;
    }
}

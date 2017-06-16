package api;

import java.util.List;

import apimodels.AccountSummary;
import apimodels.AccountSummaryNew;
import apimodels.Area;
import apimodels.City;
import apimodels.FromLocation;
import apimodels.TripBreakUpDetails;
import apimodels.TripByCustomerId;
import apimodels.Vehicle;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by rutvik on 1/9/2017 at 3:22 PM.
 */

public interface ApiInterface
{

    @FormUrlEncoded
    @POST("webservice.php")
    Call<List<City>> getCities(@Field("method") String method);

    @FormUrlEncoded
    @POST("webservice.php")
    Call<List<Area>> getAreas(@Field("method") String method,
                              @Field("city_id") String cityId);


    @FormUrlEncoded
    @POST("webservice.php")
    Call<ResponseBody> insertCustomer(@Field("method") String method,
                                      @Field("prefix") String customerNamePrefix,
                                      @Field("customer_name") String customerName,
                                      @Field("customer_address") String customerAddress,
                                      @Field("customer_address2") String customerAddress2,
                                      @Field("customer_city_id") String cityId,
                                      @Field("customer_area") String areaId,
                                      @Field("customerContact") String contact,
                                      @Field("email") String email);


    @FormUrlEncoded
    @POST("webservice.php")
    Call<List<Vehicle>> getVehicleTypes(@Field("method") String method);

    @FormUrlEncoded
    @POST("webservice.php")
    Call<Integer> getCustomerIdFromPhone(@Field("method") String method,
                                         @Field("contact_no") String phone);

    @FormUrlEncoded
    @POST("webservice.php")
    Call<List<FromLocation>> getShippingLocationsForCustomer(@Field("method") String method,
                                                             @Field("customer_id") String customerId);

    @FormUrlEncoded
    @POST("webservice.php")
    Call<Integer> getFareForVehicleTypeLocations(@Field("method") String method,
                                                 @Field("from_shipping_id") String fromShippingId,
                                                 @Field("to_shipping_id") String toShippingLocation,
                                                 @Field("vehicle_type_id") String vehicleTypeId,
                                                 @Field("customer_id") String customerId);

    @FormUrlEncoded
    @POST("webservice.php")
    Call<Integer> insertCustomerTrip(@Field("method") String method,
                                     @Field("from_shipping_id") String fromShippingId,
                                     @Field("to_shipping_id") String toShippingLocation,
                                     @Field("vehicle_type_id") String vehicleTypeId,
                                     @Field("customer_id") String customerId,
                                     @Field("from_new_address") String fromNewAddress,
                                     @Field("to_new_address") String toNewAddress,
                                     @Field("from_lat_long") String fromLatLng,
                                     @Field("to_lat_long") String toLatLng);

    @FormUrlEncoded
    @POST("webservice.php")
    Call<ResponseBody> updateDeviceTokenCustomer(@Field("method") String method,
                                                 @Field("customer_id") String customerId,
                                                 @Field("device_token") String deviceToken);

    @FormUrlEncoded
    @POST("webservice.php")
    Call<AccountSummary> getAccountSummary(@Field("method") String method,
                                           @Field("from") String fromDate,
                                           @Field("to") String toDate,
                                           @Field("customer_id") String email);

    @FormUrlEncoded
    @POST("webservice.php")
    Call<List<TripByCustomerId>> getTripsByCustomerId(@Field("method") String method,
                                                      @Field("customer_id") String customerId);

    @FormUrlEncoded
    @POST("webservice.php")
    Call<List<TripByCustomerId>> getTripSummary(@Field("method") String method,
                                                @Field("customer_id") String customerId,
                                                @Field("trip_status") String tripStatus,
                                                @Field("from_date") String fromDate,
                                                @Field("to_date") String toDate,
                                                @Field("unactioned_driver_email") String unActionedByEmail);

    @FormUrlEncoded
    @POST("webservice.php")
    Call<TripByCustomerId> getTripDetailsByTripId(@Field("method") String method,
                                                  @Field("trip_id") String tripId);

    @FormUrlEncoded
    @POST("webservice.php")
    Call<TripByCustomerId> getTripDetailsByTripNo(@Field("method") String method,
                                                  @Field("trip_no") String tripNo);

    @FormUrlEncoded
    @POST("webservice.php")
    Call<List<String>> getTripNumberLike(@Field("method") String method,
                                         @Field("driver_id") String driverId,
                                         @Field("customer_id") String customerId,
                                         @Field("no") String tripNo);

    @FormUrlEncoded
    @POST("webservice.php")
    Call<AccountSummaryNew> getAccountSummary(@Field("method") String method,
                                              @Field("customer_id") String customerId);

    @FormUrlEncoded
    @POST("webservice.php")
    Call<List<TripBreakUpDetails>> getTripBreakUpDetails(@Field("method") String method,
                                                         @Field("trip_id") String tripId);

}

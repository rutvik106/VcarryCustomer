package api;

import java.util.List;

import apimodels.Area;
import apimodels.City;
import apimodels.FromLocation;
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
    Call<Integer> getCustomerIdFromEmail(@Field("method") String method,
                                         @Field("email") String email);

    @FormUrlEncoded
    @POST("webservice.php")
    Call<List<FromLocation>> getShippingLocationsForCustomer(@Field("method") String method,
                                                             @Field("customer_id") String customerId);

    @FormUrlEncoded
    @POST("webservice.php")
    Call<ResponseBody> getFareForVehicleTypeLocations(@Field("method") String method,
                                                      @Field("from_shipping_id") String fromShippingId,
                                                      @Field("to_shipping_id") String toShippingLocation,
                                                      @Field("vehicle_type_id") String vehicleTypeId);

    @FormUrlEncoded
    @POST("webservice.php")
    Call<ResponseBody> insertCustomerTrip(@Field("method") String method,
                                          @Field("from_shipping_id") String fromShippingId,
                                          @Field("to_shipping_id") String toShippingLocation,
                                          @Field("vehicle_type_id") String vehicleTypeId,
                                          @Field("customer_id") String customerId,
                                          @Field("from_new_address") String fromNewAddress,
                                          @Field("to_new_address") String toNewAddress);

    @FormUrlEncoded
    @POST("webservice.php")
    Call<ResponseBody> updateDeviceTokenCustomer(@Field("method") String method,
                                                 @Field("customer_id") String customerId,
                                                 @Field("device_token") String deviceToken);

}

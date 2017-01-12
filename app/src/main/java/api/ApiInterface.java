package api;

import java.util.List;

import apimodels.Area;
import apimodels.City;
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
    Call<ResponseBody> getCustomerIdFromEmail(@Field("method") String method,
                                              @Field("email") String email);

}

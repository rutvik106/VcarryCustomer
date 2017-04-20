package api;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import javax.inject.Inject;

import apimodels.AccountSummary;
import apimodels.Area;
import apimodels.City;
import apimodels.FromLocation;
import apimodels.TripByCustomerId;
import apimodels.Vehicle;
import io.fusionbit.vcarrycustomer.App;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * Created by rutvik on 1/9/2017 at 3:23 PM.
 */

public class API
{

    private static final String TAG = App.APP_TAG + API.class.getSimpleName();
    Retrofit retrofit;
    private ApiInterface apiService;

    @Inject
    public API(Retrofit retrofit)
    {
        this.retrofit = retrofit;
        apiService = retrofit.create(ApiInterface.class);
    }

    //**********************************************************************************************

    public Call<List<City>> getCities(final RetrofitCallbacks<List<City>> callback)
    {
        Call<List<City>> call = apiService.getCities("get_cities");

        call.enqueue(callback);

        return call;
    }

    public Call<List<Area>> getAreas(final String cityId,
                                     final RetrofitCallbacks<List<Area>> callback)
    {
        Call<List<Area>> call = apiService.getAreas("get_areas_for_city", cityId);

        call.enqueue(callback);

        return call;
    }

    public Call<ResponseBody> insertCustomer(final String namePrefix,
                                             final String name,
                                             final String address1,
                                             final String address2,
                                             final String areaId,
                                             final String contact,
                                             final String cityId,
                                             final RetrofitCallbacks<ResponseBody> callback)
    {
        final String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        Call<ResponseBody> call = apiService.insertCustomer("insert_customer", namePrefix, name,
                address1, address2, cityId, areaId, contact, email);

        call.enqueue(callback);

        return call;
    }


    public Call<List<Vehicle>> getVehicleTypes(final RetrofitCallbacks<List<Vehicle>> callback)
    {
        Call<List<Vehicle>> call = apiService.getVehicleTypes("get_vehicle_types");

        call.enqueue(callback);

        return call;
    }

    public Call<Integer> getCustomerIdFromEmail(final String email,
                                                RetrofitCallbacks<Integer> callback)
    {
        Call<Integer> call = apiService.getCustomerIdFromEmail("get_customer_id_from_customer_email",
                email);

        call.enqueue(callback);

        return call;
    }

    public Call<List<FromLocation>> getShippingLocationsForCustomer(final String customerId,
                                                                    RetrofitCallbacks<List<FromLocation>> callback)
    {
        Call<List<FromLocation>> call =
                apiService.getShippingLocationsForCustomer("get_shipping_locations_for_customer",
                        customerId);

        call.enqueue(callback);

        return call;
    }

    public Call<Integer> getFareForVehicleTypeLocations(final String fromShippingId,
                                                        final String toShippingId,
                                                        final String vehicleTypeId,
                                                        RetrofitCallbacks<Integer> callback)
    {
        Call<Integer> call =
                apiService.getFareForVehicleTypeLocations("get_fare_for_vehicle_type_locations",
                        fromShippingId, toShippingId, vehicleTypeId);

        call.enqueue(callback);

        return call;
    }

    public Call<Integer> insertCustomerTrip(final String fromShippingId,
                                            final String toShippingId,
                                            final String vehicleTypeId,
                                            final String customerId,
                                            final String fromNewAddress,
                                            final String toNewAddress,
                                            final String fromLatLng,
                                            final String toLatLng,
                                            RetrofitCallbacks<Integer> callback)
    {
        Call<Integer> call =
                apiService.insertCustomerTrip("insert_customer_trip",
                        fromShippingId, toShippingId, vehicleTypeId, customerId, fromNewAddress,
                        toNewAddress, fromLatLng, toLatLng);

        call.enqueue(callback);

        return call;
    }

    public Call<ResponseBody> updateDeviceTokenCustomer(final String customerId,
                                                        final String deviceToken,
                                                        RetrofitCallbacks<ResponseBody> callback)
    {
        Call<ResponseBody> call =
                apiService.updateDeviceTokenCustomer("update_device_token_customer",
                        customerId, deviceToken);

        call.enqueue(callback);

        return call;
    }

    public void getAccountSummary(final String customerId,
                                  final String fromDate,
                                  final String toDate,
                                  final RetrofitCallbacks<AccountSummary> callback)
    {
        Call<AccountSummary> call = apiService.getAccountSummary("get_customer_balance",
                fromDate, toDate, customerId);

        call.enqueue(callback);
    }

    public Call<List<TripByCustomerId>> getTripsByCustomerId(final String customerId,
                                                             final RetrofitCallbacks<List<TripByCustomerId>> callback)
    {
        Call<List<TripByCustomerId>> call = apiService
                .getTripsByCustomerId("get_trips_by_customer_id", customerId);

        call.enqueue(callback);

        return call;
    }

    public void getTripSummary(final String customerId,
                               final String tripStatus,
                               final String fromDate,
                               final String toDate,
                               final String unActionedByEmail,
                               final RetrofitCallbacks<List<TripByCustomerId>> callback)
    {
        Call<List<TripByCustomerId>> call = apiService.getTripSummary("get_trips_by_trip_status",
                customerId, tripStatus, fromDate, toDate, unActionedByEmail);

        call.enqueue(callback);
    }

    public void getTripDetailsByTripId(final String tripId,
                                       final RetrofitCallbacks<TripByCustomerId> callback)
    {
        Call<TripByCustomerId> call = apiService.getTripDetailsByTripId("get_trip_details_by_trip_id",
                tripId);

        call.enqueue(callback);
    }

    public Call<TripByCustomerId> getTripDetailsByTripNo(final String tripNo,
                                                         final RetrofitCallbacks<TripByCustomerId> callback)
    {
        Call<TripByCustomerId> call =
                apiService.getTripDetailsByTripNo("get_trip_details_by_trip_no",
                        tripNo);

        call.enqueue(callback);

        return call;
    }

    public Call<List<String>> getTripNumberLike(final String tripNo,
                                                final String customerId,
                                                final RetrofitCallbacks<List<String>> callback)
    {
        Call<List<String>> call =
                apiService.getTripNumberLike("get_trip_nos_like_no", null, customerId, tripNo);

        call.enqueue(callback);

        return call;
    }

}

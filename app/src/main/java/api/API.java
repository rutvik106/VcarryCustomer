package api;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;
import java.util.List;

import apimodels.Area;
import apimodels.City;
import io.fusionbit.vcarrycustomer.App;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by rutvik on 1/9/2017 at 3:23 PM.
 */

public class API
{

    private ApiInterface apiService;

    private static final String TAG = App.APP_TAG + API.class.getSimpleName();

    private static API instance = new API();

    private API()
    {
        apiService = ApiClient.getClient().create(ApiInterface.class);
    }

    public static API getInstance()
    {
        return instance;
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

        final String time = Calendar.getInstance().getTimeInMillis() + "";

        Call<ResponseBody> call = apiService.insertCustomer("insert_customer", namePrefix, name,
                address1, address2, cityId, areaId, contact, email);

        call.enqueue(callback);

        return call;
    }

}

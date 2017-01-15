package api;

import android.support.annotation.CallSuper;

import io.fusionbit.vcarrycustomer.App;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by rutvik on 1/9/2017 at 3:08 PM.
 */

public class RetrofitCallbacks<T> implements Callback<T>
{
    private static final String TAG = App.APP_TAG + RetrofitCallbacks.class.getSimpleName();

    @CallSuper
    @Override
    public void onResponse(Call<T> call, Response<T> response)
    {

    }

    @CallSuper
    @Override
    public void onFailure(Call<T> call, Throwable t)
    {
        if (!call.isCanceled())
        {

        }
    }
}

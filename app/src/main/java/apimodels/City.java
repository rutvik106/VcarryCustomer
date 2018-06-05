package apimodels;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by rutvik on 1/9/2017 at 3:37 PM.
 */

public class City implements SpinnerModel
{


    /**
     * city_id : 2
     * city_name : Ahmedabad
     */

    @SerializedName("city_id")
    private String cityId;
    @SerializedName("city_name")
    private String cityName;

    public String getCityId()
    {
        return cityId;
    }

    public void setCityId(String cityId)
    {
        this.cityId = cityId;
    }

    public String getCityName()
    {
        return cityName;
    }

    public void setCityName(String cityName)
    {
        this.cityName = cityName;
    }

    @Override
    public int getId()
    {
        return Integer.valueOf(cityId);
    }

    @Override
    public String getLabel()
    {
        return cityName;
    }

    @Override
    public String companyName() {
        return null;
    }
}

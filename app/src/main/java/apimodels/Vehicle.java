package apimodels;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by rutvik on 1/12/2017 at 9:22 AM.
 */

public class Vehicle extends RealmObject implements SpinnerModel
{

    /**
     * vehicle_type_id : 2
     * vehicle_type : Atul Loading
     */

    @PrimaryKey
    @SerializedName("vehicle_type_id")
    private String vehicleTypeId;
    @SerializedName("vehicle_type")
    private String vehicleType;

    public String getVehicleTypeId()
    {
        return vehicleTypeId;
    }

    public void setVehicleTypeId(String vehicleTypeId)
    {
        this.vehicleTypeId = vehicleTypeId;
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
    public int getId()
    {
        return Integer.parseInt(getVehicleTypeId());
    }

    @Override
    public String getLabel()
    {
        return getVehicleType();
    }

    @Override
    public String companyName() {
        return null;
    }
}

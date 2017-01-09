package apimodels;

import com.google.gson.annotations.SerializedName;

/**
 * Created by rutvik on 1/9/2017 at 4:08 PM.
 */

public class Area extends SpinnerModel
{


    /**
     * area_id : 567
     * area_name : Kankriya
     */

    @SerializedName("area_id")
    private String areaId;
    @SerializedName("area_name")
    private String areaName;

    public String getAreaId()
    {
        return areaId;
    }

    public void setAreaId(String areaId)
    {
        this.areaId = areaId;
    }

    public String getAreaName()
    {
        return areaName;
    }

    public void setAreaName(String areaName)
    {
        this.areaName = areaName;
    }

    @Override
    public int getId()
    {
        return Integer.valueOf(areaId);
    }

    @Override
    public String getLabel()
    {
        return areaName;
    }
}

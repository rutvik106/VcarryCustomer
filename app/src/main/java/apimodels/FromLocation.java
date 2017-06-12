package apimodels;

import com.google.gson.annotations.SerializedName;

import java.io.UnsupportedEncodingException;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by rutvik on 1/13/2017 at 11:26 PM.
 */

public class FromLocation extends RealmObject implements SpinnerModel
{


    /**
     * shipping_location_id : 34
     * shipping_name : Pankaj
     * shipping_address : zestard
     * shipping_address2 : zestard
     * city_id : 2
     * city_name : Ahmedabad
     * area_name : S g highway
     * area_id : 566
     * cp_name : Pankaj
     * cp_contact_no : 9825864258
     * recess_timings_from : 00:00:00
     * recess_timings_to : 00:00:00
     * goods_type : NA
     * goods_weight_range : 0
     * customer_id : 1668
     * primary_location : 1
     */

    @PrimaryKey
    @SerializedName("shipping_location_id")
    private String shippingLocationId;
    @SerializedName("shipping_name")
    private String shippingName;
    @SerializedName("shipping_address")
    private String shippingAddress;
    @SerializedName("shipping_address2")
    private String shippingAddress2;
    @SerializedName("city_id")
    private String cityId;
    @SerializedName("city_name")
    private String cityName;
    @SerializedName("area_name")
    private String areaName;
    @SerializedName("area_id")
    private String areaId;
    @SerializedName("cp_name")
    private String cpName;
    @SerializedName("cp_contact_no")
    private String cpContactNo;
    @SerializedName("recess_timings_from")
    private String recessTimingsFrom;
    @SerializedName("recess_timings_to")
    private String recessTimingsTo;
    @SerializedName("goods_type")
    private String goodsType;
    @SerializedName("goods_weight_range")
    private String goodsWeightRange;
    @SerializedName("customer_id")
    private String customerId;
    @SerializedName("primary_location")
    private String primaryLocation;
    /**
     * gujarati_address :
     */

    @SerializedName("gujarati_address")
    private String gujaratiAddress;

    private boolean returnGujaratiAddress;

    public void setReturnGujaratiAddress(boolean returnGujaratiAddress)
    {
        this.returnGujaratiAddress = returnGujaratiAddress;
    }

    public String getShippingLocationId()
    {
        return shippingLocationId;
    }

    public void setShippingLocationId(String shippingLocationId)
    {
        this.shippingLocationId = shippingLocationId;
    }

    public String getShippingName()
    {
        return shippingName;
    }

    public void setShippingName(String shippingName)
    {
        this.shippingName = shippingName;
    }

    public String getShippingAddress()
    {
        return shippingAddress + ", " + getAreaName() + ", " + getCityName();
    }

    public void setShippingAddress(String shippingAddress)
    {
        this.shippingAddress = shippingAddress;
    }

    public String getCompanyName()
    {
        return shippingName;
    }

    public String getShippingAddress2()
    {
        return shippingAddress2;
    }

    public void setShippingAddress2(String shippingAddress2)
    {
        this.shippingAddress2 = shippingAddress2;
    }

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

    public String getAreaName()
    {
        return areaName;
    }

    public void setAreaName(String areaName)
    {
        this.areaName = areaName;
    }

    public String getAreaId()
    {
        return areaId;
    }

    public void setAreaId(String areaId)
    {
        this.areaId = areaId;
    }

    public String getCpName()
    {
        return cpName;
    }

    public void setCpName(String cpName)
    {
        this.cpName = cpName;
    }

    public String getCpContactNo()
    {
        return cpContactNo;
    }

    public void setCpContactNo(String cpContactNo)
    {
        this.cpContactNo = cpContactNo;
    }

    public String getRecessTimingsFrom()
    {
        return recessTimingsFrom;
    }

    public void setRecessTimingsFrom(String recessTimingsFrom)
    {
        this.recessTimingsFrom = recessTimingsFrom;
    }

    public String getRecessTimingsTo()
    {
        return recessTimingsTo;
    }

    public void setRecessTimingsTo(String recessTimingsTo)
    {
        this.recessTimingsTo = recessTimingsTo;
    }

    public String getGoodsType()
    {
        return goodsType;
    }

    public void setGoodsType(String goodsType)
    {
        this.goodsType = goodsType;
    }

    public String getGoodsWeightRange()
    {
        return goodsWeightRange;
    }

    public void setGoodsWeightRange(String goodsWeightRange)
    {
        this.goodsWeightRange = goodsWeightRange;
    }

    public String getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId(String customerId)
    {
        this.customerId = customerId;
    }

    public String getPrimaryLocation()
    {
        return primaryLocation;
    }

    public void setPrimaryLocation(String primaryLocation)
    {
        this.primaryLocation = primaryLocation;
    }

    @Override
    public int getId()
    {
        return Integer.valueOf(getShippingLocationId());
    }

    @Override
    public String getLabel()
    {
        if (returnGujaratiAddress)
        {
            return getGujaratiAddress();
        }
        return getShippingAddress();
    }

    public String getComparisionAddress()
    {
        return getShippingAddress() + " " + getShippingName();
    }

    public String getGujaratiAddress()
    {
        try
        {
            if (gujaratiAddress != null)
            {
                if (!gujaratiAddress.isEmpty())
                {
                    // Convert from Unicode to UTF-8
                    String string = gujaratiAddress;
                    byte[] utf8 = string.getBytes("UTF-8");

                    // Convert from UTF-8 to Unicode
                    return new String(utf8, "UTF-8");
                } else
                {
                    return getShippingAddress();
                }
            } else
            {
                return getShippingAddress();
            }
        } catch (UnsupportedEncodingException e)
        {
            return getShippingAddress();
        }
    }

    public void setGujaratiAddress(String gujaratiAddress)
    {
        this.gujaratiAddress = gujaratiAddress;
    }
}

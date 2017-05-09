package apimodels;

import com.google.gson.annotations.SerializedName;

/**
 * Created by rutvik on 5/9/2017 at 8:09 PM.
 */

public class TripBreakUpDetails
{


    /**
     * 0 : Trip Charges
     * item_name : Trip Charges
     * 1 : Soni ani chali - Na
     * item_desc : Soni ani chali - Na
     * 2 : 0
     * discount : 0
     * 3 : 1300
     * amount : 1300
     * 4 : 1300
     * net_amount : 1300
     * 5 : null
     * tax_group_name : null
     * 6 : null
     * tax_amount : null
     */

    @SerializedName("item_name")
    private String itemName;
    @SerializedName("item_desc")
    private String itemDesc;
    @SerializedName("discount")
    private String discount;
    @SerializedName("amount")
    private String amount;
    @SerializedName("net_amount")
    private String netAmount;

    public String getItemName()
    {
        return itemName;
    }

    public void setItemName(String itemName)
    {
        this.itemName = itemName;
    }

    public String getItemDesc()
    {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc)
    {
        this.itemDesc = itemDesc;
    }

    public String getDiscount()
    {
        return discount;
    }

    public void setDiscount(String discount)
    {
        this.discount = discount;
    }

    public String getAmount()
    {
        return amount;
    }

    public void setAmount(String amount)
    {
        this.amount = amount;
    }

    public String getNetAmount()
    {
        return netAmount;
    }

    public void setNetAmount(String netAmount)
    {
        this.netAmount = netAmount;
    }
}

package apimodels;

import com.google.gson.annotations.SerializedName;

/**
 * Created by rutvik on 6/17/2017 at 4:23 PM.
 */

public class CountryCodes
{


    /**
     * name : Israel
     * dial_code : +972
     * code : IL
     */

    @SerializedName("name")
    private String name;
    @SerializedName("dial_code")
    private String dialCode;
    @SerializedName("code")
    private String code;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDialCode()
    {
        return dialCode;
    }

    public void setDialCode(String dialCode)
    {
        this.dialCode = dialCode;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }
}

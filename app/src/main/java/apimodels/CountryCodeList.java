package apimodels;

import java.util.List;

/**
 * Created by rutvik on 6/17/2017 at 4:45 PM.
 */

public class CountryCodeList
{
    private List<CountryCodes> countryCodesList;

    public List<CountryCodes> getCountryCodesList()
    {
        return countryCodesList;
    }

    public void setCountryCodesList(List<CountryCodes> countryCodesList)
    {
        this.countryCodesList = countryCodesList;
    }
}

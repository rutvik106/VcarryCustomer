package io.fusionbit.vcarrycustomer;

/**
 * Created by rutvik on 11/20/2016 at 4:22 PM.
 */

public class Constants
{

    //public static final String API_BASE_URL = "";
    public static final String API_BASE_URL = "http://tapandtype.com/vcarry/webservice/";

    public static final int CHANGE_LANGUAGE = 123;
    public static final String WAS_LANGUAGE_CHANGED = "WAS_LANGUAGE_CHANGED";

    public static final String CUSTOMER_ID = "0ce7141e06cb2bae1f1cbff8aded62d5";


    public static final String PACKAGE_NAME =
            "com.rutvik.trails";
    public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
    public static final String RESULT_DATA_KEY = PACKAGE_NAME + ".RESULT_DATA_KEY";
    public static final String RESULT_ADDRESS = PACKAGE_NAME + ".RESULT_ADDRESS";
    public static final String LOCATION_LATITUDE_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_LATITUDE_DATA_EXTRA";
    public static final String LOCATION_LONGITUDE_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_LONGITUDE_DATA_EXTRA";
    public static final String LOCATION_NAME_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_NAME_DATA_EXTRA";
    public static final String FETCH_TYPE_EXTRA = PACKAGE_NAME + ".FETCH_TYPE_EXTRA";

    public static final int SELECT_START_LOCATION_ACTIVITY = 111;
    public static final int SELECT_DESTINATION_LOCATION_ACTIVITY = 222;

    public static final int SUCCESS_RESULT = 0;
    public static final int FAILURE_RESULT = 1;

    public static final int USE_ADDRESS_NAME = 1;
    public static final int USE_ADDRESS_LOCATION = 2;

    public static final String REALM_DATABASE_NAME = "vcarrycustomerrealmdb";


}

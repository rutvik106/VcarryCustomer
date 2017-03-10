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

    public static final String FCM_INSTANCE_ID = "a9ccaffd39b6bbcf445a914d4988778e";

    public static final String USER_ACTIVITIES = "USER_ACTIVITIES";

    public static final String BOOKED_TRIP_ID = "BOOKED_TRIP_ID";

    public static final int TRIP_CONFIRMED = 1;
    public static final int DRIVER_ALLOCATED = 2;

    public static final String TRIP_STATUS_NEW = "1";
    public static final String TRIP_STATUS_DRIVER_ALLOCATED = "2";
    public static final String TRIP_STATUS_LOADING = "3";
    public static final String TRIP_STATUS_TRIP_STARTED = "4";
    public static final String TRIP_STATUS_UNLOADING = "5";
    public static final String TRIP_STATUS_FINISHED = "6";
    public static final String TRIP_STATUS_CANCELLED_BY_DRIVER = "7";
    public static final String TRIP_STATUS_CANCELLED_BY_CUSTOMER = "8";

    public static final String ACCOUNT_TRIP_TYPE = "ACCOUNT_TRIP_TYPE";

    public static final String PARCELABLE_TRIP_LIST = "PARCELABLE_TRIP_LIST";
    public static final String DAY = "DAY";
    public static final String MONTH = "MONTH";
    public static final String YEAR = "YEAR";
    public static final String HOUR = "HOUR";
    public static final String MINUTE = "MINUTE";
    public static final String IS_PM = "IS_PM";
    public static final String IS_SCHEDULING_TRIP = "IS_SCHEDULING_TRIP";
    public static final String SCHEDULE_DETAILS = "SCHEDULE_DETAILS";

    public static final class NotificationType
    {
        public static final String SIMPLE = "simple";
        public static final String TRIP_CONFIRMATION = "trip_confirmation";
        public static final String DRIVER_ALLOCATED = "driver_allocated";
        public static final String GET_DRIVER_LOCATION = "get_driver_location";
        public static final String DRIVER_CURRENT_LOCATION = "driver_current_location";
    }

    public static class AccountTripType
    {
        public static final String TODAY = "TODAY";
        public static final String THIS_MONTH = "THIS_MONTH";
        public static final String TOTAL = "TOTAL";
    }

}

package apimodels;

import com.google.gson.annotations.SerializedName;

/**
 * Created by rutvik on 5/9/2017 at 6:49 PM.
 */

public class AccountSummaryNew
{


    /**
     * today : {"payable":0,"paid":0,"no_of_trips":null}
     * this_month : {"payable":835,"paid":6554,"no_of_trips":null}
     * all_time : {"payable":13890,"paid":13355,"no_of_trips":null}
     */

    @SerializedName("today")
    private TodayBean today = new TodayBean();
    @SerializedName("this_month")
    private ThisMonthBean thisMonth = new ThisMonthBean();
    @SerializedName("all_time")
    private AllTimeBean allTime = new AllTimeBean();

    public TodayBean getToday()
    {
        return today;
    }

    public void setToday(TodayBean today)
    {
        this.today = today;
    }

    public ThisMonthBean getThisMonth()
    {
        return thisMonth;
    }

    public void setThisMonth(ThisMonthBean thisMonth)
    {
        this.thisMonth = thisMonth;
    }

    public AllTimeBean getAllTime()
    {
        return allTime;
    }

    public void setAllTime(AllTimeBean allTime)
    {
        this.allTime = allTime;
    }

    public static class TodayBean
    {
        /**
         * payable : 0
         * paid : 0
         * no_of_trips : null
         */

        @SerializedName("payable")
        private double payable = 0;
        @SerializedName("paid")
        private double paid = 0;
        @SerializedName("no_of_trips")
        private Object noOfTrips;

        public double getPayable()
        {
            return payable;
        }

        public void setPayable(double payable)
        {
            this.payable = payable;
        }

        public double getPaid()
        {
            return paid;
        }

        public void setPaid(double paid)
        {
            this.paid = paid;
        }

        public Object getNoOfTrips()
        {
            return noOfTrips;
        }

        public void setNoOfTrips(Object noOfTrips)
        {
            this.noOfTrips = noOfTrips;
        }
    }

    public static class ThisMonthBean
    {
        /**
         * payable : 835
         * paid : 6554
         * no_of_trips : null
         */

        @SerializedName("payable")
        private double payable = 0;
        @SerializedName("paid")
        private double paid = 0;
        @SerializedName("no_of_trips")
        private Object noOfTrips;

        public double getPayable()
        {
            return payable;
        }

        public void setPayable(double payable)
        {
            this.payable = payable;
        }

        public double getPaid()
        {
            return paid;
        }

        public void setPaid(double paid)
        {
            this.paid = paid;
        }

        public Object getNoOfTrips()
        {
            return noOfTrips;
        }

        public void setNoOfTrips(Object noOfTrips)
        {
            this.noOfTrips = noOfTrips;
        }
    }

    public static class AllTimeBean
    {
        /**
         * payable : 13890
         * paid : 13355
         * no_of_trips : null
         */

        @SerializedName("payable")
        private double payable = 0;
        @SerializedName("paid")
        private double paid = 0;
        @SerializedName("no_of_trips")
        private Object noOfTrips;

        public double getPayable()
        {
            return payable;
        }

        public void setPayable(double payable)
        {
            this.payable = payable;
        }

        public double getPaid()
        {
            return paid;
        }

        public void setPaid(double paid)
        {
            this.paid = paid;
        }

        public Object getNoOfTrips()
        {
            return noOfTrips;
        }

        public void setNoOfTrips(Object noOfTrips)
        {
            this.noOfTrips = noOfTrips;
        }
    }
}

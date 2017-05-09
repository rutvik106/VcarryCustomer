package apimodels;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import io.fusionbit.vcarrycustomer.Constants;


/**
 * Created by rutvik on 12/24/2016 at 9:11 AM.
 */

public class AccountSummary
{


    private final List<TripByCustomerId> tripToday = new ArrayList<>();
    private final List<TripByCustomerId> tripThisMonth = new ArrayList<>();
    private final List<TripByCustomerId> totalTrips = new ArrayList<>();
    boolean busyLoading = true;
    /**
     * received : 300
     * receivable : 320
     */

    @SerializedName("paid")
    private float received = 0;
    @SerializedName("payable")
    private float receivable = 0;
    private float receivedToday = 0, receivableToday = 0, receivedThisMonth = 0, receivableThisMonth = 0,
            totalReceived = 0, totalReceivable = 0;
    private AccountSummaryNew accountSummaryNew = new AccountSummaryNew();

    public AccountSummaryNew getAccountSummaryNew()
    {
        return accountSummaryNew;
    }

    public void setAccountSummaryNew(AccountSummaryNew accountSummaryNew)
    {
        this.accountSummaryNew = accountSummaryNew;
    }

    public boolean isBusyLoading()
    {
        return busyLoading;
    }

    public void setBusyLoading(boolean busyLoading)
    {
        this.busyLoading = busyLoading;
    }

    public void clearData()
    {
        tripToday.clear();
        tripThisMonth.clear();
        totalTrips.clear();
        receivable = 0;
        received = 0;
        receivedToday = 0;
        receivableToday = 0;
        receivedThisMonth = 0;
        receivableThisMonth = 0;
        totalReceived = 0;
        totalReceivable = 0;
    }

    public List<TripByCustomerId> getTripToday()
    {
        return tripToday;
    }

    public List<TripByCustomerId> getTripThisMonth()
    {
        return tripThisMonth;
    }

    public List<TripByCustomerId> getTotalTrips()
    {
        return totalTrips;
    }

    public float getReceived()
    {
        return received;
    }

    public void setReceived(float received)
    {
        this.received = received;
    }

    public float getReceivable()
    {
        return receivable;
    }

    public void setReceivable(float receivable)
    {
        this.receivable = receivable;
    }

    public float getReceivedToday()
    {
        return receivedToday;
    }

    public void setReceivedToday(float receivedToday)
    {
        this.receivedToday = receivedToday;
    }

    public float getReceivableToday()
    {
        return receivableToday;
    }

    public void setReceivableToday(float receivableToday)
    {
        this.receivableToday = receivableToday;
    }

    public float getReceivedThisMonth()
    {
        return receivedThisMonth;
    }

    public void setReceivedThisMonth(float receivedThisMonth)
    {
        this.receivedThisMonth = receivedThisMonth;
    }

    public float getReceivableThisMonth()
    {
        return receivableThisMonth;
    }

    public void setReceivableThisMonth(float receivableThisMonth)
    {
        this.receivableThisMonth = receivableThisMonth;
    }

    public float getTotalReceived()
    {
        return totalReceived;
    }

    public void setTotalReceived(float totalReceived)
    {
        this.totalReceived = totalReceived;
    }

    public float getTotalReceivable()
    {
        return totalReceivable;
    }

    public void setTotalReceivable(float totalReceivable)
    {
        this.totalReceivable = totalReceivable;
    }


    //TOTAL
    public int getTotalIncompleteTrips()
    {
        int count = 0;
        if (totalTrips.size() > 0)
        {
            for (int i = 0; i < totalTrips.size(); i++)
            {
                if (totalTrips.get(i).getTripStatus().equals(Constants.TRIP_STATUS_CANCELLED_BY_CUSTOMER) ||
                        totalTrips.get(i).getTripStatus().equals(Constants.TRIP_STATUS_CANCELLED_BY_DRIVER))
                {
                    count++;
                }
            }
        }

        return count;
    }

    public int getTotalCompletedTrips()
    {
        int count = 0;
        if (totalTrips.size() > 0)
        {
            for (int i = 0; i < totalTrips.size(); i++)
            {
                if (totalTrips.get(i).getTripStatus().equals(Constants.TRIP_STATUS_FINISHED))
                {
                    count++;
                }
            }
        }

        return count;
    }


    //TODAY
    public int getTodayIncompleteTrips()
    {
        int count = 0;
        if (tripToday.size() > 0)
        {
            for (int i = 0; i < tripToday.size(); i++)
            {
                if (tripToday.get(i).getTripStatus().equals(Constants.TRIP_STATUS_CANCELLED_BY_CUSTOMER) ||
                        tripToday.get(i).getTripStatus().equals(Constants.TRIP_STATUS_CANCELLED_BY_DRIVER))
                {
                    count++;
                }
            }
        }

        return count;
    }

    public int getTodayCompletedTrips()
    {
        int count = 0;
        if (tripToday.size() > 0)
        {
            for (int i = 0; i < tripToday.size(); i++)
            {
                if (tripToday.get(i).getTripStatus().equals(Constants.TRIP_STATUS_FINISHED))
                {
                    count++;
                }
            }
        }

        return count;
    }


    //THIS MONTH
    public int getThisMonthIncompleteTrips()
    {
        int count = 0;
        if (tripThisMonth.size() > 0)
        {
            for (int i = 0; i < tripThisMonth.size(); i++)
            {
                if (tripThisMonth.get(i).getTripStatus().equals(Constants.TRIP_STATUS_CANCELLED_BY_CUSTOMER) ||
                        tripThisMonth.get(i).getTripStatus().equals(Constants.TRIP_STATUS_CANCELLED_BY_DRIVER))
                {
                    count++;
                }
            }
        }

        return count;
    }

    public int getThisMonthCompletedTrips()
    {
        int count = 0;
        if (tripThisMonth.size() > 0)
        {
            for (int i = 0; i < tripThisMonth.size(); i++)
            {
                if (tripThisMonth.get(i).getTripStatus().equals(Constants.TRIP_STATUS_FINISHED))
                {
                    count++;
                }
            }
        }

        return count;
    }

}

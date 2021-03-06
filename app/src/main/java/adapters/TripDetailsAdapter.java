package adapters;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import apimodels.TripByCustomerId;
import io.fusionbit.vcarrycustomer.Constants;
import viewholder.VHSingleTripDetails;

/**
 * Created by rutvik on 1/26/2017 at 1:58 PM.
 */

public class TripDetailsAdapter extends RecyclerView.Adapter implements VHSingleTripDetails.CountDownTimerReferenceHolder
{

    private final List<TripByCustomerId> bookedTripList;

    private final Context context;

    private VHSingleTripDetails.OnDriverPhotoClickListener onDriverPhotoClickListener;

    private ArrayList<CountDownTimer> countDownTimers;

    public TripDetailsAdapter(Context context, VHSingleTripDetails.OnDriverPhotoClickListener onDriverPhotoClickListener)
    {
        this.context = context;
        this.onDriverPhotoClickListener = onDriverPhotoClickListener;
        bookedTripList = new ArrayList<>();
        countDownTimers = new ArrayList<>();
    }

    public void addBookedTrip(TripByCustomerId bookedTrip)
    {
        bookedTripList.add(0, bookedTrip);
        Collections.sort(bookedTripList);
        Collections.sort(bookedTripList, new Comparator<TripByCustomerId>()
        {
            @Override
            public int compare(TripByCustomerId tripsByDriverMail, TripByCustomerId t1)
            {
                if (t1.getTripDatetime() != null && tripsByDriverMail.getTripDatetime() != null)
                {
                    return t1.getTripDatetime().compareTo(tripsByDriverMail.getTripDatetime());
                }
                return 0;
            }
        });
        notifyItemInserted(bookedTripList.size());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return VHSingleTripDetails.create(context, parent, onDriverPhotoClickListener, this);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        VHSingleTripDetails.bind((VHSingleTripDetails) holder,
                bookedTripList.get(position));
    }

    @Override
    public int getItemCount()
    {
        return bookedTripList.size();
    }

    public void clear()
    {
        final List<TripByCustomerId> toBeRemoved = new ArrayList<>();
        for (TripByCustomerId trip : bookedTripList)
        {
            if (!trip.getTripStatus().equals(Constants.TRIP_STATUS_PENDING))
            {
                toBeRemoved.add(trip);
            }
        }
        bookedTripList.removeAll(toBeRemoved);
        notifyDataSetChanged();
    }

    public void clearPendingTrips()
    {
        final List<TripByCustomerId> toBeRemoved = new ArrayList<>();
        for (TripByCustomerId trip : bookedTripList)
        {
            if (trip.getTripStatus().equals(Constants.TRIP_STATUS_PENDING))
            {
                toBeRemoved.add(trip);
            }
        }
        bookedTripList.removeAll(toBeRemoved);
        notifyDataSetChanged();
    }

    public void clearAll()
    {
        bookedTripList.clear();
        notifyDataSetChanged();
    }

    public void stopTimers()
    {
        if (countDownTimers != null)
        {
            if (countDownTimers.size() > 0)
            {
                for (CountDownTimer c : countDownTimers)
                {
                    c.cancel();
                    c = null;
                }
                countDownTimers.clear();
                countDownTimers = null;
            }
        }
    }

    @Override
    public void onCountDownCreated(CountDownTimer cdt)
    {
        if (countDownTimers != null)
        {
            countDownTimers.add(cdt);
        }
    }
}

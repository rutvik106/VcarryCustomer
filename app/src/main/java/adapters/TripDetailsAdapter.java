package adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import apimodels.TripByCustomerId;
import io.fusionbit.vcarrycustomer.Constants;
import viewholder.VHSingleTripDetails;

/**
 * Created by rutvik on 1/26/2017 at 1:58 PM.
 */

public class TripDetailsAdapter extends RecyclerView.Adapter
{

    private final List<TripByCustomerId> bookedTripList;

    private final Context context;

    public TripDetailsAdapter(Context context)
    {
        this.context = context;
        bookedTripList = new ArrayList<>();
    }

    public void addBookedTrip(TripByCustomerId bookedTrip)
    {
        bookedTripList.add(0, bookedTrip);
        Collections.sort(bookedTripList);
        notifyItemInserted(bookedTripList.size());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return VHSingleTripDetails.create(context, parent);
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
}

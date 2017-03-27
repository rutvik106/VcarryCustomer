package adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import models.BookedTrip;
import viewholder.VHSingleTripDetails;

/**
 * Created by rutvik on 1/26/2017 at 1:58 PM.
 */

public class TripDetailsAdapter extends RecyclerView.Adapter
{

    private final List<BookedTrip> bookedTripList;

    private final Context context;

    public TripDetailsAdapter(Context context)
    {
        this.context = context;
        bookedTripList = new ArrayList<>();
    }

    public void addBookedTrip(BookedTrip bookedTrip)
    {
        bookedTripList.add(0, bookedTrip);
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
        bookedTripList.clear();
        notifyDataSetChanged();
    }
}

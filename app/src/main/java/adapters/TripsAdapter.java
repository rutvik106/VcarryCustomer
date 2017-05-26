package adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import apimodels.TripByCustomerId;
import io.fusionbit.vcarrycustomer.Utils;
import viewholder.VHSingleTripDetails;

/**
 * Created by rutvik on 11/27/2016 at 4:03 PM.
 */

public class TripsAdapter extends RecyclerView.Adapter
{

    final List<TripByCustomerId> tripsByDriverMailList;
    //final List<Integer> tripIdList;

    final Context context;

    public TripsAdapter(final Context context)
    {
        this.context = context;
        //tripsByDriverMailList = new LinkedHashMap<>();
        tripsByDriverMailList = new ArrayList<>();
    }


    public void addTrip(TripByCustomerId tripsByDriverMail)
    {
        if (!tripsByDriverMailList.contains(tripsByDriverMail))
        {
            tripsByDriverMailList.add(tripsByDriverMail);
            //tripsByDriverMailList.put(Integer.valueOf(tripsByDriverMail.getCustomerTripId()), tripsByDriverMail);
            Collections.sort(tripsByDriverMailList);
            Collections.sort(tripsByDriverMailList, new Comparator<TripByCustomerId>()
            {
                @Override
                public int compare(TripByCustomerId tripsByDriverMail, TripByCustomerId t1)
                {
                    Date date1 = Utils.convertToDate(tripsByDriverMail.getTripDatetime());
                    Date date2 = Utils.convertToDate(t1.getTripDatetime());
                    return date2.compareTo(date1);
                }
            });
            notifyItemInserted(tripsByDriverMailList.size());
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return VHSingleTripDetails.create(context, parent, null, null);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        VHSingleTripDetails.bind((VHSingleTripDetails) holder, tripsByDriverMailList.get(position));
    }

    @Override
    public int getItemCount()
    {
        return tripsByDriverMailList.size();
    }
}

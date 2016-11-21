package fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.fusionbit.vcarrycustomer.ActivityBookTrip;
import io.fusionbit.vcarrycustomer.R;

/**
 * Created by rutvik on 11/17/2016 at 10:49 PM.
 */

public class FragmentTrips extends Fragment
{

    Context context;

    CardView cvBookTrip;

    public static FragmentTrips newInstance(int index, Context context)
    {
        FragmentTrips fragmentTrips = new FragmentTrips();
        fragmentTrips.context = context;
        Bundle b = new Bundle();
        b.putInt("index", index);
        fragmentTrips.setArguments(b);
        return fragmentTrips;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_trips, container, false);

        cvBookTrip = (CardView) view.findViewById(R.id.cv_bookTrip);

        cvBookTrip.setOnClickListener(new BookTrip());

        return view;
    }


    class BookTrip implements View.OnClickListener
    {
        @Override
        public void onClick(View view)
        {

            startActivity(new Intent(context, ActivityBookTrip.class));

        }
    }


}

package fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.fusionbit.vcarrycustomer.ActivityBookTrip;
import io.fusionbit.vcarrycustomer.R;

/**
 * Created by rutvik on 11/17/2016 at 10:49 PM.
 */

public class FragmentTrips extends Fragment
{

    @BindView(R.id.ll_bookTrip)
    LinearLayout llBookTrip;

    public static FragmentTrips newInstance(int index)
    {
        FragmentTrips fragmentTrips = new FragmentTrips();
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

        ButterKnife.bind(this, view);

        llBookTrip.setOnClickListener(new BookTrip());

        return view;
    }


    class BookTrip implements View.OnClickListener
    {
        @Override
        public void onClick(View view)
        {

            startActivity(new Intent(getActivity(), ActivityBookTrip.class));

        }
    }


}

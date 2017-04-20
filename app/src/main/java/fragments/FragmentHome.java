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
import dialogs.DateTimePickerDialog;
import io.fusionbit.vcarrycustomer.ActivityBookTrip;
import io.fusionbit.vcarrycustomer.ActivityOnGoingTrips;
import io.fusionbit.vcarrycustomer.Constants;
import io.fusionbit.vcarrycustomer.R;

/**
 * Created by rutvik on 11/17/2016 at 10:49 PM.
 */

public class FragmentHome extends Fragment implements DateTimePickerDialog.OnDateTimeSetListener
{

    @BindView(R.id.ll_bookTrip)
    LinearLayout llBookTrip;

    @BindView(R.id.ll_scheduleTrip)
    LinearLayout llScheduleTrip;

    DateTimePickerDialog dateTimePickerDialog;
    @BindView(R.id.ll_ongoingTrip)
    LinearLayout llOngoingTrip;
    @BindView(R.id.ll_tripSummary)
    LinearLayout llTripSummary;

    public static FragmentHome newInstance(int index)
    {
        FragmentHome fragmentHome = new FragmentHome();
        Bundle b = new Bundle();
        b.putInt("index", index);
        fragmentHome.setArguments(b);
        return fragmentHome;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ButterKnife.bind(this, view);

        llBookTrip.setOnClickListener(new BookTrip());

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        llScheduleTrip.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dateTimePickerDialog = new DateTimePickerDialog(getActivity(), "SELECT DATE & TIME",
                        FragmentHome.this);
                dateTimePickerDialog.show();
            }
        });

        llOngoingTrip.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ActivityOnGoingTrips.start(getActivity());
            }
        });
    }

    @Override
    public void onDateTimeSet(DateTimePickerDialog dialog, int dayOfMonth, int month, int year, int hourIn24, int minute)
    {
        dialog.dismiss();

        Intent i = new Intent(getActivity(), ActivityBookTrip.class);

        Bundle bundle = new Bundle();
        bundle.putInt(Constants.DAY, dayOfMonth);
        bundle.putInt(Constants.MONTH, month);
        bundle.putInt(Constants.YEAR, year);
        bundle.putInt(Constants.HOUR, hourIn24);
        bundle.putInt(Constants.MINUTE, minute);
        if (hourIn24 >= 12)
        {
            bundle.putBoolean(Constants.IS_PM, true);
        } else
        {
            bundle.putBoolean(Constants.IS_PM, false);
        }


        i.putExtra(Constants.IS_SCHEDULING_TRIP, true);
        i.putExtra(Constants.SCHEDULE_DETAILS, bundle);

        startActivity(i);
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

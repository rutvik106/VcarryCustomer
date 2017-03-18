package viewholder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import apimodels.TripByCustomerId;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.fusionbit.vcarrycustomer.ActivityDriverLocation;
import io.fusionbit.vcarrycustomer.Constants;
import io.fusionbit.vcarrycustomer.R;
import models.BookedTrip;

/**
 * Created by rutvik on 1/26/2017 at 10:44 AM.
 */

public class VHSingleTripDetails extends RecyclerView.ViewHolder
{
    final Context context;

    @BindView(R.id.tv_singleTripCost)
    TextView tvSingleTripCost;

    @BindView(R.id.tv_singleTripStatus)
    TextView tvSingleTripStatus;

    @BindView(R.id.tv_singleTripFrom)
    TextView tvSingleTripFrom;

    @BindView(R.id.tv_singleTripTo)
    TextView tvSingleTripTo;

    @BindView(R.id.tv_singleTripVehicle)
    TextView tvSingleTripVehicle;

    @BindView(R.id.tv_singleTripDriverName)
    TextView tvSingleTripDriverName;

    @BindView(R.id.ib_singleTripCallDriver)
    ImageButton ibSingleTripCallDriver;

    @BindView(R.id.ib_singleTripDriverLocation)
    ImageButton ibSingleTripDriverLocation;

    @BindView(R.id.ll_singleTripDriverDetailsContainer)
    LinearLayout llSingleTripDriverDetailsContainer;

    BookedTrip model;

    TripByCustomerId tripDetails;

    public VHSingleTripDetails(final Context context, View itemView)
    {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.context = context;

        ibSingleTripDriverLocation.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(context, ActivityDriverLocation.class);
                i.putExtra(Constants.BOOKED_TRIP_ID, model.getTripId());
                context.startActivity(i);
            }
        });

        ibSingleTripCallDriver.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(context, "Feature Coming Soon...", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public static VHSingleTripDetails create(final Context context, final ViewGroup parent)
    {
        return new VHSingleTripDetails(context, LayoutInflater.from(context)
                .inflate(R.layout.single_trip_row_item, parent, false));
    }

    public static void bind(final VHSingleTripDetails vh, BookedTrip model)
    {
        vh.model = model;
        vh.tvSingleTripFrom.setText(model.getTripFrom());
        vh.tvSingleTripTo.setText(model.getTripTo());
        vh.tvSingleTripVehicle.setText(model.getTripVehicle());
        vh.tvSingleTripCost.setText(vh.context.getResources().getString(R.string.rs) + " " + model.getTripCost());

        if (model.getDriverTripId() != null)
        {
            vh.llSingleTripDriverDetailsContainer.setVisibility(View.VISIBLE);
            vh.tvSingleTripDriverName.setText(model.getDriverName());
        } else
        {
            vh.tvSingleTripDriverName.setText("");
            vh.llSingleTripDriverDetailsContainer.setVisibility(View.GONE);
        }

        switch (model.getStatus())
        {
            case Constants.TRIP_CONFIRMED:
                vh.tvSingleTripStatus.setText(R.string.trip_confirmed);
                vh.tvSingleTripStatus.setTextColor(vh.context.getResources()
                        .getColor(android.R.color.holo_orange_light));
                break;

            case Constants.DRIVER_ALLOCATED:
                vh.tvSingleTripStatus.setText(R.string.driver_allocated);
                vh.tvSingleTripStatus.setTextColor(vh.context.getResources()
                        .getColor(android.R.color.holo_green_light));
                break;

            default:
                vh.tvSingleTripStatus.setText(R.string.pending_confirmation);
                vh.tvSingleTripStatus.setTextColor(vh.context.getResources()
                        .getColor(android.R.color.holo_red_light));
                break;
        }

    }

    public static void bind(final VHSingleTripDetails vh, TripByCustomerId model)
    {
        vh.tripDetails = model;
        vh.model = model.getBookedTrip();

        vh.tvSingleTripFrom.setText(model.getFromShippingLocation());
        vh.tvSingleTripTo.setText(model.getToShippingLocation());
        if (model.getBookedTrip() != null)
        {
            vh.tvSingleTripVehicle.setText(model.getBookedTrip().getTripVehicle());
            vh.tvSingleTripCost.setText(vh.context.getResources().getString(R.string.rs) +
                    " " + model.getBookedTrip().getTripCost());
        }


        if (model.getBookedTrip() != null)
        {
            if (model.getBookedTrip().getDriverTripId() != null)
            {
                vh.llSingleTripDriverDetailsContainer.setVisibility(View.VISIBLE);
                vh.tvSingleTripDriverName.setText(model.getBookedTrip().getDriverName());
            } else
            {
                vh.tvSingleTripDriverName.setText("");
                vh.llSingleTripDriverDetailsContainer.setVisibility(View.GONE);
            }
        }

        switch (model.getTripStatus())
        {
            case Constants.TRIP_STATUS_NEW:
                vh.tvSingleTripStatus.setText(R.string.trip_confirmed);
                vh.tvSingleTripStatus.setTextColor(vh.context.getResources()
                        .getColor(android.R.color.holo_orange_light));
                break;

            case Constants.TRIP_STATUS_DRIVER_ALLOCATED:
                vh.tvSingleTripStatus.setText(R.string.driver_allocated);
                vh.tvSingleTripStatus.setTextColor(vh.context.getResources()
                        .getColor(android.R.color.holo_green_light));
                break;

            case Constants.TRIP_STATUS_FINISHED:
                vh.tvSingleTripStatus.setText(R.string.driver_allocated);
                vh.tvSingleTripStatus.setTextColor(vh.context.getResources()
                        .getColor(android.R.color.black));
                break;

            case Constants.TRIP_STATUS_CANCELLED_BY_CUSTOMER:
                vh.tvSingleTripStatus.setText("Cancelled By You");
                vh.tvSingleTripStatus.setTextColor(vh.context.getResources()
                        .getColor(android.R.color.holo_red_light));
                break;

            case Constants.TRIP_STATUS_CANCELLED_BY_DRIVER:
                vh.tvSingleTripStatus.setText("Cancelled By Driver");
                vh.tvSingleTripStatus.setTextColor(vh.context.getResources()
                        .getColor(android.R.color.holo_red_light));
                break;

            case Constants.TRIP_STATUS_TRIP_STARTED:
                vh.tvSingleTripStatus.setText("Trip Started");
                vh.tvSingleTripStatus.setTextColor(vh.context.getResources()
                        .getColor(android.R.color.holo_green_light));
                break;

            default:
                vh.tvSingleTripStatus.setText(R.string.pending_confirmation);
                vh.tvSingleTripStatus.setTextColor(vh.context.getResources()
                        .getColor(android.R.color.holo_red_light));
                break;
        }
    }


}

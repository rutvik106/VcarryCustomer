package viewholder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

    @BindView(R.id.rl_tripRowItem)
    RelativeLayout rlTripRowItem;

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
                i.putExtra(Constants.BOOKED_TRIP_ID, model.getCustomerTripId());
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

        switch (model.getStatus() + "")
        {
            case Constants.TRIP_STATUS_NEW:
                vh.tvSingleTripStatus.setText(R.string.trip_confirmed);
                vh.llSingleTripDriverDetailsContainer.setVisibility(View.GONE);
                vh.tvSingleTripStatus.setTextColor(vh.context.getResources()
                        .getColor(android.R.color.holo_orange_light));
                vh.rlTripRowItem.setBackground(vh.context.getResources()
                        .getDrawable(R.drawable.trip_card_bg_orange));
                break;

            case Constants.TRIP_STATUS_DRIVER_ALLOCATED:
                vh.llSingleTripDriverDetailsContainer.setVisibility(View.VISIBLE);
                vh.tvSingleTripStatus.setText(R.string.driver_allocated);
                vh.tvSingleTripStatus.setTextColor(vh.context.getResources()
                        .getColor(android.R.color.holo_green_light));
                vh.rlTripRowItem.setBackground(vh.context.getResources()
                        .getDrawable(R.drawable.trip_card_bg_green));
                break;

            case Constants.TRIP_STATUS_FINISHED:
                vh.llSingleTripDriverDetailsContainer.setVisibility(View.GONE);
                vh.tvSingleTripStatus.setText(R.string.trip_finished);
                vh.tvSingleTripStatus.setTextColor(vh.context.getResources()
                        .getColor(android.R.color.black));
                vh.rlTripRowItem.setBackground(vh.context.getResources()
                        .getDrawable(R.drawable.trip_card_bg_black));
                break;

            case Constants.TRIP_STATUS_CANCELLED_BY_CUSTOMER:
                vh.llSingleTripDriverDetailsContainer.setVisibility(View.GONE);
                vh.tvSingleTripStatus.setText(R.string.cancelled_by_you);
                vh.tvSingleTripStatus.setTextColor(vh.context.getResources()
                        .getColor(android.R.color.holo_red_light));
                vh.rlTripRowItem.setBackground(vh.context.getResources()
                        .getDrawable(R.drawable.trip_card_bg_red));
                break;

            case Constants.TRIP_STATUS_CANCELLED_BY_DRIVER:
                vh.llSingleTripDriverDetailsContainer.setVisibility(View.GONE);
                vh.tvSingleTripStatus.setText(R.string.cancelled_by_motorist);
                vh.tvSingleTripStatus.setTextColor(vh.context.getResources()
                        .getColor(android.R.color.holo_red_light));
                vh.rlTripRowItem.setBackground(vh.context.getResources()
                        .getDrawable(R.drawable.trip_card_bg_red));
                break;

            case Constants.TRIP_STATUS_TRIP_STARTED:
                vh.llSingleTripDriverDetailsContainer.setVisibility(View.VISIBLE);
                vh.tvSingleTripStatus.setText(R.string.trip_started);
                vh.tvSingleTripStatus.setTextColor(vh.context.getResources()
                        .getColor(android.R.color.holo_green_light));
                vh.rlTripRowItem.setBackground(vh.context.getResources()
                        .getDrawable(R.drawable.trip_card_bg_green));
                break;

            default:
                vh.llSingleTripDriverDetailsContainer.setVisibility(View.GONE);
                vh.tvSingleTripStatus.setText(R.string.pending_confirmation);
                vh.tvSingleTripStatus.setTextColor(vh.context.getResources()
                        .getColor(android.R.color.holo_red_light));
                vh.rlTripRowItem.setBackground(vh.context.getResources()
                        .getDrawable(R.drawable.trip_card_bg_red));
                break;
        }

    }

    public static void bind(final VHSingleTripDetails vh, TripByCustomerId model)
    {
        vh.tripDetails = model;
        vh.model = model.getBookedTrip();

        vh.tvSingleTripFrom.setText(model.getFromShippingLocation());
        vh.tvSingleTripTo.setText(model.getToShippingLocation());

        vh.tvSingleTripVehicle.setText(model.getVehicleType());
        vh.tvSingleTripCost.setText(vh.context.getResources().getString(R.string.rs) +
                " " + model.getFare());


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
                vh.llSingleTripDriverDetailsContainer.setVisibility(View.VISIBLE);
                vh.tvSingleTripStatus.setTextColor(vh.context.getResources()
                        .getColor(android.R.color.holo_orange_light));
                vh.rlTripRowItem.setBackground(vh.context.getResources()
                        .getDrawable(R.drawable.trip_card_bg_orange));
                break;

            case Constants.TRIP_STATUS_DRIVER_ALLOCATED:
                vh.llSingleTripDriverDetailsContainer.setVisibility(View.VISIBLE);
                vh.tvSingleTripStatus.setText(R.string.driver_allocated);
                vh.tvSingleTripStatus.setTextColor(vh.context.getResources()
                        .getColor(android.R.color.holo_green_light));
                vh.rlTripRowItem.setBackground(vh.context.getResources()
                        .getDrawable(R.drawable.trip_card_bg_green));
                break;

            case Constants.TRIP_STATUS_FINISHED:
                vh.llSingleTripDriverDetailsContainer.setVisibility(View.GONE);
                vh.tvSingleTripStatus.setText(R.string.trip_finished);
                vh.tvSingleTripStatus.setTextColor(vh.context.getResources()
                        .getColor(android.R.color.black));
                vh.rlTripRowItem.setBackground(vh.context.getResources()
                        .getDrawable(R.drawable.trip_card_bg_black));
                break;

            case Constants.TRIP_STATUS_CANCELLED_BY_CUSTOMER:
                vh.llSingleTripDriverDetailsContainer.setVisibility(View.GONE);
                vh.tvSingleTripStatus.setText(R.string.cancelled_by_you);
                vh.tvSingleTripStatus.setTextColor(vh.context.getResources()
                        .getColor(android.R.color.holo_red_light));
                vh.rlTripRowItem.setBackground(vh.context.getResources()
                        .getDrawable(R.drawable.trip_card_bg_red));
                break;

            case Constants.TRIP_STATUS_CANCELLED_BY_DRIVER:
                vh.llSingleTripDriverDetailsContainer.setVisibility(View.GONE);
                vh.tvSingleTripStatus.setText(R.string.cancelled_by_motorist);
                vh.tvSingleTripStatus.setTextColor(vh.context.getResources()
                        .getColor(android.R.color.holo_red_light));
                vh.rlTripRowItem.setBackground(vh.context.getResources()
                        .getDrawable(R.drawable.trip_card_bg_red));
                break;

            case Constants.TRIP_STATUS_TRIP_STARTED:
                vh.llSingleTripDriverDetailsContainer.setVisibility(View.VISIBLE);
                vh.tvSingleTripStatus.setText(R.string.trip_started);
                vh.tvSingleTripStatus.setTextColor(vh.context.getResources()
                        .getColor(android.R.color.holo_green_light));
                vh.rlTripRowItem.setBackground(vh.context.getResources()
                        .getDrawable(R.drawable.trip_card_bg_green));
                break;

            default:
                vh.llSingleTripDriverDetailsContainer.setVisibility(View.GONE);
                vh.tvSingleTripStatus.setText(R.string.pending_confirmation);
                vh.tvSingleTripStatus.setTextColor(vh.context.getResources()
                        .getColor(android.R.color.holo_red_light));
                vh.rlTripRowItem.setBackground(vh.context.getResources()
                        .getDrawable(R.drawable.trip_card_bg_red));
                break;
        }
    }


}

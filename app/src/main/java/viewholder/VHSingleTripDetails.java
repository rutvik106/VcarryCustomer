package viewholder;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import extra.Log;
import io.fusionbit.vcarrycustomer.ActivityDriverLocation;
import io.fusionbit.vcarrycustomer.ActivityTripDetails;
import io.fusionbit.vcarrycustomer.App;
import io.fusionbit.vcarrycustomer.Constants;
import io.fusionbit.vcarrycustomer.R;

/**
 * Created by rutvik on 1/26/2017 at 10:44 AM.
 */

public class VHSingleTripDetails extends RecyclerView.ViewHolder
{
    private static final String TAG = App.APP_TAG + VHSingleTripDetails.class.getSimpleName();

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

    TripByCustomerId tripDetails;
    @BindView(R.id.tv_tripNumber)
    TextView tvTripNumber;
    @BindView(R.id.ll_tripNumberContainer)
    LinearLayout llTripNumberContainer;

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
                i.putExtra(Constants.TRIP_ID, tripDetails.getTripId());
                context.startActivity(i);
            }
        });

        ibSingleTripCallDriver.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (tripDetails.getDriverNumber() != null)
                {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + tripDetails.getDriverNumber()));
                    context.startActivity(intent);
                } else
                {
                    Toast.makeText(context, "Driver contact number not found", Toast.LENGTH_SHORT).show();
                }
            }
        });

        rlTripRowItem.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                if (tripDetails.getStatus().equals(Constants.TRIP_STATUS_PENDING))
                {
                    Toast.makeText(context, R.string.trip_not_confirmed, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (tripDetails != null)
                {
                    Log.i(TAG, "tripDetails is not NULL");
                    Log.i(TAG, "tripDetails tripId is: " + tripDetails.getTripId());
                    ActivityTripDetails.start(context, tripDetails.getTripId());
                } else if (tripDetails != null)
                {
                    Log.i(TAG, "BookedTrip is not NULL");
                    Log.i(TAG, "BookedTrip tripId is: " + tripDetails.getTripId());
                    Log.i(TAG, "BookedTrip driverTripId is: " + tripDetails.getTripId());
                    Log.i(TAG, "BookedTrip customerTripId is: " + tripDetails.getCustomerTripId());
                    ActivityTripDetails.start(context, tripDetails.getTripId() != null ?
                            tripDetails.getTripId() : tripDetails.getTripId());
                }
            }
        });

    }

    public static VHSingleTripDetails create(final Context context, final ViewGroup parent)
    {
        return new VHSingleTripDetails(context, LayoutInflater.from(context)
                .inflate(R.layout.single_trip_row_item, parent, false));
    }

    /*public static void bind(final VHSingleTripDetails vh, BookedTrip model)
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

        if (model.getTripNumber() != null)
        {
            vh.llTripNumberContainer.setVisibility(View.VISIBLE);
            vh.tvTripNumber.setText(model.getTripNumber());
        } else
        {
            vh.llTripNumberContainer.setVisibility(View.GONE);
            vh.tvTripNumber.setText("");
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

    }*/

    public static void bind(final VHSingleTripDetails vh, TripByCustomerId model)
    {
        vh.tripDetails = model;

        if (model.getTripStatus().equals(Constants.TRIP_STATUS_PENDING))
        {
            vh.tvSingleTripFrom.setText(model.getBookedFromLocation());
            vh.tvSingleTripTo.setText(model.getBookedToLocation());
        } else
        {
            vh.tvSingleTripFrom.setText(model.getFromShippingLocation());
            vh.tvSingleTripTo.setText(model.getToShippingLocation());
        }

        vh.tvSingleTripVehicle.setText(model.getVehicleType());
        vh.tvSingleTripCost.setText(vh.context.getResources().getString(R.string.rs) +
                " " + model.getFare());


        if (model.getDriverName() != null)
        {
            if (!model.getDriverName().isEmpty())
            {
                vh.llSingleTripDriverDetailsContainer.setVisibility(View.VISIBLE);
                vh.tvSingleTripDriverName.setText(model.getDriverName());
            }
        }

        if (model.getTripNo() != null)
        {
            vh.llTripNumberContainer.setVisibility(View.VISIBLE);
            vh.tvTripNumber.setText(model.getTripNo());
        } else
        {
            vh.llTripNumberContainer.setVisibility(View.GONE);
            vh.tvTripNumber.setText("");
        }

        switch (model.getTripStatus())
        {
            case Constants.TRIP_STATUS_PENDING:
                vh.llSingleTripDriverDetailsContainer.setVisibility(View.GONE);
                vh.tvSingleTripStatus.setText(R.string.pending_confirmation);
                vh.tvSingleTripStatus.setTextColor(vh.context.getResources()
                        .getColor(android.R.color.holo_red_light));
                vh.rlTripRowItem.setBackground(vh.context.getResources()
                        .getDrawable(R.drawable.trip_card_bg_red));
                break;

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

            case Constants.TRIP_STATUS_LOADING:
                vh.llSingleTripDriverDetailsContainer.setVisibility(View.VISIBLE);
                vh.tvSingleTripStatus.setText(R.string.driver_allocated);
                vh.tvSingleTripStatus.setTextColor(vh.context.getResources()
                        .getColor(android.R.color.holo_green_light));
                vh.rlTripRowItem.setBackground(vh.context.getResources()
                        .getDrawable(R.drawable.trip_card_bg_green));
                break;

            case Constants.TRIP_STATUS_TRIP_STARTED:
                vh.llSingleTripDriverDetailsContainer.setVisibility(View.VISIBLE);
                vh.tvSingleTripStatus.setText(R.string.trip_started);
                vh.tvSingleTripStatus.setTextColor(vh.context.getResources()
                        .getColor(android.R.color.holo_green_light));
                vh.rlTripRowItem.setBackground(vh.context.getResources()
                        .getDrawable(R.drawable.trip_card_bg_green));
                break;

            case Constants.TRIP_STATUS_UNLOADING:
                vh.llSingleTripDriverDetailsContainer.setVisibility(View.VISIBLE);
                vh.tvSingleTripStatus.setText(R.string.trip_started);
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

            case Constants.TRIP_STATUS_CANCELLED_BY_DRIVER:
                vh.llSingleTripDriverDetailsContainer.setVisibility(View.GONE);
                vh.tvSingleTripStatus.setText(R.string.cancelled_by_motorist);
                vh.tvSingleTripStatus.setTextColor(vh.context.getResources()
                        .getColor(android.R.color.holo_red_light));
                vh.rlTripRowItem.setBackground(vh.context.getResources()
                        .getDrawable(R.drawable.trip_card_bg_red));
                break;

            case Constants.TRIP_STATUS_CANCELLED_BY_CUSTOMER:
                vh.llSingleTripDriverDetailsContainer.setVisibility(View.GONE);
                vh.tvSingleTripStatus.setText(R.string.cancelled_by_you);
                vh.tvSingleTripStatus.setTextColor(vh.context.getResources()
                        .getColor(android.R.color.holo_red_light));
                vh.rlTripRowItem.setBackground(vh.context.getResources()
                        .getDrawable(R.drawable.trip_card_bg_red));
                break;

            case Constants.TRIP_STATUS_CANCELLED_BY_VCARRY:
                vh.llSingleTripDriverDetailsContainer.setVisibility(View.GONE);
                vh.tvSingleTripStatus.setText(R.string.canceled_by_vcarry);
                vh.tvSingleTripStatus.setTextColor(vh.context.getResources()
                        .getColor(android.R.color.holo_red_light));
                vh.rlTripRowItem.setBackground(vh.context.getResources()
                        .getDrawable(R.drawable.trip_card_bg_red));
                break;


        }
    }


}

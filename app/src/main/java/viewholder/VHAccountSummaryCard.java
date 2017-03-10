package viewholder;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import java.util.ArrayList;

import apimodels.AccountSummary;
import extra.EasingConstants;
import io.fusionbit.vcarrycustomer.Constants;
import io.fusionbit.vcarrycustomer.R;

/**
 * Created by rutvik on 12/24/2016 at 9:09 AM.
 */

public class VHAccountSummaryCard extends RecyclerView.ViewHolder implements View.OnClickListener
{
    private final Context context;

    private TextView tvAccountPaidToday, tvAccountUnpaidToday,
            tvAccountPaidThisMonth, tvAccountUnpaidThisMonth, tvAccountPaidTotal,
            tvAccountUnpaidTotal, tvCompletedTripToday, tvCompletedTripThisMonth,
            tvCompletedTripTotal, tvIncompleteTripToday, tvIncompleteTripThisMonth,
            tvIncompleteTripTotal;

    private CardView cvAccountToday, cvAccountThisMonth, cvAccountTotal;

    private AccountSummary accountSummary;

    private VHAccountSummaryCard(Context context, View itemView)
    {
        super(itemView);
        this.context = context;

        tvAccountPaidToday = (TextView) itemView.findViewById(R.id.tv_accountPaidToday);
        tvAccountUnpaidToday = (TextView) itemView.findViewById(R.id.tv_accountUnpaidToday);
        tvAccountPaidThisMonth = (TextView) itemView.findViewById(R.id.tv_accountPaidThisMonth);
        tvAccountUnpaidThisMonth = (TextView) itemView.findViewById(R.id.tv_accountUnpaidThisMonth);
        tvAccountPaidTotal = (TextView) itemView.findViewById(R.id.tv_accountPaidTotal);
        tvAccountUnpaidTotal = (TextView) itemView.findViewById(R.id.tv_accountUnpaidTotal);

        tvCompletedTripToday = (TextView) itemView.findViewById(R.id.tv_completedTripToday);
        tvCompletedTripThisMonth = (TextView) itemView.findViewById(R.id.tv_completedTripThisMonth);
        tvCompletedTripTotal = (TextView) itemView.findViewById(R.id.tv_completedTripTotal);

        tvIncompleteTripToday = (TextView) itemView.findViewById(R.id.tv_incompleteTripToday);
        tvIncompleteTripThisMonth = (TextView) itemView.findViewById(R.id.tv_incompleteTripThisMonth);
        tvIncompleteTripTotal = (TextView) itemView.findViewById(R.id.tv_incompleteTripTotal);

        cvAccountToday = (CardView) itemView.findViewById(R.id.cv_accountToday);
        cvAccountThisMonth = (CardView) itemView.findViewById(R.id.cv_accountThisMonth);
        cvAccountTotal = (CardView) itemView.findViewById(R.id.cv_accountTotal);

        /*cvAccountToday.setOnClickListener(this);
        cvAccountThisMonth.setOnClickListener(this);
        cvAccountTotal.setOnClickListener(this);*/

    }

    public static VHAccountSummaryCard create(final Context context, final ViewGroup parent)
    {
        return new VHAccountSummaryCard(context, LayoutInflater.from(context)
                .inflate(R.layout.account_summary_card, parent, false));
    }

    public static void bind(final VHAccountSummaryCard vh, AccountSummary accountSummary)
    {
        vh.accountSummary = accountSummary;

        if (accountSummary.isBusyLoading())
        {
            vh.setFadeAnimation(vh.itemView);
        } else
        {
            vh.itemView.clearAnimation();
        }

        //TODAY
        vh.tvCompletedTripToday.setText(vh.context.getResources().getString(R.string.trip_completed) + " " +
                accountSummary.getTodayCompletedTrips());

        vh.tvIncompleteTripToday.setText(vh.context.getResources().getString(R.string.incomplete_trip) + " " +
                accountSummary.getTodayIncompleteTrips());

        vh.tvAccountPaidToday.setText(vh.context.getResources().getString(R.string.rs) + " " +
                accountSummary.getReceivedToday());

        vh.tvAccountUnpaidToday.setText(vh.context.getResources().getString(R.string.rs) + " " +
                (accountSummary.getReceivableToday() != 0 ?
                        accountSummary.getReceivableToday() - accountSummary.getReceivedToday() : 0));
        ////////////////////////////////////////////////////////////////////////////////////////////


        //THIS MONTH
        vh.tvIncompleteTripThisMonth.setText(vh.context.getResources().getString(R.string.incomplete_trip) + " " +
                accountSummary.getThisMonthIncompleteTrips());

        vh.tvCompletedTripThisMonth.setText(vh.context.getResources().getString(R.string.trip_completed) + " " +
                accountSummary.getThisMonthCompletedTrips());

        vh.tvAccountPaidThisMonth.setText(vh.context.getResources().getString(R.string.rs) + " " +
                accountSummary.getReceivedThisMonth());

        vh.tvAccountUnpaidThisMonth.setText(vh.context.getResources().getString(R.string.rs) + " " +
                (accountSummary.getReceivableThisMonth() != 0 ?
                        accountSummary.getReceivableThisMonth() - accountSummary.getReceivedThisMonth() : 0));
        ////////////////////////////////////////////////////////////////////////////////////////////


        //TOTAL
        vh.tvIncompleteTripThisMonth.setText(vh.context.getResources().getString(R.string.incomplete_trip) + " " +
                accountSummary.getThisMonthIncompleteTrips());

        vh.tvIncompleteTripTotal.setText(vh.context.getResources().getString(R.string.incomplete_trip) + " " +
                accountSummary.getTotalIncompleteTrips());

        vh.tvAccountPaidTotal.setText(vh.context.getResources().getString(R.string.rs) + " " +
                accountSummary.getTotalReceived());

        vh.tvAccountUnpaidTotal.setText(vh.context.getResources().getString(R.string.rs) + " " +
                (accountSummary.getTotalReceivable() != 0 ?
                        accountSummary.getTotalReceivable() - accountSummary.getTotalReceived() : 0));
        ////////////////////////////////////////////////////////////////////////////////////////////

    }

    @Override
    public void onClick(View view)
    {
        final Intent i = new Intent(context, AppCompatActivity.class);
        switch (view.getId())
        {
            case R.id.cv_accountToday:
                i.putExtra(Constants.ACCOUNT_TRIP_TYPE, Constants.AccountTripType.TODAY);
                i.putParcelableArrayListExtra(Constants.PARCELABLE_TRIP_LIST,
                        new ArrayList<Parcelable>(accountSummary.getTripToday()));
                context.startActivity(i);
                break;

            case R.id.cv_accountThisMonth:
                i.putExtra(Constants.ACCOUNT_TRIP_TYPE, Constants.AccountTripType.THIS_MONTH);
                i.putParcelableArrayListExtra(Constants.PARCELABLE_TRIP_LIST,
                        new ArrayList<Parcelable>(accountSummary.getTripThisMonth()));
                context.startActivity(i);
                break;

            case R.id.cv_accountTotal:
                i.putExtra(Constants.ACCOUNT_TRIP_TYPE, Constants.AccountTripType.TOTAL);
                i.putParcelableArrayListExtra(Constants.PARCELABLE_TRIP_LIST,
                        new ArrayList<Parcelable>(accountSummary.getTotalTrips()));
                context.startActivity(i);
                break;

        }
    }

    private void setFadeAnimation(View view)
    {
        AlphaAnimation anim = new AlphaAnimation(0.1f, 1.0f);
        anim.setDuration(500); // duration - half a second
        anim.setInterpolator(new LinearInterpolator()); // do not alter animation rate
        anim.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
        anim.setRepeatMode(Animation.REVERSE); // Reverse animation at the end so the button will fade back in
        anim.setInterpolator(EasingConstants.easeInOutCirc);
        view.setAnimation(anim);
        view.startAnimation(anim);
    }

}

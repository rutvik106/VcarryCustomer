package viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import apimodels.AccountSummary;
import butterknife.BindView;
import butterknife.ButterKnife;
import extra.EasingConstants;
import io.fusionbit.vcarrycustomer.ActivityTrips;
import io.fusionbit.vcarrycustomer.Constants;
import io.fusionbit.vcarrycustomer.R;

/**
 * Created by rutvik on 12/24/2016 at 9:09 AM.
 */

public class VHAccountSummaryCard extends RecyclerView.ViewHolder implements View.OnClickListener
{
    private final Context context;
    @BindView(R.id.tv_completedTripToday)
    TextView tvCompletedTripToday;
    @BindView(R.id.tv_incompleteTripToday)
    TextView tvIncompleteTripToday;
    @BindView(R.id.tv_accountPaidToday)
    TextView tvAccountPaidToday;
    @BindView(R.id.tv_accountUnpaidToday)
    TextView tvAccountUnpaidToday;
    @BindView(R.id.ll_accountToday)
    LinearLayout llAccountToday;
    @BindView(R.id.tv_completedTripThisMonth)
    TextView tvCompletedTripThisMonth;
    @BindView(R.id.tv_incompleteTripThisMonth)
    TextView tvIncompleteTripThisMonth;
    @BindView(R.id.tv_accountPaidThisMonth)
    TextView tvAccountPaidThisMonth;
    @BindView(R.id.tv_accountUnpaidThisMonth)
    TextView tvAccountUnpaidThisMonth;
    @BindView(R.id.ll_accountThisMonth)
    LinearLayout llAccountThisMonth;
    @BindView(R.id.tv_completedTripTotal)
    TextView tvCompletedTripTotal;
    @BindView(R.id.tv_incompleteTripTotal)
    TextView tvIncompleteTripTotal;
    @BindView(R.id.tv_accountPaidTotal)
    TextView tvAccountPaidTotal;
    @BindView(R.id.tv_accountUnpaidTotal)
    TextView tvAccountUnpaidTotal;
    @BindView(R.id.ll_accountTotal)
    LinearLayout llAccountTotal;


    private AccountSummary accountSummary;

    private VHAccountSummaryCard(Context context, View itemView)
    {
        super(itemView);
        this.context = context;

        ButterKnife.bind(this, itemView);

        llAccountToday.setOnClickListener(this);
        llAccountThisMonth.setOnClickListener(this);
        llAccountTotal.setOnClickListener(this);

    }

    public static VHAccountSummaryCard create(final Context context, final ViewGroup parent)
    {
        return new VHAccountSummaryCard(context, LayoutInflater.from(context)
                .inflate(R.layout.account_summary_card, parent, false));
    }

    public static void bind(final VHAccountSummaryCard vh, AccountSummary accountSummary)
    {
        vh.accountSummary = accountSummary;

        /*if (accountSummary.isBusyLoading())
        {
            vh.setFadeAnimation(vh.itemView);
        } else
        {
            vh.itemView.clearAnimation();
        }*/

        //TODAY
        vh.tvCompletedTripToday.setText(vh.context.getResources().getString(R.string.trip_completed) + " " +
                accountSummary.getTodayCompletedTrips());

        vh.tvIncompleteTripToday.setText(vh.context.getResources().getString(R.string.incomplete_trip) + " " +
                accountSummary.getTodayIncompleteTrips());

        vh.tvAccountPaidToday.setText(vh.context.getResources().getString(R.string.rs) + " " +
                accountSummary.getAccountSummaryNew().getToday().getPaid());

        vh.tvAccountUnpaidToday.setText(vh.context.getResources().getString(R.string.rs) + " " +
                accountSummary.getAccountSummaryNew().getToday().getPayable());

        /*vh.tvAccountPaidToday.setText(vh.context.getResources().getString(R.string.rs) + " " +
                accountSummary.getReceivedToday());

        vh.tvAccountUnpaidToday.setText(vh.context.getResources().getString(R.string.rs) + " " +
                (accountSummary.getReceivableToday() != 0 ?
                        accountSummary.getReceivableToday() - accountSummary.getReceivedToday() : 0));*/
        ////////////////////////////////////////////////////////////////////////////////////////////


        //THIS MONTH
        vh.tvIncompleteTripThisMonth.setText(vh.context.getResources().getString(R.string.incomplete_trip) + " " +
                accountSummary.getThisMonthIncompleteTrips());

        vh.tvCompletedTripThisMonth.setText(vh.context.getResources().getString(R.string.trip_completed) + " " +
                accountSummary.getThisMonthCompletedTrips());

        vh.tvAccountPaidThisMonth.setText(vh.context.getResources().getString(R.string.rs) + " " +
                accountSummary.getAccountSummaryNew().getThisMonth().getPaid());

        vh.tvAccountUnpaidThisMonth.setText(vh.context.getResources().getString(R.string.rs) + " " +
                accountSummary.getAccountSummaryNew().getThisMonth().getPayable());

        /*vh.tvAccountPaidThisMonth.setText(vh.context.getResources().getString(R.string.rs) + " " +
                accountSummary.getReceivedThisMonth());

        vh.tvAccountUnpaidThisMonth.setText(vh.context.getResources().getString(R.string.rs) + " " +
                (accountSummary.getReceivableThisMonth() != 0 ?
                        accountSummary.getReceivableThisMonth() - accountSummary.getReceivedThisMonth() : 0));*/
        ////////////////////////////////////////////////////////////////////////////////////////////


        //TOTAL
        vh.tvCompletedTripTotal.setText(vh.context.getResources().getString(R.string.trip_completed) + " " +
                accountSummary.getTotalCompletedTrips());

        vh.tvIncompleteTripTotal.setText(vh.context.getResources().getString(R.string.incomplete_trip) + " " +
                accountSummary.getTotalIncompleteTrips());

        vh.tvAccountPaidTotal.setText(vh.context.getResources().getString(R.string.rs) + " " +
                accountSummary.getAccountSummaryNew().getAllTime().getPaid());

        vh.tvAccountUnpaidTotal.setText(vh.context.getResources().getString(R.string.rs) + " " +
                accountSummary.getAccountSummaryNew().getAllTime().getPayable());

        /*vh.tvAccountPaidTotal.setText(vh.context.getResources().getString(R.string.rs) + " " +
                accountSummary.getTotalReceived());

        vh.tvAccountUnpaidTotal.setText(vh.context.getResources().getString(R.string.rs) + " " +
                (accountSummary.getTotalReceivable() != 0 ?
                        accountSummary.getTotalReceivable() - accountSummary.getTotalReceived() : 0));*/
        ////////////////////////////////////////////////////////////////////////////////////////////

    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.ll_accountToday:
                ActivityTrips.start(context, Constants.AccountTripType.TODAY);
                break;

            case R.id.ll_accountThisMonth:
                ActivityTrips.start(context, Constants.AccountTripType.THIS_MONTH);
                break;

            case R.id.ll_accountTotal:
                ActivityTrips.start(context, Constants.AccountTripType.TOTAL);
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

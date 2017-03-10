package adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import apimodels.AccountSummary;
import viewcomponents.AccountBalanceComponent;
import viewholder.VHAccountSummaryCard;


/**
 * Created by rutvik on 12/24/2016 at 9:18 AM.
 */

public class AccountBalanceAdapter extends RecyclerView.Adapter
{

    private final Context context;

    private final List<AccountBalanceComponent> accountBalanceComponentList;

    public AccountBalanceAdapter(Context context)
    {
        this.context = context;
        accountBalanceComponentList = new ArrayList<>();
    }

    public void addAccountSummaryCard(AccountSummary accountSummary)
    {
        accountBalanceComponentList.add(
                new AccountBalanceComponent(AccountBalanceComponent.ACCOUNT_SUMMARY_CARD,
                        accountSummary));
    }

    @Override
    public int getItemViewType(int position)
    {
        return accountBalanceComponentList.get(position).getViewType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        switch (viewType)
        {
            case AccountBalanceComponent.ACCOUNT_SUMMARY_CARD:
                return VHAccountSummaryCard.create(context, parent);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        switch (getItemViewType(position))
        {
            case AccountBalanceComponent.ACCOUNT_SUMMARY_CARD:
                VHAccountSummaryCard.bind((VHAccountSummaryCard) holder,
                        (AccountSummary) accountBalanceComponentList.get(position).getModel());
        }
    }

    @Override
    public int getItemCount()
    {
        return accountBalanceComponentList.size();
    }
}

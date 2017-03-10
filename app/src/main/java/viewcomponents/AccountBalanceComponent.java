package viewcomponents;

/**
 * Created by rutvik on 12/24/2016 at 8:46 AM.
 */

public class AccountBalanceComponent<T>
{

    public static final int TRIP_SUMMARY_CARD = 0;

    public static final int ACCOUNT_SUMMARY_CARD = 1;


    final T model;

    final int viewType;

    public AccountBalanceComponent(final int viewType, final T model)
    {
        this.viewType = viewType;
        this.model = model;
    }

    public T getModel()
    {
        return model;
    }

    public int getViewType()
    {
        return viewType;
    }
}

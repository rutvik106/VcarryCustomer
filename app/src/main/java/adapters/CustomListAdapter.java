package adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import apimodels.FromLocation;
import apimodels.SpinnerModel;
import extra.Log;
import io.fusionbit.vcarrycustomer.App;
import io.fusionbit.vcarrycustomer.R;

/**
 * Created by rutvik on 1/9/2017 at 3:55 PM.
 */

public class CustomListAdapter<T extends SpinnerModel> extends ArrayAdapter
{


    public final List<T> spinnerModelList;
    private final Context context;
    public List<T> suggestedSpinnerModelList;
    private CustomFilter filter;
    public CustomListAdapter(Context context, int resource, List<T> spinnerModelList)
    {
        super(context, resource, spinnerModelList);
        this.context = context;
        this.spinnerModelList = spinnerModelList;
        this.suggestedSpinnerModelList = spinnerModelList;
    }

    @Override
    public int getCount()
    {
        return suggestedSpinnerModelList.size();
    }

    @Nullable
    @Override
    public T getItem(int position)
    {
        return suggestedSpinnerModelList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return suggestedSpinnerModelList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
        View v = null;
        if (convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.single_spinner_row_item, parent, false);
        }
        TextView label = (TextView) convertView.findViewById(R.id.tv_spinnerItem);
        // Then you can get the current item using the values array (Users array) and the current position
        // You can NOW reference each method you has created in your bean object (User class)
        label.setText(suggestedSpinnerModelList.get(position).getLabel());

        label.setTextColor(Color.BLACK);
        label.setTextSize(15f);

        // And finally return your dynamic (or custom) view for each spinner item
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent)
    {
        if (convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.single_spinner_row_item, parent, false);
        }
        TextView label = (TextView) convertView.findViewById(R.id.tv_spinnerItem);
        label.setTextColor(Color.BLACK);
        label.setText(suggestedSpinnerModelList.get(position).getLabel());

        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter()
    {
        return filter;
    }

    public void setFilter(CustomFilter filter)
    {
        this.filter = filter;
    }

    public static class CustomFilter extends Filter
    {
        private static final String TAG = App.APP_TAG + CustomFilter.class.getSimpleName();
        List<FromLocation> spinnerModelList;
        List<FromLocation> suggestedSpinnerModelList;
        CustomListAdapter adapter;

        public CustomFilter(List<FromLocation> spinnerModelList, List<FromLocation> suggestedSpinnerModelList,
                            CustomListAdapter adapter)
        {
            this.spinnerModelList = spinnerModelList;
            this.suggestedSpinnerModelList = suggestedSpinnerModelList;
            this.adapter = adapter;
        }

        @Override
        protected FilterResults performFiltering(CharSequence charSequence)
        {
            final FilterResults filterResults = new FilterResults();
            final List<FromLocation> queryResult = new ArrayList<>();

            if (charSequence != null)
            {
                queryResult.clear();
                Log.i(TAG, "spinnerModelList: " + spinnerModelList.size());
                for (int i = 0; i < spinnerModelList.size(); i++)
                {
                    if (spinnerModelList.get(i).getLabel().toLowerCase()
                            .matches("(?i).*" + charSequence.toString().trim().toLowerCase() + ".*") ||
                            spinnerModelList.get(i).getShippingName().toLowerCase()
                                    .startsWith(charSequence.toString().trim().toLowerCase()))
                    {
                        Log.i(TAG, spinnerModelList.get(i).getLabel().toLowerCase());
                        Log.i(TAG, spinnerModelList.get(i).getShippingName().toLowerCase());

                        boolean isDuplicate = false;

                        for (FromLocation location : queryResult)
                        {
                            if (location.getShippingAddress().equals(spinnerModelList.get(i).getShippingAddress()))
                            {
                                isDuplicate = true;
                            }
                        }

                        if (!queryResult.contains(spinnerModelList.get(i)) && !isDuplicate)
                        {
                            queryResult.add(spinnerModelList.get(i));
                        }
                    }
                }
            }

            Log.i(TAG, "queryResult SIZE: " + queryResult.size());

            filterResults.values = queryResult;
            filterResults.count = queryResult.size();

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults)
        {
            suggestedSpinnerModelList = (List<FromLocation>) filterResults.values;

            if (suggestedSpinnerModelList.size() > 0 && charSequence != null)
            {
                /*if (suggestedSpinnerModelList.size() > 0)
                {*/
                for (FromLocation location : suggestedSpinnerModelList)
                {
                    Log.i(TAG, "SUGGESTED LOCATIONS: " + location.getShippingName());
                }
                adapter.suggestedSpinnerModelList = suggestedSpinnerModelList;
                adapter.notifyDataSetChanged();
                /*} else*/

            } else
            {
                suggestedSpinnerModelList = spinnerModelList;
                adapter.suggestedSpinnerModelList = suggestedSpinnerModelList;
                adapter.notifyDataSetInvalidated();
            }
        }
    }


}

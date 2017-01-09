package adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import apimodels.SpinnerModel;
import io.fusionbit.vcarrycustomer.R;

/**
 * Created by rutvik on 1/9/2017 at 3:55 PM.
 */

public class CustomListAdapter<T extends SpinnerModel> extends ArrayAdapter<T>
{


    private final Context context;

    private final List<T> spinnerModelList;

    public CustomListAdapter(Context context, int resource, List<T> spinnerModelList)
    {
        super(context, resource, spinnerModelList);

        this.context = context;
        this.spinnerModelList = spinnerModelList;

    }


    @Override
    public int getCount()
    {
        return spinnerModelList.size();
    }


    @Nullable
    @Override
    public T getItem(int position)
    {
        return spinnerModelList.get(position);
    }


    @Override
    public long getItemId(int position)
    {
        return spinnerModelList.get(position).getId();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
        View v = null;
        if (convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.single_spinner_row_item, parent,false);
        }
        TextView label = (TextView) convertView.findViewById(R.id.tv_spinnerItem);
        // Then you can get the current item using the values array (Users array) and the current position
        // You can NOW reference each method you has created in your bean object (User class)
        label.setText(spinnerModelList.get(position).getLabel());

        // And finally return your dynamic (or custom) view for each spinner item
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent)
    {
        if (convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.single_spinner_row_item, parent,false);
        }
        TextView label = (TextView) convertView.findViewById(R.id.tv_spinnerItem);
        label.setTextColor(Color.BLACK);
        label.setText(spinnerModelList.get(position).getLabel());

        return label;
    }

}

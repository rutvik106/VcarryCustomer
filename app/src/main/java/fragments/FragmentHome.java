package fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.fusionbit.vcarrycustomer.R;

/**
 * Created by rutvik on 11/20/2016 at 11:16 AM.
 */

public class FragmentHome extends Fragment
{

    Context context;

    public static FragmentHome newInstance(int index, Context context)
    {
        FragmentHome fragmentHome = new FragmentHome();
        fragmentHome.context = context;
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


        return view;
    }

}
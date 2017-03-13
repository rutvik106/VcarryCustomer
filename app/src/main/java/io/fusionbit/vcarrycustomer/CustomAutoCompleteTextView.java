package io.fusionbit.vcarrycustomer;

import android.content.Context;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by rutvik on 3/14/2017 at 2:13 AM.
 */

public class CustomAutoCompleteTextView extends AppCompatAutoCompleteTextView
{
    public CustomAutoCompleteTextView(Context context)
    {
        super(context);
    }

    public CustomAutoCompleteTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public CustomAutoCompleteTextView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK && isPopupShowing())
        {
            InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

            if (inputManager.hideSoftInputFromWindow(findFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS))
            {
                return true;
            }
        }

        return super.onKeyPreIme(keyCode, event);
    }
}

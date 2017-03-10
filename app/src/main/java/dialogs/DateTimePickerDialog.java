package dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.fusionbit.vcarrycustomer.R;

/**
 * Created by rutvik on 3/10/2017 at 4:35 PM.
 */

public class DateTimePickerDialog extends Dialog
{
    @BindView(R.id.tv_selectedDay)
    TextView tvSelectedDay;
    @BindView(R.id.tv_selectedMonth)
    TextView tvSelectedMonth;
    @BindView(R.id.tv_selectedYear)
    TextView tvSelectedYear;
    @BindView(R.id.ll_selectDate)
    LinearLayout llSelectDate;
    @BindView(R.id.tv_selectedTime)
    TextView tvSelectedTime;
    @BindView(R.id.ll_selectTime)
    LinearLayout llSelectTime;
    @BindView(R.id.fab_doneDateTime)
    FloatingActionButton fabDoneDateTime;

    Calendar selectedCalender;

    String title;
    @BindView(R.id.tv_datePickerTitle)
    TextView tvDatePickerTitle;

    OnDateTimeSetListener onDateTimeSetListener;

    public DateTimePickerDialog(@NonNull Context context, String title,
                                OnDateTimeSetListener onDateTimeSetListener)
    {
        super(context);
        this.title = title;
        this.onDateTimeSetListener = onDateTimeSetListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.date_time_picker_dialog);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(params);

        ButterKnife.bind(this);

        tvDatePickerTitle.setText(title);

        selectedCalender = Calendar.getInstance();

        setDateTime();

    }

    private void setDateTime()
    {
        final int day = selectedCalender.get(Calendar.DAY_OF_MONTH);
        final int month = selectedCalender.get(Calendar.MONTH) + 1;
        final int year = selectedCalender.get(Calendar.YEAR);

        tvSelectedDay.setText(day + "");
        tvSelectedMonth.setText(month + "");
        tvSelectedYear.setText(year + "");

        final int hour = selectedCalender.get(Calendar.HOUR);
        final int minute = selectedCalender.get(Calendar.MINUTE);
        final int amPm = selectedCalender.get(Calendar.AM_PM);

        String sHour, sMinute;

        if (hour == 0)
        {
            sHour = "12";
        } else if (hour < 10)
        {
            sHour = "0" + hour;
        } else
        {
            sHour = hour + "";
        }

        if (minute == 0)
        {
            sMinute = "00";
        } else if (minute < 10)
        {
            sMinute = "0" + minute;
        } else
        {
            sMinute = minute + "";
        }

        if (amPm == Calendar.AM)
        {
            tvSelectedTime.setText(sHour + ":" + sMinute + " AM");
        } else if (amPm == Calendar.PM)
        {
            tvSelectedTime.setText(sHour + ":" + sMinute + " PM");
        }

    }

    @OnClick({R.id.ll_selectDate, R.id.ll_selectTime, R.id.fab_doneDateTime})
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.ll_selectDate:

                new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener()
                {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth)
                    {
                        selectedCalender.set(Calendar.YEAR, year);
                        selectedCalender.set(Calendar.MONTH, monthOfYear);
                        selectedCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        setDateTime();
                    }
                }, selectedCalender.get(Calendar.YEAR), selectedCalender.get(Calendar.MONTH),
                        selectedCalender.get(Calendar.DAY_OF_MONTH)).show();

                break;
            case R.id.ll_selectTime:

                new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener()
                {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute)
                    {
                        selectedCalender.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        selectedCalender.set(Calendar.MINUTE, minute);
                        setDateTime();
                    }
                }, selectedCalender.get(Calendar.HOUR_OF_DAY),
                        selectedCalender.get(Calendar.MINUTE), false).show();

                break;
            case R.id.fab_doneDateTime:
                if (onDateTimeSetListener != null)
                {
                    onDateTimeSetListener.onDateTimeSet(this, selectedCalender.get(Calendar.DAY_OF_MONTH),
                            selectedCalender.get(Calendar.MONTH), selectedCalender.get(Calendar.YEAR),
                            selectedCalender.get(Calendar.HOUR_OF_DAY), selectedCalender.get(Calendar.MINUTE));
                    ;
                }
                break;
        }
    }

    public interface OnDateTimeSetListener
    {
        void onDateTimeSet(DateTimePickerDialog dialog, int dayOfMonth, int month, int year,
                           int hourIn24, int minute);
    }

}

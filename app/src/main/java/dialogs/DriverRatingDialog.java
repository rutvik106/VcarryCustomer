package dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.AppCompatRatingBar;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.fusionbit.vcarrycustomer.R;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by rutvik on 5/9/2017 at 5:20 PM.
 */

public class DriverRatingDialog extends Dialog
{
    final String driverId, driverName, driverImage;
    @BindView(R.id.iv_ratingBoxDriverImage)
    ImageView ivRatingBoxDriverImage;
    @BindView(R.id.tv_ratingBoxText)
    TextView tvRatingBoxText;
    @BindView(R.id.rb_ratingBoxRatingBar)
    AppCompatRatingBar rbRatingBoxRatingBar;
    @BindView(R.id.btn_ratingBoxSubmit)
    Button btnRatingBoxSubmit;

    public DriverRatingDialog(Context context, String driverId, String driverName, String image)
    {
        super(context);
        this.driverImage = image;
        this.driverName = driverName;
        this.driverId = driverId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_rate_driver);

        ButterKnife.bind(this);

        if (getWindow() != null)
        {
            WindowManager.LayoutParams params = getWindow().getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            getWindow().setAttributes(params);
        }

        Glide.with(getContext())
                .load(driverImage)
                .placeholder(R.drawable.driver_photo_placeholder)
                .error(R.drawable.driver_photo_placeholder)
                .bitmapTransform(new CropCircleTransformation(getContext()))
                .into(ivRatingBoxDriverImage);

        tvRatingBoxText.setText("How much would you like to rate " + driverName + " out of 5?");

    }

    @OnClick(R.id.btn_ratingBoxSubmit)
    public void onClick()
    {
        Toast.makeText(getContext(), "Thank you.", Toast.LENGTH_SHORT).show();
        dismiss();
    }
}

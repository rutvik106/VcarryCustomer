package io.fusionbit.vcarrycustomer;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatRatingBar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import javax.inject.Inject;

import api.API;
import api.RetrofitCallbacks;
import apimodels.TripByCustomerId;
import butterknife.BindView;
import butterknife.OnClick;
import dialogs.DriverRatingDialog;
import extra.LocaleHelper;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import retrofit2.Call;
import retrofit2.Response;

public class ActivityTripDetails extends BaseActivity
{
    @BindView(R.id.tv_tripStatus)
    TextView tvTripStatus;
    @BindView(R.id.tv_tripNumber)
    TextView tvTripNumber;
    @BindView(R.id.tv_tripDetailTime)
    TextView tvTripDetailTime;
    @BindView(R.id.tv_driverName)
    TextView tvDriverName;
    @BindView(R.id.tv_tripDriverLicenceNo)
    TextView tvTripDriverLicenceNo;
    @BindView(R.id.tv_vehicleNo)
    TextView tvVehicleNo;
    @BindView(R.id.ll_tripStartedDetails)
    LinearLayout llTripStartedDetails;
    @BindView(R.id.tv_tripFromCompanyName)
    TextView tvTripFromCompanyName;
    @BindView(R.id.tv_tripLocation)
    TextView tvTripLocation;
    @BindView(R.id.tv_tripToCompanyName)
    TextView tvTripToCompanyName;
    @BindView(R.id.tv_tripDestination)
    TextView tvTripDestination;
    @BindView(R.id.tv_tripFare)
    TextView tvTripFare;

    String tripId, tripNumber;

    TripByCustomerId tripDetails;

    @Inject
    Realm realm;

    @Inject
    API api;
    @BindView(R.id.tv_tripWeight)
    TextView tvTripWeight;
    @BindView(R.id.tv_tripDimension)
    TextView tvTripDimension;
    @BindView(R.id.iv_driverPhoto)
    ImageView ivDriverPhoto;
    @BindView(R.id.cl_ActivityTripDetails)
    CoordinatorLayout clActivityTripDetails;

    Snackbar sbNoInternet;
    @BindView(R.id.rb_rateDriver)
    AppCompatRatingBar rbRateDriver;
    @BindView(R.id.tv_tripChargesDetails)
    TextView tvTripChargesDetails;

    // Hold a reference to the current animator,
    // so that it can be canceled mid-way.
    private Animator mCurrentAnimator;

    // The system "short" animation time duration, in milliseconds. This
    // duration is ideal for subtle animations or animations that occur
    // very frequently.
    private int mShortAnimationDuration;

    public static void start(Context context, String tripId)
    {
        Intent i = new Intent(context, ActivityTripDetails.class);
        i.putExtra(Constants.INTENT_EXTRA_TRIP_ID, tripId);
        context.startActivity(i);
    }

    public static void start(String tripNumber, Context context)
    {
        Intent i = new Intent(context, ActivityTripDetails.class);
        i.putExtra(Constants.INTENT_EXTRA_TRIP_NUMBER, tripNumber);

        final TripByCustomerId trip = Realm.getDefaultInstance()
                .where(TripByCustomerId.class)
                .equalTo("tripNo", tripNumber)
                .findFirst();

        if (trip != null)
        {
            i.putExtra(Constants.INTENT_EXTRA_TRIP_ID, trip.getTripId());
        }

        context.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        tripId = getIntent().getStringExtra(Constants.INTENT_EXTRA_TRIP_ID);
        tripNumber = getIntent().getStringExtra(Constants.INTENT_EXTRA_TRIP_NUMBER);
        ((App) getApplication()).getUser().inject(this);
        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setTitle("Trip Details");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mShortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);

        Glide.with(this)
                .load(R.drawable.driver_photo_placeholder)
                .bitmapTransform(new CropCircleTransformation(this))
                .into(ivDriverPhoto);

        ivDriverPhoto.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                zoomImageFromThumb(ivDriverPhoto, tripDetails.getDriverImage());
            }
        });

    }

    @Override
    protected void onStart()
    {
        super.onStart();

        tryToGetFromRealm();
        getTripDetails();

    }

    private void getTripDetails()
    {
        if (tripId != null)
        {
            api.getTripDetailsByTripId(tripId, new RetrofitCallbacks<TripByCustomerId>()
            {

                @Override
                public void onResponse(Call<TripByCustomerId> call, Response<TripByCustomerId> response)
                {
                    super.onResponse(call, response);
                    if (response.isSuccessful())
                    {
                        handleResponse(response);
                    }
                }
            });
        } else if (tripNumber != null)
        {
            api.getTripDetailsByTripNo(tripNumber, new RetrofitCallbacks<TripByCustomerId>()
            {

                @Override
                public void onResponse(Call<TripByCustomerId> call, Response<TripByCustomerId> response)
                {
                    super.onResponse(call, response);
                    if (response.isSuccessful())
                    {
                        handleResponse(response);
                    }
                }
            });
        }
    }

    private void handleResponse(Response<TripByCustomerId> response)
    {
        if (response.body() != null)
        {
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(response.body());
            realm.commitTransaction();

            bindDataToUi();
        }
    }

    private void tryToGetFromRealm()
    {
        if (tripId != null)
        {
            tripDetails =
                    realm.where(TripByCustomerId.class).equalTo("tripId", tripId).findFirst();
        } else if (tripNumber != null)
        {
            tripDetails = realm.where(TripByCustomerId.class).equalTo("tripNo", tripNumber).findFirst();
        }


        if (tripDetails != null)
        {
            bindDataToUi();
            tripDetails.addChangeListener(new RealmChangeListener<TripByCustomerId>()
            {
                @Override
                public void onChange(TripByCustomerId tripDetails)
                {
                    bindDataToUi();
                }
            });
        }
    }

    private void bindDataToUi()
    {
        tvTripStatus.setText(tripDetails.getStatus());

        if (tripDetails.getTripNo() != null)
        {
            tvTripNumber.setText(tripDetails.getTripNo());
        } else
        {
            tvTripNumber.setVisibility(View.GONE);
        }

        tvTripDetailTime.setText(tripDetails.getTripDatetimeDmy());

        if (tripDetails.getDriverName() != null)
        {
            tvDriverName.setText(tripDetails.getDriverName());
            tvVehicleNo.setText(tripDetails.getVehicleRegNo() != null ?
                    !tripDetails.getVehicleRegNo().isEmpty() ? tripDetails.getVehicleRegNo() : "NA" : "NA");
            tvTripDriverLicenceNo.setText(tripDetails.getLicenceNo() != null ?
                    !tripDetails.getLicenceNo().isEmpty() ? tripDetails.getLicenceNo() : "NA" : "NA");
            if (tripDetails.getDriverImage() != null)
            {
                Glide.with(this)
                        .load(tripDetails.getDriverImage())
                        .bitmapTransform(new CropCircleTransformation(this))
                        .into(ivDriverPhoto);
            }

            rbRateDriver.setOnTouchListener(new View.OnTouchListener()
            {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent)
                {
                    new DriverRatingDialog(ActivityTripDetails.this, tripDetails.getDriverId(),
                            tripDetails.getDriverName(), tripDetails.getDriverImage()).show();
                    return false;
                }
            });

        } else
        {
            llTripStartedDetails.setVisibility(View.GONE);
        }


        if (LocaleHelper.getLanguage(this).equalsIgnoreCase("gu"))
        {
            tvTripLocation.setText(tripDetails.getFromGujaratiAddress());
            tvTripDestination.setText(tripDetails.getToGujaratiAddress());
        } else
        {
            tvTripLocation.setText(tripDetails.getFromShippingLocation());
            tvTripDestination.setText(tripDetails.getToShippingLocation());
        }

        if (LocaleHelper.getLanguage(this).equalsIgnoreCase("gu"))
        {
            tvTripFromCompanyName.setText(tripDetails.getFromGujaratiName());
            tvTripToCompanyName.setText(tripDetails.getToGujaratiName());
        } else
        {
            tvTripFromCompanyName.setText(tripDetails.getFromCompanyName());
            tvTripToCompanyName.setText(tripDetails.getToCompanyName());
        }

        tvTripWeight.setText(tripDetails.getWeight() != null ? !tripDetails.getWeight().isEmpty()
                ? tripDetails.getWeight() : "NA" : "NA");

        tvTripDimension.setText(tripDetails.getDimensions() != null ? !tripDetails.getDimensions().isEmpty()
                ? tripDetails.getDimensions() : "NA" : "NA");


        tvTripFare.setText(tripDetails.getFare() != null ? !tripDetails.getFare().isEmpty() ?
                getString(R.string.rs) + " " + tripDetails.getFare() : "NA" : "NA");

    }

    @Override
    protected int getLayoutResourceId()
    {
        return R.layout.activity_trip_details;
    }

    @Override
    protected void internetNotAvailable()
    {
        if (sbNoInternet == null)
        {
            sbNoInternet = Snackbar.make(clActivityTripDetails, R.string.no_internet, Snackbar.LENGTH_INDEFINITE);
            sbNoInternet.show();
        }
    }

    @Override
    protected void internetAvailable()
    {
        if (sbNoInternet != null)
        {
            if (sbNoInternet.isShown())
            {
                sbNoInternet.dismiss();
                sbNoInternet = null;
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy()
    {
        tripDetails.removeChangeListeners();
        super.onDestroy();
    }


    private void zoomImageFromThumb(final View thumbView, String image)
    {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        if (mCurrentAnimator != null)
        {
            mCurrentAnimator.cancel();
        }

        // Load the high-resolution "zoomed-in" image.
        final ImageView expandedImageView = (ImageView) findViewById(
                R.id.expanded_image);

        Glide.with(this)
                .load(image != null ? image : R.drawable.driver_photo_placeholder)
                .into(expandedImageView);

        final FrameLayout flExpandedImage = (FrameLayout) findViewById(R.id.fl_expandedImage);

        //expandedImageView.setImageResource(imageResId);

        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        thumbView.getGlobalVisibleRect(startBounds);
        findViewById(R.id.cl_ActivityTripDetails)
                .getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height())
        {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else
        {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        thumbView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);
        flExpandedImage.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                        startScale, 1f)).with(ObjectAnimator.ofFloat(expandedImageView,
                View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation)
            {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (mCurrentAnimator != null)
                {
                    mCurrentAnimator.cancel();
                }

                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(expandedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.Y, startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_Y, startScaleFinal));
                set.setDuration(mShortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter()
                {
                    @Override
                    public void onAnimationEnd(Animator animation)
                    {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        flExpandedImage.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation)
                    {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        flExpandedImage.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }
                });
                set.start();
                mCurrentAnimator = set;
            }
        });
    }

    @OnClick(R.id.tv_tripChargesDetails)
    public void onClick()
    {
        ActivityFareDetails.start(this, tripId);
    }
}

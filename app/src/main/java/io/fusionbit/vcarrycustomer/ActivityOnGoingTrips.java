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
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import javax.inject.Inject;

import adapters.TripDetailsAdapter;
import api.API;
import api.RetrofitCallbacks;
import apimodels.TripByCustomerId;
import butterknife.BindView;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import models.BookedTrip;
import retrofit2.Call;
import retrofit2.Response;
import viewholder.VHSingleTripDetails;

public class ActivityOnGoingTrips extends BaseActivity implements VHSingleTripDetails.OnDriverPhotoClickListener, SwipeRefreshLayout.OnRefreshListener {
    public static final String TAG = App.APP_TAG + ActivityOnGoingTrips.class.getSimpleName();

    @BindView(R.id.rv_onGoingTrips)
    RecyclerView rvOnGoingTrips;
    @BindView(R.id.cl_activityOnGoingTrips)
    CoordinatorLayout clActivityOnGoingTrips;

    Snackbar sbNoInternet;

    @Inject
    Realm realm;

    TripDetailsAdapter adapter;
    @BindView(R.id.fl_noActiveTrips)
    FrameLayout flNoActiveTrips;
    @BindView(R.id.srl_refreshActiveTrips)
    SwipeRefreshLayout srlRefreshActiveTrips;
    private Call<List<TripByCustomerId>> call;
    private RealmResults<TripByCustomerId> tripByCustomerIds;

    // Hold a reference to the current animator,
    // so that it can be canceled mid-way.
    private Animator mCurrentAnimator;

    // The system "short" animation time duration, in milliseconds. This
    // duration is ideal for subtle animations or animations that occur
    // very frequently.
    private int mShortAnimationDuration;


    public static void start(Context context) {
        context.startActivity(new Intent(context, ActivityOnGoingTrips.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.active_trips);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ((App) getApplication()).getUser().inject(this);

        mShortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);
        srlRefreshActiveTrips.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        srlRefreshActiveTrips.setOnRefreshListener(this);

        setupRecyclerView();

        getOnGoingTripsFromRealm();

        getTripsFromAPI();

        logPendingTrips();

    }

    private void setupRecyclerView() {
        rvOnGoingTrips.setLayoutManager(new LinearLayoutManager(this));
        rvOnGoingTrips.setHasFixedSize(true);
        adapter = new TripDetailsAdapter(this, this);
        rvOnGoingTrips.setAdapter(adapter);
    }

    private void getOnGoingTripsFromRealm() {

        final RealmResults<BookedTrip> bookedTrips =
                realm.where(BookedTrip.class)
                        .equalTo("tripStatus", Constants.TRIP_STATUS_PENDING)
                        .findAll();

        for (BookedTrip bookedTrip : bookedTrips) {
            Log.i(TAG, "REALM TRIP STATUS: " + bookedTrip.getTripStatus());
            adapter.addBookedTrip(BookedTrip.bakePendingTrip(bookedTrip));
        }

        bookedTrips.addChangeListener(new RealmChangeListener<RealmResults<BookedTrip>>() {
            @Override
            public void onChange(RealmResults<BookedTrip> element) {
                Log.i(TAG, "BOOKED TRIP ON CHANGED REALM");
                if (element.isEmpty()) {
                    adapter.clearPendingTrips();
                } else {
                    Log.i(TAG, "BOOKED TRIP CHANGE SIZE: " + element.size());
                }
            }
        });

        tripByCustomerIds =
                realm.where(TripByCustomerId.class)
                        .notEqualTo("tripStatus", Constants.TRIP_STATUS_FINISHED)
                        .notEqualTo("tripStatus", Constants.TRIP_STATUS_CANCELLED_BY_CUSTOMER)
                        .notEqualTo("tripStatus", Constants.TRIP_STATUS_CANCELLED_BY_DRIVER)
                        .notEqualTo("tripStatus", Constants.TRIP_STATUS_CANCELLED_BY_VCARRY)
                        .findAll();

        for (TripByCustomerId trip : tripByCustomerIds) {
            Log.i(TAG, "getDriverDeviceToken: " + trip.getDriverDeviceToken());
            Log.i(TAG, "getDeviceToken: " + trip.getDeviceToken());
            adapter.addBookedTrip(trip);
        }


        tripByCustomerIds.addChangeListener(new RealmChangeListener<RealmResults<TripByCustomerId>>() {
            @Override
            public void onChange(RealmResults<TripByCustomerId> element) {

                adapter.clearAll();

                final RealmResults<BookedTrip> bookedTrips =
                        realm.where(BookedTrip.class)
                                .equalTo("tripStatus", Constants.TRIP_STATUS_PENDING)
                                .findAll();


                for (BookedTrip bookedTrip : bookedTrips) {
                    Log.i(TAG, "REALM TRIP STATUS: " + bookedTrip.getTripStatus());
                    adapter.addBookedTrip(BookedTrip.bakePendingTrip(bookedTrip));
                }


                Log.i(TAG, "tripByCustomerIds SIZE: " + tripByCustomerIds.size());
                Log.i(TAG, "element SIZE: " + element.size());
                for (TripByCustomerId trip : element) {
                    Log.i(TAG, "TRIP STATUS: " + trip.getStatus() + " STATUS NO: " + trip.getTripStatus());
                    Log.i(TAG, "ADDING TRIP FROM ON CHANGE REALM");
                    adapter.addBookedTrip(trip);
                }

                Log.i(TAG, "ADAPTER SIZE: " + adapter.getItemCount());

                if (adapter.getItemCount() > 0) {
                    flNoActiveTrips.setVisibility(View.GONE);
                } else {
                    flNoActiveTrips.setVisibility(View.VISIBLE);
                }

            }
        });

        Log.i(TAG, "ADAPTER SIZE: " + adapter.getItemCount());

        if (adapter.getItemCount() > 0) {
            flNoActiveTrips.setVisibility(View.GONE);
        }

    }

    private void getTripsFromAPI() {
        srlRefreshActiveTrips.setRefreshing(true);
        final String customerId = PreferenceManager.getDefaultSharedPreferences(this)
                .getString(Constants.CUSTOMER_ID, null);
        if (customerId != null) {
            if (call != null) {
                call.cancel();
            }
            call = API.getInstance().getTripsByCustomerId(customerId, new RetrofitCallbacks<List<TripByCustomerId>>() {
                @Override
                public void onResponse(Call<List<TripByCustomerId>> call, Response<List<TripByCustomerId>> response) {
                    super.onResponse(call, response);
                    if (srlRefreshActiveTrips.isRefreshing()) {
                        srlRefreshActiveTrips.setRefreshing(false);
                    }
                    if (response.isSuccessful()) {
                        realm.beginTransaction();
                        for (TripByCustomerId trip : response.body()) {
                            realm.copyToRealmOrUpdate(trip);
                        }
                        realm.commitTransaction();
                    }
                }

                @Override
                public void onFailure(Call<List<TripByCustomerId>> call, Throwable t) {
                    super.onFailure(call, t);
                    if (srlRefreshActiveTrips.isRefreshing()) {
                        srlRefreshActiveTrips.setRefreshing(false);
                    }
                }
            });
        }
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_on_going_trips;
    }

    @Override
    protected void internetNotAvailable() {
        if (sbNoInternet == null) {
            sbNoInternet = Snackbar.make(clActivityOnGoingTrips, R.string.no_internet, Snackbar.LENGTH_INDEFINITE);
            sbNoInternet.show();
        }
    }

    @Override
    protected void internetAvailable() {
        if (sbNoInternet != null) {
            if (sbNoInternet.isShown()) {
                sbNoInternet.show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        if (call != null) {
            call.cancel();
        }
        super.onDestroy();
    }

    private void zoomImageFromThumb(final View thumbView, String image) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        if (mCurrentAnimator != null) {
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
        findViewById(R.id.cl_activityOnGoingTrips)
                .getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
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
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentAnimator != null) {
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
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        flExpandedImage.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
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

    @Override
    public void openImageInFullView(View view, String image) {
        zoomImageFromThumb(view, image);
    }

    @Override
    public void onBackPressed() {
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
            mCurrentAnimator = null;
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onRefresh() {
        //getOnGoingTripsFromRealm();

        getTripsFromAPI();
    }

    @Override
    protected void onStop() {
        adapter.stopTimers();
        super.onStop();
    }

    private void logPendingTrips() {
        RealmResults<BookedTrip> bookedTrips = realm.where(BookedTrip.class)
                .findAll();

        for (BookedTrip trip : bookedTrips) {
            Log.i(TAG, "Booked Trip ID: " + trip.getCustomerTripId());
        }

    }

}

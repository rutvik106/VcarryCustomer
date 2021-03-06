package fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import adapters.TripDetailsAdapter;
import api.API;
import api.RetrofitCallbacks;
import apimodels.TripByCustomerId;
import butterknife.BindView;
import butterknife.ButterKnife;
import extra.Log;
import io.fusionbit.vcarrycustomer.App;
import io.fusionbit.vcarrycustomer.Constants;
import io.fusionbit.vcarrycustomer.R;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Response;
import viewholder.VHSingleTripDetails;

/**
 * Created by rutvik on 11/20/2016 at 11:16 AM.
 */

public class FragmentTrips extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        VHSingleTripDetails.OnDriverPhotoClickListener {

    private static final String TAG = App.APP_TAG + FragmentTrips.class.getSimpleName();
    final List<String> tripIds = new ArrayList<>();
    @BindView(R.id.rv_userActivity)
    RecyclerView rvUserActivity;
    @BindView(R.id.ll_homeEmpty)
    LinearLayout llHomeEmpty;
    @BindView(R.id.srl_refreshTrips)
    SwipeRefreshLayout srlRefreshTrips;
    TripDetailsAdapter adapter;
    Realm realm = Realm.getDefaultInstance();
    View view;
    private RealmResults<TripByCustomerId> bookedTripRealmResults;
    // Hold a reference to the current animator,
    // so that it can be canceled mid-way.
    private Animator mCurrentAnimator;
    // The system "short" animation time duration, in milliseconds. This
    // duration is ideal for subtle animations or animations that occur
    // very frequently.
    private int mShortAnimationDuration;

    public static FragmentTrips newInstance(int index) {
        FragmentTrips fragmentTrips = new FragmentTrips();
        Bundle b = new Bundle();
        b.putInt("index", index);
        fragmentTrips.setArguments(b);
        return fragmentTrips;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_trips, container, false);

            ButterKnife.bind(this, view);

            mShortAnimationDuration = getResources().getInteger(
                    android.R.integer.config_shortAnimTime);

            adapter = new TripDetailsAdapter(getActivity(), this);

            rvUserActivity.setLayoutManager(new LinearLayoutManager(getActivity()));
            rvUserActivity.setHasFixedSize(true);
            rvUserActivity.setAdapter(adapter);

            srlRefreshTrips.setColorSchemeColors(getResources().getColor(R.color.colorAccent));

            srlRefreshTrips.setOnRefreshListener(this);

            getTripsFromRealm();
            getTrips();

        }
        return view;
    }


    private void getTripsFromRealm() {
        adapter.clearAll();

        bookedTripRealmResults =
                realm.where(TripByCustomerId.class).findAll();

        bookedTripRealmResults.addChangeListener(new RealmChangeListener<RealmResults<TripByCustomerId>>() {
            @Override
            public void onChange(RealmResults<TripByCustomerId> element) {
                adapter.notifyDataSetChanged();
            }
        });

        for (TripByCustomerId bookedTrip : bookedTripRealmResults) {
            adapter.addBookedTrip(bookedTrip);
        }

        if (adapter.getItemCount() != 0) {
            llHomeEmpty.setVisibility(View.GONE);
        } else {
            llHomeEmpty.setVisibility(View.VISIBLE);
        }
    }


    private void getTrips() {
        final String customerId = PreferenceManager.getDefaultSharedPreferences(getActivity())
                .getString(Constants.CUSTOMER_ID, null);
        API.getInstance().getTripsByCustomerId(customerId, new RetrofitCallbacks<List<TripByCustomerId>>() {

            @Override
            public void onResponse(Call<List<TripByCustomerId>> call, Response<List<TripByCustomerId>> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    Log.i(TAG, "TOTAL TRIPS: " + response.body().size());
                    realm.beginTransaction();
                    for (TripByCustomerId trip : response.body()) {
                        if (!tripIds.contains(trip.getTripId())) {
                            Log.i(TAG, "TRIP ID: " + trip.getTripId());
                            tripIds.add(trip.getTripId());
                        } else {
                            Log.i(TAG, "DUPLICATE TRIP ID: " + trip.getTripId());
                        }
                        realm.copyToRealmOrUpdate(trip);
                    }
                    realm.commitTransaction();
                    getTripsFromRealm();
                } else {
                    Toast.makeText(getActivity(), R.string.something_went_wrong,
                            Toast.LENGTH_SHORT).show();
                }
                if (srlRefreshTrips.isRefreshing()) {
                    srlRefreshTrips.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<List<TripByCustomerId>> call, Throwable t) {
                super.onFailure(call, t);
                /*Toast.makeText(getActivity(), R.string.something_went_wrong,
                        Toast.LENGTH_SHORT).show();*/
                if (srlRefreshTrips.isRefreshing()) {
                    srlRefreshTrips.setRefreshing(false);
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        getTrips();
    }


    private void zoomImageFromThumb(final View thumbView, String image) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        // Load the high-resolution "zoomed-in" image.
        final ImageView expandedImageView = (ImageView) view.findViewById(
                R.id.expanded_image);

        Glide.with(this)
                .load(image != null ? image : R.drawable.driver_photo_placeholder)
                .into(expandedImageView);

        final FrameLayout flExpandedImage = (FrameLayout) view.findViewById(R.id.fl_expandedImage);

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
        view.findViewById(R.id.rl_fragTrips)
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
}
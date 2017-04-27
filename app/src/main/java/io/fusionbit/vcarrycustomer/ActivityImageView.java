package io.fusionbit.vcarrycustomer;

import android.animation.Animator;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class ActivityImageView extends FragmentActivity
{

    // Hold a reference to the current animator,
    // so that it can be canceled mid-way.
    private Animator mCurrentAnimator;

    // The system "short" animation time duration, in milliseconds. This
    // duration is ideal for subtle animations or animations that occur
    // very frequently.
    private int mShortAnimationDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
    }
}

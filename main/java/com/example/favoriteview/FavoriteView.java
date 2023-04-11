package com.example.favoriteview;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class FavoriteView extends View {
    private final Drawable drawable;
    private int drawableColor;
    private final int defaultColor;
    private final int pressedColor;
    private boolean isPressed;
    private FavoriteListener favoriteListener;

    public FavoriteView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = getContext().getTheme()
                .obtainStyledAttributes(attrs, R.styleable.FavoriteView, 0, 0);
        try {
            drawable = typedArray.getDrawable(R.styleable.FavoriteView_drawable);
            defaultColor = typedArray.getColor(R.styleable.FavoriteView_default_color, Color.GRAY);
            pressedColor = typedArray.getColor(R.styleable.FavoriteView_pressed_color, Color.RED);
        } finally {
            typedArray.recycle();
        }

        drawableColor = defaultColor;
        drawable.setColorFilter(new PorterDuffColorFilter(drawableColor, PorterDuff.Mode.SRC_IN));
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startAnimation();
                isPressed = !isPressed;
                int newColor = isPressed ? pressedColor : defaultColor;
                animateColor(newColor);
                if (favoriteListener != null) {
                    favoriteListener.onFavoriteChanged(isPressed);
                }
                break;
            case MotionEvent.ACTION_UP:
                performClick(); // Handle the click event
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean performClick() {
        super.performClick();
        // Handle the click event here
        setFavorite(!isPressed);
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawable.setBounds(0, 0, getWidth(), getHeight());
        drawable.draw(canvas);
    }

    private void startAnimation() {
        ObjectAnimator animScaleX = ObjectAnimator.ofFloat(this, "scaleX", 1f, 0.9f, 1f);
        ObjectAnimator animScaleY = ObjectAnimator.ofFloat(this, "scaleY", 1f, 0.9f, 1f);
        AnimatorSet animSet = new AnimatorSet();
        animSet.playTogether(animScaleX, animScaleY);
        animSet.setDuration(250); // Change duration as per your requirement
        animSet.start();
    }

    private void animateColor(int color) {
        ValueAnimator colorAnimation = ValueAnimator.ofArgb(drawableColor, color);
        colorAnimation.setDuration(200);
        colorAnimation.addUpdateListener(animator -> {
            drawableColor = (int) animator.getAnimatedValue();
            drawable.setColorFilter(new PorterDuffColorFilter(drawableColor, PorterDuff.Mode.SRC_IN));
            invalidate();
        });
        colorAnimation.start();
    }

    public void setFavorite(boolean isFavorite) {
        if (isFavorite != isPressed) {
            startAnimation();
            isPressed = isFavorite;
            int newColor = isPressed ? pressedColor : defaultColor;
            animateColor(newColor);
            if (favoriteListener != null) {
                favoriteListener.onFavoriteChanged(isPressed);
            }
            // Call invalidate to redraw the view with the new state
            invalidate();
        }
    }

    public void setFavoriteListener(FavoriteView.FavoriteListener listener) {
        favoriteListener = (FavoriteListener) listener;
    }

    public interface FavoriteListener {
        void onFavoriteChanged(boolean isFavorite);
    }
}

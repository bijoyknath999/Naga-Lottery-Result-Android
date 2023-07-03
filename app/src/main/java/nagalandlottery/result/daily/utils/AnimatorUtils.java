package nagalandlottery.result.daily.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

public class AnimatorUtils {

    public static void collapseView(View view) {
        int height = view.getHeight();

        ValueAnimator valueAnimator = ValueAnimator.ofInt(height, 0);

        valueAnimator.setDuration(250);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                int value = (int) animator.getAnimatedValue();
                view.getLayoutParams().height = value;
                view.requestLayout();
            }
        });

        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setVisibility(View.GONE);
            }
        });

        valueAnimator.start();
    }

}

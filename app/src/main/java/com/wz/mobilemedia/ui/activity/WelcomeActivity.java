package com.wz.mobilemedia.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.eftimoff.androipathview.PathView;
import com.wz.mobilemedia.R;

/**
 * Created by wz on 17-5-17.
 */

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_welcome);
        PathView pathView = (PathView) findViewById(R.id.pathView);

        pathView.getPathAnimator()
                .delay(100)
                .listenerEnd(new PathView.AnimatorBuilder.ListenerEnd() {
                    @Override
                    public void onAnimationEnd() {
                        startActivity(new Intent(WelcomeActivity.this,MainActivity.class));
                        finish();
                    }
                })
                .duration(3000)
                .interpolator(new AccelerateDecelerateInterpolator())
                .start();

    }
}

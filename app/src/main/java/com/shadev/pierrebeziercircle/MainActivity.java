package com.shadev.pierrebeziercircle;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;

public class MainActivity extends AppCompatActivity {

	private MagicCircle mMagicCircle;
	private MagicCircle.Builder builder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mMagicCircle = (MagicCircle) findViewById(R.id.circle);
		builder = new MagicCircle.Builder(mMagicCircle);
	}

	public void startCircleAnimation(View view) {
		builder .setDuration(2000)
//				.setStartColor(Color.RED)
//				.setEndColor(Color.BLUE)
				.setColor(Color.BLACK)
				.setInterpolator(new AccelerateDecelerateInterpolator())
				.setRepeatCount(Animation.INFINITE)
				.setRepeatMode(Animation.REVERSE)
				.start();
	}
}

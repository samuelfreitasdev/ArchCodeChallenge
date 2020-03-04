package com.arctouch.codechallenge.util;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import androidx.core.view.GestureDetectorCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {

	private final OnItemClickListener mClickListener;
	private final OnLongItemClickListener mOnLongItemClickListener;

	private final GestureDetectorCompat mGestureDetector;

	private RecyclerView recyclerView;

	private int maxDistanceForClick = 70;

	public RecyclerItemClickListener(Context context, OnItemClickListener listener, OnLongItemClickListener onLongItemClickListener) {
		mClickListener = listener;
		mOnLongItemClickListener = onLongItemClickListener;
		mGestureDetector = new GestureDetectorCompat(context, getGestureListener());
	}

	@Override
	public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent e) {

		View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
		if (child != null && ViewCompat.hasOnClickListeners(child)) {
			child.performClick();
			return false;
		}

		this.recyclerView = recyclerView;
		return mGestureDetector.onTouchEvent(e);
	}

	@Override
	public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {
		mGestureDetector.onTouchEvent(motionEvent);
	}

	@Override
	public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
	}

	public GestureDetector.SimpleOnGestureListener getGestureListener() {
		return new GestureDetector.SimpleOnGestureListener() {

			private final int longClickTimeout = ViewConfiguration.getLongPressTimeout();

			@Override
			public boolean onSingleTapUp(MotionEvent e) {

				View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
				if (childView != null) {
					if (ViewCompat.hasOnClickListeners(childView)) {
						childView.performClick();
						return true;
					} else if (mClickListener != null) {
						int position = recyclerView.getChildAdapterPosition(childView);
						if (position != RecyclerView.NO_POSITION) {
							mClickListener.onItemClick(childView, recyclerView.getChildAdapterPosition(childView));
							return true;
						}
					}
				}

				return super.onSingleTapUp(e);
			}

			@Override
			public void onLongPress(MotionEvent e) {
				if (mOnLongItemClickListener != null) {
					View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
					int position = recyclerView.getChildAdapterPosition(childView);
					if (position != RecyclerView.NO_POSITION) {
						mOnLongItemClickListener.onLongItemClick(childView, recyclerView.getChildAdapterPosition(childView));
					}
				}
			}

			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
				float x1, x2, y1, y2;
				x1 = e1.getX();
				y1 = e1.getY();
				x2 = e2.getX();
				y2 = e2.getY();

				float distance = (float) Math.sqrt(((x1 - x2) * (x1 - x2)) + ((y1 - y2) * (y1 - y2)));

				if (distance < maxDistanceForClick) {
					long clickDuration = (e2.getEventTime() - e1.getEventTime());
					if (clickDuration < longClickTimeout) {
						return onSingleTapUp(e1);
					} else {
						onLongPress(e1);
					}
				}
				return super.onFling(e1, e2, velocityX, velocityY);
			}
		};
	}

	public int getMaxDistanceForClick() {
		return maxDistanceForClick;
	}

	public void setMaxDistanceForClick(int maxDistanceForClick) {
		this.maxDistanceForClick = maxDistanceForClick;
	}

	public interface OnItemClickListener {
		void onItemClick(View view, int position);
	}

	public interface OnLongItemClickListener {
		void onLongItemClick(View view, int position);
	}

}
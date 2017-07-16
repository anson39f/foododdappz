package com.app.OddfoodyDriver.ListItemDecorations;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;


/**
 * Created by NEXTBRAIN on 2/25/2017.
 */

public class OrderItemDecoration extends RecyclerView.ItemDecoration {

    private final int mItemOffset;

    private OrderItemDecoration(int itemOffset) {
        mItemOffset = itemOffset;
    }

    public OrderItemDecoration(@NonNull Context context, @DimenRes int itemOffsetId) {
        this(context.getResources().getDimensionPixelSize(itemOffsetId));
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        // Left , Top, Right, Bottom

        outRect.set(0, 0, 0, mItemOffset);
    }


}

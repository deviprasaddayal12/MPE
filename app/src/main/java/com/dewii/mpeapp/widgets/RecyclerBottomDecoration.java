package com.dewii.mpeapp.widgets;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerBottomDecoration extends RecyclerView.ItemDecoration {
    private int offsetTop;
    private int offsetBottom;
    private boolean top;

    public RecyclerBottomDecoration(int offset, boolean top) {
        if (top) {
            this.offsetTop = offset;
            this.offsetBottom = 0;
        } else {
            this.offsetTop = 0;
            this.offsetBottom = offset;
        }
    }

    public RecyclerBottomDecoration(int offsetTop, int offsetBottom) {
        this.offsetTop = offsetTop;
        this.offsetBottom = offsetBottom;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        if (state.getItemCount() == 1 || (state.getItemCount() > 0 && parent.getChildAdapterPosition(view) == 0))
            outRect.set(0, offsetTop, 0, 0);
        else if (state.getItemCount() > 0 && parent.getChildAdapterPosition(view) == state.getItemCount() - 1)
            outRect.set(0, 0, 0, offsetBottom);
        else
            outRect.set(0, 0, 0, 0);
    }
}

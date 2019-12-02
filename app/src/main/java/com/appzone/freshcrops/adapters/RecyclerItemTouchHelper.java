package com.appzone.freshcrops.adapters;

import android.graphics.Canvas;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import com.appzone.freshcrops.R;


public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {

    private SwipeListener listener;
    public RecyclerItemTouchHelper(int dragDirs, int swipeDirs,SwipeListener listener) {
        super(dragDirs, swipeDirs);
        this.listener = listener;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if (listener!=null)
        {
            Log.e("dir",direction+"");
            listener.onSwipe(viewHolder,direction,viewHolder.getAdapterPosition());

        }
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {

        if (viewHolder!=null)
        {
            View view_foreground = ((DelegateCollectOrderAdapter.MyHolder) viewHolder).ll_view_foreground;
            getDefaultUIUtil().onSelected(view_foreground);

        }



    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        View view_foreground = ((DelegateCollectOrderAdapter.MyHolder) viewHolder).ll_view_foreground;
        getDefaultUIUtil().clearView(view_foreground);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {


        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE)
        {
            DelegateCollectOrderAdapter.MyHolder myHolder = (DelegateCollectOrderAdapter.MyHolder) viewHolder;

            if (dX>0)
            {
                myHolder.ll_view_background.setBackgroundResource(R.color.discount_color);
                myHolder.ll_delete.setVisibility(View.VISIBLE);
                myHolder.ll_add.setVisibility(View.INVISIBLE);
            }else
                {
                    myHolder.ll_view_background.setBackgroundResource(R.drawable.edt_search_bg_cover_map);
                    myHolder.ll_add.setVisibility(View.VISIBLE);
                    myHolder.ll_delete.setVisibility(View.INVISIBLE);

                }

        }
        View view_foreground = ((DelegateCollectOrderAdapter.MyHolder) viewHolder).ll_view_foreground;

        getDefaultUIUtil().onDraw(c,recyclerView,view_foreground,dX,dY,actionState,isCurrentlyActive);

    }

    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View view_foreground = ((DelegateCollectOrderAdapter.MyHolder) viewHolder).ll_view_foreground;

        getDefaultUIUtil().onDrawOver(c,recyclerView,view_foreground,dX,dY,actionState,isCurrentlyActive);


    }


    public interface SwipeListener
    {
        void onSwipe(RecyclerView.ViewHolder viewHolder,int direction,int position);
    }
}

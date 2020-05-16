package com.example.testleftrightview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.animation.ScaleAnimation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author admin
 * desc: 使用ItemTouchHelper实现RecyclerView的item拖动位置交换
 */
public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ItemTouchHelper itemTouchHelper;
    private int dragFlags, swipeFlags;
    private List<String> mDatas = new ArrayList<>();
    private NormalRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        adapter = new NormalRecyclerViewAdapter(this);
        mRecyclerView.setAdapter(adapter);

        String str[] = getResources().getStringArray(R.array.titles);
        for (int i = 0; i < str.length; i++) {
            mDatas.add(str[i]);
        }

        itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                if (mRecyclerView.getLayoutManager() instanceof GridLayoutManager) {
                    dragFlags = ItemTouchHelper.UP |
                            ItemTouchHelper.DOWN |
                            ItemTouchHelper.LEFT |
                            ItemTouchHelper.RIGHT;
                    swipeFlags = 0;
                    return makeMovementFlags(dragFlags, swipeFlags);
                } else {
                    dragFlags = ItemTouchHelper.UP |
                            ItemTouchHelper.DOWN;
                    swipeFlags = 0;
                    return makeMovementFlags(dragFlags, swipeFlags);
                }
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();
                if (fromPosition < toPosition) {
                    for (int i = fromPosition; i < toPosition; i++) {
                        Collections.swap(mDatas, i, i + 1);
                    }
                } else {
                    for (int i = fromPosition; i > toPosition; i--) {
                        Collections.swap(mDatas, i, i - 1);
                    }
                }
                adapter.notifyItemMoved(fromPosition, toPosition);
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            }

            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                    viewHolder.itemView.setBackgroundColor(Color.LTGRAY);

                    // Animation
                    ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 1.2f, 1f, 1.2f);
                    scaleAnimation.setDuration(300);
                    scaleAnimation.setFillAfter(true);
                    viewHolder.itemView.startAnimation(scaleAnimation);
                }
                super.onSelectedChanged(viewHolder, actionState);
            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                viewHolder.itemView.setBackgroundColor(0);

                // Animation
                ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 1f, 1f, 1f);
                scaleAnimation.setDuration(0);
                scaleAnimation.setFillAfter(true);
                viewHolder.itemView.startAnimation(scaleAnimation);
            }

            @Override
            public boolean isLongPressDragEnabled() {
                return true;
            }
        });
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }
}

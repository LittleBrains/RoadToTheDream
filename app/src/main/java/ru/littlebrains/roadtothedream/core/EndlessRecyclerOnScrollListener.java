package ru.littlebrains.roadtothedream.core;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import trikita.log.Log;

/**
 * Created by Evgeny on 10.03.2016.
 */
public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {
    public static String TAG = EndlessRecyclerOnScrollListener.class.getSimpleName();

    private int previousTotal = 0; // The total number of items in the dataset after the last load
    private boolean loading = true; // True if we are still waiting for the last set of data to load.
    private int visibleThreshold = 5; // The minimum amount of items to have below your current scroll position before loading more.
    int firstVisibleItem, visibleItemCount, totalItemCount;

    private int current_page = 1;

    private RecyclerView.LayoutManager mlayoutManager;

    public EndlessRecyclerOnScrollListener(RecyclerView.LayoutManager layoutManager) {
        this.mlayoutManager = layoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = mlayoutManager.getItemCount();
        if(mlayoutManager instanceof GridLayoutManager){
            firstVisibleItem = ((GridLayoutManager)mlayoutManager).findFirstVisibleItemPosition();
        } else if(mlayoutManager instanceof  LinearLayoutManager){
            firstVisibleItem = ((LinearLayoutManager)mlayoutManager).findFirstVisibleItemPosition();
        }

        if (totalItemCount <= previousTotal) {
            previousTotal = totalItemCount;
            if (totalItemCount == 0) {
                loading = true; }
        }

        if (loading && (totalItemCount > previousTotal)) {
            loading = false;
            previousTotal = totalItemCount;
            current_page++;
        }

        if (!loading && (totalItemCount - visibleItemCount)<=(firstVisibleItem + visibleThreshold)) {
            onLoadMore(current_page);
            loading = true;
        }
    }

    public abstract void onLoadMore(int current_page);
}
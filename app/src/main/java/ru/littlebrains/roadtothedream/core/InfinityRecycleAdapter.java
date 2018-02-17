package ru.littlebrains.roadtothedream.core;

import android.app.Activity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.littlebrains.roadtothedream.R;
import trikita.log.Log;

/**
 * Created by Evgeny on 10.03.2016.
 */
abstract public class InfinityRecycleAdapter<T> extends RecyclerView.Adapter {
    protected final int VIEW_ITEM = 1;
    protected final int VIEW_PROG = 0;

    protected List<T> dataList = new ArrayList<>();
    private int serverListSize = -1;
    private boolean isUnknownHostException;
    private boolean isTimeoutException;
    protected Activity mActivity;
    private Reload reload;
    protected OnInternetErrorListener onInternetErrorListener;


    public InfinityRecycleAdapter(Activity activity, Reload reload) {
        mActivity = activity;
        this.reload = reload;
    }

    @Override
    public int getItemViewType(int position) {
        return position >= dataList.size() ? VIEW_PROG : VIEW_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            vh = getViewHolderItem(parent);
        }else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_progress, parent, false);

            vh = new ProgressViewHolder(v);
        }
        return vh;
    }

    abstract public RecyclerView.ViewHolder getViewHolderItem(ViewGroup parent);

    @Override
    public int getItemCount() {
        return dataList.size()+1;
    }

    public void addItems(List<T> items){
        if(items != null && dataList != null) {
            this.dataList.addAll(items);
        }
    }

    public void clear(){
        this.dataList.clear();
    }

    public void setServerListSize(int serverListSize){
        this.serverListSize = serverListSize;
    }
    protected int getServerListSize(){
        return  serverListSize;
    }



    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public AppCompatButton btn_reload;
        public ProgressBar progressBar;
        public TextView text_error;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
            text_error = (TextView) v.findViewById(R.id.text_error);
            btn_reload = (AppCompatButton) v.findViewById(R.id.btn_reload);
        }
    }

    protected void onBindFooterViewHolder(RecyclerView.ViewHolder holder, int position){
        ProgressViewHolder progressHolder = ((ProgressViewHolder) holder);
        String message = "";
        if(isUnknownHostException || isTimeoutException){
            message = "Не удалось загрузить данные, проверьте соединение с интернетом и обновите";
        }

        Log.d(position, getServerListSize());
        if(isTimeoutException || isUnknownHostException){
            Log.d("no internet", dataList.size());
            if(dataList.size() == 0 && onInternetErrorListener != null){
                onInternetErrorListener.onInternetErrorListener();
                progressHolder.progressBar.setVisibility(View.GONE);
                progressHolder.text_error.setVisibility(View.VISIBLE);
                progressHolder.text_error.setText(message);
                progressHolder.btn_reload.setVisibility(View.VISIBLE);
                return;
            } else {
                progressHolder.progressBar.setVisibility(View.GONE);
                progressHolder.text_error.setVisibility(View.VISIBLE);
                progressHolder.text_error.setText(message);
                progressHolder.btn_reload.setVisibility(View.VISIBLE);
            }
        }else if(position >= getServerListSize() && getServerListSize() >= 0) {
            progressHolder.progressBar.setVisibility(View.GONE);
            progressHolder.text_error.setVisibility(View.GONE);
            progressHolder.btn_reload.setVisibility(View.GONE);
        } else if(getServerListSize() < -1){
            progressHolder.progressBar.setVisibility(View.GONE);
            progressHolder.text_error.setVisibility(View.GONE);
            progressHolder.btn_reload.setVisibility(View.GONE);
        } else{
            progressHolder.progressBar.setVisibility(View.VISIBLE);
            progressHolder.text_error.setVisibility(View.GONE);
            progressHolder.btn_reload.setVisibility(View.GONE);
        }
        progressHolder.btn_reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(reload != null) {
                    error(false, false);
                    reload.onReload();
                }
            }
        });
    }

    public void error(boolean isUnknownHostException, boolean isTimeoutException) {
        this.isUnknownHostException = isUnknownHostException;
        this.isTimeoutException = isTimeoutException;
    }

    public interface Reload{
        void onReload();
    }

    public void setOnInternetErrorListener(OnInternetErrorListener onInternetErrorListener){
        this.onInternetErrorListener = onInternetErrorListener;
    }

    public interface OnInternetErrorListener{
        void onInternetErrorListener();
    }
}
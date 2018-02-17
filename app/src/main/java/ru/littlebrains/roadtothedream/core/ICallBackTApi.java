package ru.littlebrains.roadtothedream.core;

public interface ICallBackTApi<T> {

    public void onComplete(T result);
    public void onException(RequestException e);
}



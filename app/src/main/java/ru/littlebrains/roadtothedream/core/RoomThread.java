package ru.littlebrains.roadtothedream.core;

/**
 * Created by evgeniy on 30.11.2017.
 */

public class RoomThread<T> {
   /* private final WeakReference<MainActivity> mActivity;

    public interface TT<T>{
        T someThing(MyDatabase db);
    }

    public interface TTT<T>{
        void runOnUIThread(T r);
    }

    public RoomThread(final MainActivity activity, final TT<T> thread, final TTT<T> ui){
        mActivity = new WeakReference<MainActivity>(activity);

        new Thread(new Runnable() {
            @Override
            public void run() {
                if(mActivity.get() != null) {
                    final MyDatabase db = Room.databaseBuilder(activity,
                            MyDatabase.class, "populus-database").build();
                    final T t = thread.someThing(db);
                    if(mActivity.get() != null) {
                        mActivity.get().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ui.runOnUIThread(t);
                            }
                        });
                    }
                }
            }
        }).start();
    }*/
}

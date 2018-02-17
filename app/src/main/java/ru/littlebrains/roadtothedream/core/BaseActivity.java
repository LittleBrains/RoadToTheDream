package ru.littlebrains.roadtothedream.core;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.util.List;

import ru.littlebrains.roadtothedream.R;
import trikita.log.Log;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by evgeniy on 27.02.2017.
 */

public class BaseActivity extends AppCompatActivity {

    private static AlertDialog alertDialogInternetOff;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
                getSupportActionBar().setDisplayHomeAsUpEnabled(backStackEntryCount != 0);
            }
        });
    }

    public void newFragment(Fragment fragment, int tag, boolean canGoBack) {
        if(isVisibleFragment(tag)) return;
        newFragment(fragment, String.valueOf(tag), canGoBack);
    }

    public void newFragment(final Fragment fragment, final String tag, final boolean canGoBack) {
        if(isVisibleFragment(tag)) return;
        final int WHAT = 1;
        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == WHAT) changeFragment(fragment, tag, canGoBack);
            }
        };
        handler.sendEmptyMessage(WHAT);

    }

    private void changeFragment(Fragment fragment, String tag, boolean canGoBack) {
        Utils.hideKeyboard(this);
        try {
            if (canGoBack) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, fragment, tag)
                        .addToBackStack(null)
                        .commitAllowingStateLoss();
            } else {
                clearBackStack();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, fragment, tag)
                        .commitAllowingStateLoss();
            }
            getSupportActionBar().setDisplayHomeAsUpEnabled(canGoBack);
        }catch (IllegalStateException e){
            Log.d(e);
        }
    }

    public void clearBackStack(){
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        Utils.hideKeyboard(this);
    }

    public boolean backFragmet() {
        Utils.hideKeyboard(this);
        final int WHAT = 1;
        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                try {
                    if (msg.what == WHAT) getSupportFragmentManager().popBackStack();
                }catch(IllegalStateException e){

                }
            }
        };

        handler.sendEmptyMessage(WHAT);

        if(getSupportFragmentManager().getBackStackEntryCount() > 0){
            return false;
        }else {
            return true;
        }
    }


    public void refreshMenu() {
        invalidateOptionsMenu();
    }

    public Fragment getVisibleFragment(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if(fragments != null){
            for(Fragment fragment : fragments){
                if(fragment != null && fragment.isVisible())
                    return fragment;
            }
        }
        return null;
    }

    public boolean isVisibleFragment(int tag){
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(tag+"");
        if(fragment != null && fragment.isVisible())return true;
        return false;
    }

    public boolean isVisibleFragment(String tag){
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(tag+"");
        if(fragment != null && fragment.isVisible())return true;
        return false;
    }

    public Fragment getFragment(int tag){
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(tag+"");
        if(fragment != null && fragment.isVisible())return fragment;
        return null;
    }

    public int getCountFragment(){
        return getSupportFragmentManager().getBackStackEntryCount();
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    public static boolean checkInternet(final Context mContext){
        if(!Utils.isInternetOn(mContext)){
            if(alertDialogInternetOff != null && alertDialogInternetOff.isShowing()) return false;

            alertDialogInternetOff = new AlertDialog.Builder(mContext).create();
            alertDialogInternetOff.setMessage("Выключите режим \"в самолете\" или проверьте подключение к Интернету");
            alertDialogInternetOff.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialogInternetOff.setButton(AlertDialog.BUTTON_NEGATIVE, "Настройки",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent dialogIntent = new Intent(
                                    Settings.ACTION_SETTINGS);
                            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mContext.startActivity(dialogIntent);
                            dialog.dismiss();
                        }
                    });
            alertDialogInternetOff.show();
            return false;
        }else{
            return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(item.getItemId());
        if(item.getItemId() == android.R.id.home){
            Utils.hideKeyboard(this);
        }
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

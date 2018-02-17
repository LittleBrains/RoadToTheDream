package ru.littlebrains.roadtothedream.core;

import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Toast;

import ru.littlebrains.roadtothedream.R;


public abstract class BaseFragment extends Fragment {

	protected int LAYOUT_ID;

	protected View rootView;
	protected ActionBar actionBar;
    protected Toolbar toolbar;
	private View progress;
	private int orientation;
	protected BaseActivity mActivity;
	protected boolean isVisiblProgressLayout;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		setRetainInstance(true);
		mActivity = (BaseActivity) getActivity();
	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.show();
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        setSubtitle("");

		setLAYOUT_ID();

		setTitle(setTitle());

		if(isNoRotated()) return rootView;

		LAYOUT_ID = setLAYOUT_ID();
		rootView = inflater.inflate(LAYOUT_ID, container,  false);

		firstInitView();

		return rootView;
    }

	public abstract int setTitle();

    /* LAYOUT_ID = R.layout.fragment_blank;
    * */
    public abstract int setLAYOUT_ID();

    public abstract void firstInitView();

	protected boolean isNoRotated(){
		if(orientation == getActivity().getResources().getConfiguration().orientation && rootView != null) {
			return true;
		}
		orientation = getActivity().getResources().getConfiguration().orientation;
		return  false;
	}

	protected void setTitle(String title) {
		if(title == null){
			getBaseActivity().getSupportActionBar().setDisplayUseLogoEnabled(true);
			toolbar.setTitle("");
		}else{
			getBaseActivity().getSupportActionBar().setDisplayUseLogoEnabled(false);
			toolbar.setTitle(title);
		}
	}

	protected void setSubtitle(String title) {
		if(title == null){
			getBaseActivity().getSupportActionBar().setDisplayUseLogoEnabled(true);
			toolbar.setSubtitle("");
		}else{
			getBaseActivity().getSupportActionBar().setDisplayUseLogoEnabled(false);
			toolbar.setSubtitle(title);
		}
	}


	protected void setTitle(int idRes) {
		toolbar.setTitle(idRes);
	}

	public void setProgressLayout(boolean isVisible){
		setProgressLayout(isVisible, false);
	}

	public void setProgressLayout(boolean isVisible, boolean animation){
		if(isVisible && isVisiblProgressLayout) return;
		if(isVisible) {
			isVisiblProgressLayout = true;
			progress = getActivity().getLayoutInflater().inflate(R.layout.progress_layout, null, false);
			progress.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

				}
			});
			((ViewGroup) rootView).addView(progress);
		}else{
			if(animation) {
				AlphaAnimation anim = new AlphaAnimation(1.0f, 0.0f);
				anim.setDuration(250);
				anim.setAnimationListener(new Animation.AnimationListener() {
					@Override
					public void onAnimationStart(Animation animation) {

					}

					@Override
					public void onAnimationEnd(Animation animation) {
						isVisiblProgressLayout = false;
						((ViewGroup) rootView).removeView(progress);
					}

					@Override
					public void onAnimationRepeat(Animation animation) {

					}
				});
				progress.startAnimation(anim);
			}else {
				isVisiblProgressLayout = false;
				((ViewGroup) rootView).removeView(progress);
			}
		}
	}

	protected BaseActivity getBaseActivity(){
		return (BaseActivity) getActivity();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.clear();
	}


	protected boolean checkInternet(){
		if(!Utils.isInternetOn(getContext())){
			AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
			alertDialog.setMessage("Выключите режим \"в самолете\" или проверьте подключение к Интернету");
			alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Настройки",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							Intent dialogIntent = new Intent(
									Settings.ACTION_SETTINGS);
							dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							getContext().startActivity(dialogIntent);
							dialog.dismiss();
						}
					});
			alertDialog.show();
			return false;
		}else{
			return true;
		}
	}


	protected void showSimpleError(String error){
		Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
	}

	public void fadeInAnim(View v){
		v.setVisibility(View.VISIBLE);
		AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
		anim.setDuration(250);
		v.startAnimation(anim);
	}
}


package ru.littlebrains.roadtothedream;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import ru.littlebrains.roadtothedream.core.BaseFragment;
import ru.littlebrains.roadtothedream.core.Utils;
import trikita.log.Log;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends BaseFragment {

    private LinearLayout containerPlan;
    private StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);

    @Override
    public int setTitle() {
        return R.string.title_main;
    }

    @Override
    public int setLAYOUT_ID() {
        return R.layout.fragment_main;
    }

    @Override
    public void firstInitView() {

        rootView.findViewById(R.id.plan).setVisibility(View.GONE);
        rootView.findViewById(R.id.container_plan).setVisibility(View.GONE);
        rootView.findViewById(R.id.plans_done).setVisibility(View.GONE);

        final TextView descDream = rootView.findViewById(R.id.desc_dream);
        final EditText dream = (EditText) rootView.findViewById(R.id.dream);

        final String myDream = Utils.getSharedPreferences(getContext()).getString("my_dream", null);
        if(myDream != null && !myDream.isEmpty()){
            rootView.findViewById(R.id.plan).setVisibility(View.VISIBLE);
            rootView.findViewById(R.id.container_plan).setVisibility(View.VISIBLE);
            rootView.findViewById(R.id.plans_done).setVisibility(View.VISIBLE);
            rootView.findViewById(R.id.dream_done).setVisibility(View.GONE);
            dream.setVisibility(View.GONE);
            SpannableStringBuilder sb = new SpannableStringBuilder("Твоя мечта:\n" + myDream + "\nИди к ней!");
            sb.setSpan(bss, 11, sb.length()-11, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            sb.setSpan(new RelativeSizeSpan(1.2f), 11, sb.length()-11, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            descDream.setText(sb);
            descDream.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    descDream.setText("Опиши свою мечту");
                    dream.setVisibility(View.VISIBLE);
                    rootView.findViewById(R.id.dream_done).setVisibility(View.VISIBLE);
                    dream.setText(myDream);
                    descDream.setOnClickListener(null);
                }
            });
        }
        rootView.findViewById(R.id.dream_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dream.getText().toString().trim().length() > 0) {
                    rootView.findViewById(R.id.plan).setVisibility(View.VISIBLE);
                    rootView.findViewById(R.id.container_plan).setVisibility(View.VISIBLE);
                    rootView.findViewById(R.id.plans_done).setVisibility(View.VISIBLE);
                    Utils.getSharedPreferencesEditor(getContext()).putString("my_dream", dream.getText().toString()).commit();
                    view.setVisibility(View.GONE);
                    dream.setVisibility(View.GONE);
                    SpannableStringBuilder sb = new SpannableStringBuilder("Твоя мечта:\n" + dream.getText().toString() + "\nИди к ней!");
                    sb.setSpan(bss, 11, sb.length() - 11, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                    sb.setSpan(new RelativeSizeSpan(1.2f), 11, sb.length()-11, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    descDream.setText(sb);
                    descDream.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            descDream.setText("Опиши свою мечту");
                            dream.setVisibility(View.VISIBLE);
                            rootView.findViewById(R.id.dream_done).setVisibility(View.VISIBLE);
                            descDream.setOnClickListener(null);
                        }
                    });
                }
            }
        });



        rootView.findViewById(R.id.plans_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.getSharedPreferencesEditor(getContext()).putString("dream", dream.getText().toString()).commit();
                rootView.findViewById(R.id.plan).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.container_plan).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.plans_done).setVisibility(View.VISIBLE);
            }
        });


        containerPlan = (LinearLayout) rootView.findViewById(R.id.container_plan);


        ImageButton planDone = (ImageButton)rootView.findViewById(R.id.plan_done);
        planDone.setOnClickListener(new android.view.View.OnClickListener() {

            @Override
            public void onClick(View v) {
                addNewViewMaterial();
                v.setVisibility(View.GONE);
                updateNumberPlan();
            }
        });
        updateNumberPlan();

    }

    private void addNewViewMaterial(){
        Utils.hideKeyboard(mActivity);
        LinearLayout view = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.item_material, null);
        Log.d("index " + containerPlan.getChildCount());
        containerPlan.addView(view, containerPlan.getChildCount());

        TextView planNumber = (TextView) containerPlan.findViewById(R.id.plan_number);
        planNumber.setText((containerPlan.getChildCount() + 1) +". ");

        ImageButton remove = (ImageButton)view.findViewById(R.id.plan_remove);
        remove.setOnClickListener(new android.view.View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    LinearLayout view = (LinearLayout) v.getParent();
                    containerPlan.removeView(view);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                updateNumberPlan();
            }
        });

        ImageButton planDone = (ImageButton)view.findViewById(R.id.plan_done);
        planDone.setOnClickListener(new android.view.View.OnClickListener() {

            @Override
            public void onClick(View v) {
                addNewViewMaterial();
                v.setVisibility(View.GONE);
                updateNumberPlan();
            }
        });
    }

    private void updateNumberPlan(){
        for(int i = 0; i < containerPlan.getChildCount(); i++){
            TextView textView = (TextView) containerPlan.getChildAt(i).findViewById(R.id.plan_number);
            textView.setText(i+". ");
        }
    }

}

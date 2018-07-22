package com.maluta.bakingtime.adapteers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.maluta.bakingtime.R;
import com.maluta.bakingtime.model.Step;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by admin on 6/29/2018.
 */

@SuppressWarnings("DefaultFileTemplate")
public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepViewHolder> {
    private static StepClickListener mClickListener;
    private ArrayList<Step> mSteps;


    // Constructor
    public StepsAdapter(StepClickListener listener) {

        mClickListener = listener;
    }

    public void setData(ArrayList<Step> steps) {
        mSteps = steps;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StepsAdapter.StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.step_item, parent, false);

        return new StepViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StepViewHolder holder, int position) {
        // grabs the step from the clicked position
        Step currentStep = mSteps.get(position);

        Context context = holder.itemView.getContext();
        if (!currentStep.getVideoURL().isEmpty() && !currentStep.getThumbnailURL().isEmpty()) {
            Picasso.with(context).load(currentStep.getThumbnailURL())
                    .placeholder(R.drawable.video_no_thumb)
                    .error(R.drawable.alert_circle_outline)
                    .into(holder.mStepIv);
        } else if (!currentStep.getVideoURL().isEmpty() && currentStep.getThumbnailURL().isEmpty()) {
            holder.mStepIv.setImageResource(R.drawable.video_no_thumb);
        } else {
            holder.mStepIv.setImageResource(R.drawable.no_video);
        }

        if (position == 0){
            holder.mStepNumberTextView.setVisibility(View.GONE);
        } else {
            String stepNumber = String.valueOf(position) + ":";
            holder.mStepNumberTextView.setText(stepNumber);
        }

        String shortDescription = currentStep.getShortDescription();
        if (shortDescription.length() > 32) {
            holder.mStepShortDescription.setMinLines(2);
        }
        holder.mStepShortDescription.setText(shortDescription);
    }

    @Override
    public int getItemCount() {
        return mSteps == null ? 0 : mSteps.size();
    }


    public interface StepClickListener {
        void stepListItemCLick(ArrayList<Step> steps, int position);
    }

    public class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // binds the views needed to inflate the Steps list item
        @BindView(R.id.step_iv)
        ImageView mStepIv;
        @BindView(R.id.step_number_textView)
        TextView mStepNumberTextView;
        @BindView(R.id.step_short_description)
        TextView mStepShortDescription;

        public StepViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mClickListener.stepListItemCLick(mSteps, position);
        }
    }
}

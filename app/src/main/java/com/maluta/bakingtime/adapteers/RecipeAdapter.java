package com.maluta.bakingtime.adapteers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.maluta.bakingtime.DetailActivity;
import com.maluta.bakingtime.R;
import com.maluta.bakingtime.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by admin on 6/27/2018.
 */

@SuppressWarnings("DefaultFileTemplate")
public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeHolder> {
    private ArrayList<Recipe> recipes;
    private ItemClickListener mItemClickListener;

    public interface ItemClickListener {
        void onClick(Recipe recipe);
    }


    public RecipeAdapter(ItemClickListener itemClickListener) {

        mItemClickListener = itemClickListener;
    }

    public void setData (ArrayList<Recipe> items){
        recipes = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecipeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_card_item, parent, false);

        return new RecipeHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeHolder holder, int position) {
        final Recipe recipe = recipes.get(position);
        Context context = holder.itemView.getContext();
        String imageUri = recipe.getImage();

        if (!imageUri.isEmpty()) {
            Picasso.with(context).load(imageUri)
                    .placeholder(R.drawable.cake)
                    .error(R.drawable.alert_circle_outline)
                    .into(holder.mRecipeIv);
        } else {
            holder.mRecipeIv.setImageResource(R.drawable.cake);
        }

        String recipeName = recipe.getName();
        holder.mRecipeNameTv.setText(recipeName);
        String steps = context.getString(R.string.step_label) + String.valueOf(recipe.getSteps().size());
        holder.mStepTv.setText(steps);
        String servings = context.getString(R.string.servings_label) + recipe.getServings().toString();
        holder.mServingsTv.setText(servings);
    }

    @Override
    public int getItemCount() {
        return recipes == null ? 0 : recipes.size();
    }

    public class RecipeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // binds the views needed for this adapter
        @BindView(R.id.recipe_iv)
        ImageView mRecipeIv;
        @BindView(R.id.recipe_name_tv)
        TextView mRecipeNameTv;
        @BindView(R.id.step_label)
        TextView mStepTv;
        @BindView(R.id.servings_label)
        TextView mServingsTv;

        public RecipeHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mItemClickListener.onClick(recipes.get(position));
        }
    }
}

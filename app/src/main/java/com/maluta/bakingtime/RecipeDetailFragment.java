package com.maluta.bakingtime;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.diegodobelo.expandingview.ExpandingItem;
import com.diegodobelo.expandingview.ExpandingList;
import com.maluta.bakingtime.adapteers.StepsAdapter;
import com.maluta.bakingtime.model.Ingredient;
import com.maluta.bakingtime.model.Recipe;
import com.maluta.bakingtime.model.Step;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by admin on 6/29/2018.
 */

@SuppressWarnings("ALL")
public class RecipeDetailFragment extends Fragment implements StepsAdapter.StepClickListener {
    @BindView(R.id.steps_rv)
    RecyclerView mStepsRV;
    @BindView(R.id.ingredients_rv)
    RecyclerView mIngredientsRV;

    private Recipe mReceivedRecipe;
    private ArrayList<Step> mSteps = new ArrayList<>();
    private ArrayList<Ingredient> mIngredients = new ArrayList<>();
    private StepListener stepListener;

    private ImageView mListArrow;

    // * * THIS IS DIEGOS EXPANDING STUFF
    ExpandingList mExpandingList;
    ExpandingItem mItem;
    // * * * *

    RotateAnimation mAnim;
    RotateAnimation mAnim2;

    StepsAdapter mStepsAdapter;
    LinearLayoutManager mLinearLayoutManager;


    // Required empty public constructor
    public RecipeDetailFragment() {
    }

    public interface StepListener {
        void onStepClicked(ArrayList<Step> steps, int position);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            stepListener = (StepListener) context;
        } catch (ClassCastException ec) {
            throw new ClassCastException(context.toString()
                    + " must implement StepListener");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        ButterKnife.bind(this, view);

        // retains fragment across orientation change so it doesn't re-create the fragment on change
        setRetainInstance(true);

        mSteps = (ArrayList<Step>) mReceivedRecipe.getSteps();
        // instantiates the steps adapter and applies to steps recyclerview
        mStepsAdapter = new StepsAdapter( this);

        if (mSteps != null) {
            // applies the settings for our layoutManager and sets it on the recyclerView
            mLinearLayoutManager = new LinearLayoutManager(view.getContext());
            mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mStepsRV.setLayoutManager(mLinearLayoutManager);


            mStepsAdapter.setData(mSteps);
            mStepsRV.setAdapter(mStepsAdapter);
        }

        makeAnims();

        mIngredients = (ArrayList<Ingredient>) mReceivedRecipe.getIngredients();
        int ingredientArraySize = mIngredients.size();
        // Creates expanding list
        mExpandingList = view.findViewById(R.id.expanding_list_diego);
        // Creates the items and applies the data into our expandingLists views
        mItem = mExpandingList.createNewItem(R.layout.expanding_layout);
        TextView expandableTitle = mItem.findViewById(R.id.title_ingredients_list);
        expandableTitle.setText(R.string.ingredient_list_header);
        mItem.createSubItems(ingredientArraySize);
        mListArrow = view.findViewById(R.id.listArrow);
        mListArrow.setBackgroundResource(R.drawable.ic_expand_more_black_24dp);

        mListArrow.setAnimation(mAnim);
        mItem.setStateChangedListener(new ExpandingItem.OnItemStateChanged() {
            @Override
            public void itemCollapseStateChanged(boolean expanded) {
                if (expanded){
                    mListArrow.startAnimation(mAnim);
                    mListArrow.setAnimation(mAnim2);
                } else {
                    mListArrow.startAnimation(mAnim2);
                    mListArrow.setAnimation(mAnim);
                }
            }
        });

        // for loop to iterate through all ingredients and populate sub item textViews
        for (int position = 0; position < ingredientArraySize; position++) {
            // Ingredient being used for current sub item position
            Ingredient currentIngredient = mIngredients.get(position);
            String ingredientName = currentIngredient.getIngredient();
            String ingredientMeasure = currentIngredient.getMeasure();
            String ingredientQuantity = String.valueOf(currentIngredient.getQuantity());
            String fullMeasureString = ingredientQuantity + " " + ingredientMeasure + " " + ingredientName;
            Log.d("current ingredient is: ", fullMeasureString);

            View currentSubItem = mItem.getSubItemView(position);

            ((TextView) currentSubItem.findViewById(R.id.measure_textView))
                    .setText(getString(R.string.measure_string_format, ingredientQuantity, ingredientMeasure));

            ((TextView) currentSubItem.findViewById(R.id.ingredientNameTextView))
                    .setText(ingredientName);

            if (ingredientName.length() > 45) {
                ((TextView) currentSubItem.findViewById(R.id.ingredientNameTextView)).setTextSize(14);
                ((TextView) currentSubItem.findViewById(R.id.ingredientNameTextView)).setMinLines(2);
            }
        }

        return view;
    }

    public void setRecipeData(Recipe recipe){
        mReceivedRecipe = recipe;
    }

    // updates anim to updated drawable for the List Arrow imageView
    private void makeAnims() {
        mAnim = new RotateAnimation(180, 0, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mAnim.setDuration(300);
        mAnim.setInterpolator(new LinearInterpolator());
        mAnim.setFillAfter(true);


        mAnim2 = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mAnim2.setDuration(300);
        mAnim2.setInterpolator(new LinearInterpolator());
        mAnim2.setFillAfter(true);
    }

    @Override
    public void stepListItemCLick(ArrayList<Step> steps, int position) {
        stepListener.onStepClicked(steps, position);
    }
}

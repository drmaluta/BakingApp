package com.maluta.bakingtime.model;

import java.util.ArrayList;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by admin on 6/26/2018.
 */

@SuppressWarnings("ALL")
public class Recipe implements Parcelable {
    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("ingredients")
    @Expose
    private List<Ingredient> ingredients = null;
    @SerializedName("steps")
    @Expose
    private List<Step> steps = null;
    @SerializedName("servings")
    @Expose
    private Long servings;
    @SerializedName("image")
    @Expose
    private String image;



    /**
     * No args constructor for use in serialization
     */
    public Recipe() {
    }

    public Recipe(Long id, String name, List<Ingredient> ingredients, List<Step> steps, Long servings, String image) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
        this.servings = servings;
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }


    public List<Step> getSteps() {
        return steps;
    }

    public Long getServings() {
        return servings;
    }

    public String getImage() {
        return image;
    }

    public final static Parcelable.Creator<Recipe> CREATOR = new Creator<Recipe>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        public Recipe[] newArray(int size) {
            return (new Recipe[size]);
        }

    };


    protected Recipe(Parcel in) {
        this.id = ((Long) in.readValue((Long.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        ingredients = new ArrayList<>();
        in.readList(this.ingredients, (com.maluta.bakingtime.model.Ingredient.class.getClassLoader()));
        steps = new ArrayList<>();
        in.readList(this.steps, (com.maluta.bakingtime.model.Step.class.getClassLoader()));
        this.servings = ((Long) in.readValue((Long.class.getClassLoader())));
        this.image = ((String) in.readValue((String.class.getClassLoader())));
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(name);
        dest.writeList(ingredients);
        dest.writeList(steps);
        dest.writeValue(servings);
        dest.writeValue(image);
    }

    public int describeContents() {
        return 0;
    }
}
